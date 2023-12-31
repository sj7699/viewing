package com.ssafy.interviewstudy.dto.study;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.querydsl.core.annotations.QueryProjection;
import com.ssafy.interviewstudy.domain.study.CareerLevel;
import com.ssafy.interviewstudy.domain.study.Study;
import com.ssafy.interviewstudy.domain.study.StudyTag;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class StudyDtoResponse {
    private Integer studyId;

    private String title;

    private String description;

    private String appliedCompany;

    private String appliedJob;

    private Integer capacity;

    private Long headCount;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime deadline;

    private Boolean recruitment;

    private Boolean bookmark;

    private Leader leader;

    private CareerLevel careerLevel;

    private List<String> tags = new ArrayList<>();

    public StudyDtoResponse(Study study){
        this.studyId = study.getId();
        this.title = study.getTitle();
        this.description = study.getDescription();
        this.appliedCompany = study.getAppliedCompany().getName();
        this.appliedJob = study.getAppliedJob();
        this.capacity = study.getCapacity();
        this.createdAt = study.getCreatedAt();
        this.deadline = study.getDeadline();
        this.recruitment = study.getIsRecruit();
        this.leader = new Leader(study.getLeader());
        this.careerLevel = study.getCareerLevel();
        List<StudyTag> tags = study.getStudyTags();
        for (StudyTag tag : tags) {
            this.tags.add(tag.getTag().getTagName());
        }
    }

    public StudyDtoResponse(Study study, Long headCount){
        this.studyId = study.getId();
        this.title = study.getTitle();
        this.description = study.getDescription();
        this.appliedCompany = study.getAppliedCompany().getName();
        this.appliedJob = study.getAppliedJob();
        this.capacity = study.getCapacity();
        this.createdAt = study.getCreatedAt();
        this.deadline = study.getDeadline();
        this.recruitment = study.getIsRecruit();
        this.leader = new Leader(study.getLeader());
        this.careerLevel = study.getCareerLevel();
        List<StudyTag> tags = study.getStudyTags();
        for (StudyTag tag : tags) {
            this.tags.add(tag.getTag().getTagName());
        }
        this.headCount = headCount;
    }

    public StudyDtoResponse(Study study, Boolean bookmark, Long headCount){
        this.studyId = study.getId();
        this.title = study.getTitle();
        this.description = study.getDescription();
        this.appliedCompany = study.getAppliedCompany().getName();
        this.appliedJob = study.getAppliedJob();
        this.capacity = study.getCapacity();
        this.createdAt = study.getCreatedAt();
        this.deadline = study.getDeadline();
        this.recruitment = study.getIsRecruit();
        this.leader = new Leader(study.getLeader());
        this.careerLevel = study.getCareerLevel();
        List<StudyTag> tags = study.getStudyTags();
        for (StudyTag tag : tags) {
            this.tags.add(tag.getTag().getTagName());
        }
        this.bookmark = bookmark;
        this.headCount = headCount;
    }
}
