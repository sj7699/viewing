package com.ssafy.interviewstudy.repository.member;

import com.ssafy.interviewstudy.domain.board.ArticleLike;
import com.ssafy.interviewstudy.domain.board.Board;
import com.ssafy.interviewstudy.domain.board.BoardType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberArticleLikeRepository extends JpaRepository<Board,Integer> {

    //멤버 ID(PK)로 좋아요한 게시글 가져오기

    //자유게시판에서 좋아요 한 게시글 가져오기
    @Query("select a from Board a inner join a.likes l where l.member.id=:id and Type(a) = FreeBoard ")
    public List<Board> getArticleByMemberIdAtFreeBoard(@Param("id") Integer memberId);

    //질문 게시판에서 좋아요 한 게시글 가져오기
    @Query("select a from Board a inner join a.likes l where l.member.id=:id and Type(a) = QuestionBoard ")
    public List<Board> getArticleByMemberIdAtQuestionBoard(@Param("id") Integer memberId);

    //면접후기 게시판에서 좋아요 한 게시글 가져오기
    @Query("select a from Board a inner join a.likes l where l.member.id=:id and Type(a) = InterviewReviewBoard ")
    public List<Board> getArticleByMemberIdAtInterviewReviewBoard(@Param("id") Integer memberId);

}
