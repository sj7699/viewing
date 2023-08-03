package com.ssafy.interviewstudy.service.board;

import com.ssafy.interviewstudy.domain.board.ArticleComment;
import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.dto.board.Author;
import com.ssafy.interviewstudy.dto.board.CommentReplyResponse;
import com.ssafy.interviewstudy.dto.board.CommentRequest;
import com.ssafy.interviewstudy.dto.board.CommentResponse;
import com.ssafy.interviewstudy.repository.board.ArticleCommentRepository;
import com.ssafy.interviewstudy.repository.board.BoardRepository;
import com.ssafy.interviewstudy.repository.board.CommentLikeRepository;
import com.ssafy.interviewstudy.repository.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentDtoService {

    private MemberRepository memberRepository;
    private BoardRepository boardRepository;
    private CommentLikeRepository commentLikeRepository;
    private ArticleCommentRepository commentRepository;

    @Autowired
    public CommentDtoService(MemberRepository memberRepository, BoardRepository boardRepository, CommentLikeRepository commentLikeRepository, ArticleCommentRepository commentRepository) {
        this.memberRepository = memberRepository;
        this.boardRepository = boardRepository;
        this.commentLikeRepository = commentLikeRepository;
        this.commentRepository = commentRepository;
    }

    public ArticleComment toEntity(CommentRequest commentRequest){
        Member author = memberRepository.findMemberById(commentRequest.getMemberId());

        ArticleComment articleComment = ArticleComment.builder()
                .author(author)
                .article(boardRepository.findById(commentRequest.getArticleId()).get())
                .isDelete(false)
                .content(commentRequest.getContent()).build();

        return articleComment;
    }

    public ArticleComment toEntityWithParent(Integer commentId, CommentRequest commentRequest){
        ArticleComment comment = toEntity(commentRequest);
        comment.setComment(commentRepository.findById(commentId).get());

        return comment;
    }

    public CommentResponse fromEntityWithoutCommentCount(Integer memberId, ArticleComment articleComment){
        CommentResponse commentResponse = CommentResponse.builder()
                .id(articleComment.getId())
                .content(articleComment.getContent())
                .author(new Author(articleComment.getAuthor()))
                .isDelete(articleComment.getIsDelete())
                .createdAt(articleComment.getCreatedAt())
                .updatedAt(articleComment.getUpdatedAt())
                .likeCount(commentLikeRepository.countByComment(articleComment))
                .isLike(commentLikeRepository.existsByMember_IdAndComment_Id(memberId, articleComment.getId()))
                .build();

        return commentResponse;
    }

    public CommentResponse fromEntity(Integer memberId, ArticleComment articleComment){
        CommentResponse commentResponse = CommentResponse.builder()
                .id(articleComment.getId())
                .content(articleComment.getContent())
                .author(new Author(articleComment.getAuthor()))
                .isDelete(articleComment.getIsDelete())
                .createdAt(articleComment.getCreatedAt())
                .updatedAt(articleComment.getUpdatedAt())
                .likeCount(commentLikeRepository.countByComment(articleComment))
                .commentCount(commentRepository.countByComment(articleComment.getId()))
                .build();

        if(memberId != null)
            commentResponse.setIsLike(commentLikeRepository.existsByMember_IdAndComment_Id(memberId, articleComment.getId()));

        commentResponse.setReplies(fromEntity(memberId, articleComment.getReplies()));
        commentResponse.setCommentCount(commentRepository.countByComment(articleComment.getId()));

        return commentResponse;
    }

    public List<CommentReplyResponse> fromEntity(Integer memberId, List<ArticleComment> replies){
        List<CommentReplyResponse> replyResponses = new ArrayList<>();
        for (ArticleComment c: replies) {
            if(memberId != null){
                replyResponses.add(CommentReplyResponse.builder()
                        .id(c.getId())
                        .content(c.getContent())
                        .author(new Author(c.getAuthor()))
                        .isDelete(c.getIsDelete())
                        .createdAt(c.getCreatedAt())
                        .updatedAt(c.getUpdatedAt())
                        .likeCount(commentLikeRepository.countByComment(c))
                        .isLike(commentLikeRepository.existsByMember_IdAndComment_Id(memberId, c.getId()))
                        .build());
            }else{
                replyResponses.add(CommentReplyResponse.builder()
                        .id(c.getId())
                        .content(c.getContent())
                        .author(new Author(c.getAuthor()))
                        .isDelete(c.getIsDelete())
                        .createdAt(c.getCreatedAt())
                        .updatedAt(c.getUpdatedAt())
                        .likeCount(commentLikeRepository.countByComment(c))
                        .build());
            }

        }
        return replyResponses;
    }

}