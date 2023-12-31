package com.ssafy.interviewstudy.service.board;

import com.ssafy.interviewstudy.domain.board.Board;
import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.dto.board.Author;
import com.ssafy.interviewstudy.dto.board.BoardRequest;
import com.ssafy.interviewstudy.dto.board.BoardResponse;
import com.ssafy.interviewstudy.repository.member.MemberRepository;
import com.ssafy.interviewstudy.service.redis.ArticleLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardDtoService {
    private final MemberRepository memberRepository;
    private final ArticleLikeService articleLikeService;

    public Board toEntity(BoardRequest boardRequest) {
        Member author = memberRepository.findMemberById(boardRequest.getMemberId());

        Board article = Board.builder()
                .title(boardRequest.getTitle())
                .content(boardRequest.getContent())
                .author(author)
                .boardType(boardRequest.getBoardType())
                .build();

        return article;
    }

    public BoardResponse fromEntityWithoutContent(Board article) {
        BoardResponse boardResponse = BoardResponse.builder()
                .articleId(article.getId())
                .author(new Author(article.getAuthor()))
                .title(article.getTitle())
                .viewCount(article.getViewCount())
                .commentCount(article.getComments().size())
                .likeCount(articleLikeService.getLikeCount(article.getId()))
                .build();

        return boardResponse;
    }

    public BoardResponse fromEntity(Integer memberId, Board article) {
        BoardResponse boardResponse = fromEntityWithoutContent(article);

        if (memberId != null)
            boardResponse.setIsLike(articleLikeService.checkMemberLikeArticle(article.getId(), memberId));

        boardResponse.setContent(article.getContent());
        boardResponse.setCreatedAt(article.getCreatedAt());
        boardResponse.setUpdatedAt(article.getUpdatedAt());

        return boardResponse;
    }
}
