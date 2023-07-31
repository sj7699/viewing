package com.ssafy.interviewstudy.domain.board;

import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.domain.study.Study;
import com.ssafy.interviewstudy.dto.board.BoardRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class StudyBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id")
    private Study study;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member author;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "content", nullable = false, length = 5000)
    private String content;

    @Column(name = "created_at", nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;
    @Column(name = "updated_at", nullable = false)
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @ColumnDefault("0")
    @Column(name = "view_count", insertable = false)
    private Integer viewCount;

    @OneToMany(mappedBy = "article")
    private List<ArticleComment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "article")
    private List<ArticleFile> files = new ArrayList<>();

    @OneToMany(mappedBy = "article")
    private List<ReportArticle> reports = new ArrayList<>();

    public void modifyArticle(BoardRequest boardRequest) {
        this.title = boardRequest.getTitle();
        this.content = boardRequest.getContent();
    }

    public void updateViewCount(){
        this.viewCount += 1;
    }

    @Builder
    public StudyBoard(Integer id, Study study, Member author, String title, String content, LocalDateTime createdAt, LocalDateTime updatedAt, Integer viewCount, List<ArticleComment> comments, List<ArticleFile> files, List<ReportArticle> reports) {
        this.id = id;
        this.study = study;
        this.author = author;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.viewCount = viewCount;
        this.comments = comments;
        this.files = files;
        this.reports = reports;
    }
}
