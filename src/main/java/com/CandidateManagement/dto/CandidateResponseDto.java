package com.CandidateManagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CandidateResponseDto {

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String skills;
    private String source;
    private String location;

    // Resume details
    private String resumeFileName;
    private String resumeFileType;
    private String resumeDownloadUrl;
}
