package com.ssafy.interviewstudy.repository.board;

import com.ssafy.interviewstudy.domain.board.ArticleLike;
import com.ssafy.interviewstudy.domain.board.Board;
import com.ssafy.interviewstudy.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface ArticleLikeRepository extends JpaRepository<ArticleLike, Integer> {

    Boolean existsByMember_IdAndId(Integer memberId, Integer articleId);

    // 이미 좋아요를 누른 회원인지 체크
    Boolean existsByMemberAndArticle(Member member, Board article);

    // 글마다 좋아요 수 체크
    Integer countByArticle(Board article);

    @Transactional
    Integer removeByArticleAndMember(Board article, Member member);
}

