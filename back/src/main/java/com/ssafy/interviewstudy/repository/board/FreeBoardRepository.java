package com.ssafy.interviewstudy.repository.board;

import com.ssafy.interviewstudy.domain.board.FreeBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FreeBoardRepository extends JpaRepository<FreeBoard, Integer> {

    // 게시글과 작성자 정보를 함께 조회하는 쿼리
    @Query("select b from FreeBoard b JOIN FETCH b.author")
    List<FreeBoard> findAllWithAuthor();

    // 검색조건 : 제목 제목+내용 작성자
    @Query("select b from FreeBoard b where b.title like %:keyword%")
    Page<FreeBoard> findByTitleContaining(@Param("keyword") String keyword, Pageable pageable);

    // 제목+내용
    @Query("select b from FreeBoard b where b.title like %:keyword% or b.content like %:keyword%")
    Page<FreeBoard> findByTitleOrContent(@Param("keyword") String keyword, Pageable pageable);

    // 작성자
    @Query("SELECT b FROM FreeBoard b where b.author.nickname like %:keyword%")
    Page<FreeBoard> findWithAuthor(@Param("keyword") String keyword, Pageable pageable);

    // 내가 쓴 글 조회
    @Query("select b from FreeBoard b where b.author.id = :memberId")
    Page<FreeBoard> findByMemberId(Integer memberId, Pageable pageable);
}
