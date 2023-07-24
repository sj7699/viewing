package com.ssafy.interviewstudy.domain.board;

import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.domain.study.Study;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@DiscriminatorValue("study_board")
@PrimaryKeyJoinColumn(name = "article_id")
public class StudyBoard extends Board{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id")
    private Study study;

    @Builder
    public StudyBoard(Integer id, Member author, String title, String content, LocalDateTime createdAt, LocalDateTime updatedAt, Integer viewCount) {
        super(id, author, title, content, createdAt, updatedAt, viewCount);
    }
}
