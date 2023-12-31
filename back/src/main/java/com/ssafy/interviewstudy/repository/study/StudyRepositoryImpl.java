package com.ssafy.interviewstudy.repository.study;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.DateTimeTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.domain.member.QMember;
import com.ssafy.interviewstudy.domain.study.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

import static com.ssafy.interviewstudy.domain.member.QMember.member;
import static com.ssafy.interviewstudy.domain.study.QCompany.company;
import static com.ssafy.interviewstudy.domain.study.QStudy.study;
import static com.ssafy.interviewstudy.domain.study.QStudyBookmark.studyBookmark;
import static com.ssafy.interviewstudy.domain.study.QStudyMember.studyMember;
import static com.ssafy.interviewstudy.domain.study.QStudyTag.studyTag;
import static com.ssafy.interviewstudy.domain.study.QStudyTagType.studyTagType;

@Repository
public class StudyRepositoryImpl implements StudyRepositoryCustom{
    @PersistenceContext
    private EntityManager em;
    private JPAQueryFactory queryFactory;

    @PostConstruct
    public void init(){
        queryFactory = new JPAQueryFactory(em);
    }

    @Override
    //조건에 따라 study 검색 결과
    public Page<Tuple> findStudiesBySearch(Boolean isRecruit, String appliedCompany, String appliedJob, CareerLevel careerLevel, Integer tag, Pageable pageable) {
        List<Tuple> result = queryFactory.select(
                        study.id,
                        study
                ).distinct()
                .from(study)
                .leftJoin(study.studyTags, studyTag)
                .where(isRecruitTrue(isRecruit),
                        study.isDelete.eq(false),
                        appliedCompanyLike(appliedCompany),
                        appliedJobLike(appliedJob),
                        careerLevelEq(careerLevel),
                        tagEq(tag),
                        inDeadline())
                .orderBy(study.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = studyCount(isRecruit, appliedCompany, appliedJob, careerLevel, tag);
        return new PageImpl<>(result, pageable, count);
    }

    @Override
    public List<Tuple> isBookmark(Integer memberId, List<Integer> studyIds){
        List<Tuple> result = queryFactory.select(
                        new CaseBuilder().when(studyBookmark.member.id.isNotNull()).then(true).otherwise(false),
                        JPAExpressions.select(studyMember.count()).from(studyMember).where(studyMember.study.id.eq(study.id)),
                        study.id
                ).distinct()
                .from(study)
                .leftJoin(studyBookmark).on(study.id.eq(studyBookmark.study.id), isBookmarked(memberId))
                .where(study.id.in(studyIds),
                        study.isDelete.eq(false))
                .orderBy(study.id.desc())
                .fetch();
        return result;
    }

    @Override
    public List<Tuple> findBookmarksMemberCountByMember(Member member){
        List<Tuple> result = queryFactory.select(study,
                        JPAExpressions.select(studyMember.count()).from(studyMember).where(studyMember.study.id.eq(study.id))
                )
                .from(study)
                .join(study.studyBookmarks, studyBookmark).fetchJoin()
                .where(studyBookmark.member.eq(member),
                        study.isDelete.eq(false))
                .fetch();
        return result;
    }

    @Override
    public List<Tuple> findMyStudyMemberCountByMember(Member member){
        List<Tuple> result = queryFactory.select(study,
                    new CaseBuilder().when(studyBookmark.member.id.isNotNull()).then(true).otherwise(false),
                    JPAExpressions.select(studyMember.count()).from(studyMember).where(studyMember.study.id.eq(study.id))

                )
                .from(study)
                .join(study.studyMembers, studyMember).fetchJoin()
                .leftJoin(studyBookmark).on(study.id.eq(studyBookmark.study.id), studyBookmark.member.eq(member))
                .where(studyMember.member.eq(member),
                        study.isDelete.eq(false))
                .fetch();
        return result;
    }

    private Long studyCount(Boolean isRecruit, String appliedCompany, String appliedJob, CareerLevel careerLevel, Integer tag){
        Long count = 0L;
        if(tag == null){
            count = queryFactory
                    .select(study.count())
                    .from(study)
                    .where(isRecruitTrue(isRecruit),
                            study.isDelete.eq(false),
                            appliedCompanyLike(appliedCompany),
                            appliedJobLike(appliedJob),
                            careerLevelEq(careerLevel),
                            inDeadline())
                    .fetchOne();
        }
        else {
            count = queryFactory
                    .select(study.count())
                    .from(study)
                    .leftJoin(study.studyTags, studyTag)
                    .where(isRecruitTrue(isRecruit),
                            study.isDelete.eq(false),
                            appliedCompanyLike(appliedCompany),
                            appliedJobLike(appliedJob),
                            careerLevelEq(careerLevel),
                            tagEq(tag),
                            inDeadline())
                    .fetchOne();
        }
        return count;
    }

    private BooleanExpression appliedCompanyLike(String appliedCompany){
        return StringUtils.hasText(appliedCompany) ? study.appliedCompany.name.like("%" + appliedCompany + "%") : null;
    }

    private BooleanExpression appliedJobLike(String appliedJob){
        return StringUtils.hasText(appliedJob) ? study.appliedJob.like("%"+appliedJob+"%") : null;
    }

    private BooleanExpression careerLevelEq(CareerLevel careerLevel){
        return (careerLevel != null && careerLevel != CareerLevel.ALL) ? study.careerLevel.eq(careerLevel) : null;
    }

    private BooleanExpression isRecruitTrue(Boolean isRecruit){
        return isRecruit != null && isRecruit != false ? study.isRecruit.eq(true) : null;
    }

    private BooleanExpression isBookmarked(Integer memberId){
        return memberId != null ? studyBookmark.member.id.eq(memberId) : studyBookmark.member.id.isNull();
    }

    private BooleanExpression tagEq(Integer tag){
        return tag != null ? studyTag.tag.id.eq(tag) : null;
    }

    private BooleanExpression inDeadline() {
        return Expressions.dateTimeTemplate(LocalDateTime.class,"date(now())").loe(Expressions.dateTimeTemplate(LocalDateTime.class,"date({0})", study.deadline));
    }
}
