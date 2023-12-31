package com.ssafy.interviewstudy.dto.study;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ssafy.interviewstudy.domain.study.StudyRequestFile;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class RequestFile {
    private String name;
    private Integer fileId;
    private String fileType;
    private byte[] fileData;
    public RequestFile(StudyRequestFile file){
        this.name = file.getOriginalFileName();
        this.fileId = file.getId();
        this.fileType = file.getFileType();
    }
}
