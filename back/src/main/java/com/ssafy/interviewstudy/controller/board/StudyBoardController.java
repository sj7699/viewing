package com.ssafy.interviewstudy.controller.board;

import com.ssafy.interviewstudy.annotation.JWTRequired;
import com.ssafy.interviewstudy.annotation.MemberInfo;
import com.ssafy.interviewstudy.domain.board.BoardType;
import com.ssafy.interviewstudy.dto.board.BoardRequest;
import com.ssafy.interviewstudy.dto.board.StudyBoardResponse;
import com.ssafy.interviewstudy.dto.member.jwt.JWTMemberInfo;
import com.ssafy.interviewstudy.service.board.StudyBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/studies/{studyId}/boards")
@RestController
@RequiredArgsConstructor
public class StudyBoardController {

    private final StudyBoardService boardService;

    @JWTRequired(required = true)
    @GetMapping("/{articleId}")
    public ResponseEntity<?> articleDetail(@PathVariable Integer articleId){
        StudyBoardResponse boardResponse = boardService.findArticle(articleId);

        return ResponseEntity.ok(boardResponse);
    }

    // 글 저장
    @JWTRequired(required = true)
    @PostMapping
    public ResponseEntity<?> articleAdd(@PathVariable Integer studyId, @MemberInfo JWTMemberInfo memberInfo,
                                        @RequestBody BoardRequest boardRequest){
        boardRequest.setMemberId(memberInfo.getMemberId());
        System.out.println(studyId);
        boardRequest.setStudyId(studyId);
        Integer articleId = boardService.saveBoard(boardRequest);

        return ResponseEntity.ok(articleId);
    }

//    @Authority(authorityType = AuthorityType.Member_Board)
    @JWTRequired(required = true)
    @PutMapping("/{articleId}")
    public ResponseEntity<?> articleModify(@PathVariable Integer studyId, @PathVariable Integer articleId,
                                           @MemberInfo JWTMemberInfo memberInfo,
                                           @RequestBody BoardRequest boardRequest){
        boardRequest.setMemberId(memberInfo.getMemberId());
        boardRequest.setStudyId(studyId);
        StudyBoardResponse response = boardService.modifyArticle(articleId, boardRequest);

        return ResponseEntity.ok(response);
    }

//    @Authority(authorityType = AuthorityType.Member_Board)
    @JWTRequired(required = true)
    @DeleteMapping("/{articleId}")
    public ResponseEntity<?> articleRemove(@PathVariable Integer articleId){
        Integer response = boardService.removeArticle(articleId);

        if(response == 0)
            return ResponseEntity.badRequest().body("없는 게시물입니다.");

        return ResponseEntity.ok(response);
    }

    // 글 검색(키워드 없으면 전체 글 조회
    @JWTRequired(required = true)
    @GetMapping
    public ResponseEntity<?> articleList(@PathVariable Integer studyId, @RequestParam(value = "searchBy", required = false) String searchBy,
                                         @RequestParam(value = "keyword", required = false) String keyword, Pageable pageable){
        List<StudyBoardResponse> boardResponses;
        if(StringUtils.hasText(keyword))
            boardResponses = boardService.findArticleByKeyword(studyId, searchBy, keyword, pageable);
        else boardResponses = boardService.findBoardList(studyId, pageable);

        return ResponseEntity.ok(boardResponses);
    }

}