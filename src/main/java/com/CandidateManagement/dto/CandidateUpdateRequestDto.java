package com.CandidateManagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CandidateUpdateRequestDto {
	
    private String phone;
    private String skills;
    private String password;
    private String source;
    private String location;
	

}
