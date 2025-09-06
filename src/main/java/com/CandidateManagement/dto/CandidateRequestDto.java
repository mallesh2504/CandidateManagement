package com.CandidateManagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CandidateRequestDto {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String password;
    private String skills;
    private String source;
    private String location;
}
