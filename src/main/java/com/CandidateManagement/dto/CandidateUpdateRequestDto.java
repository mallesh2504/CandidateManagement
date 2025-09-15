package com.CandidateManagement.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CandidateUpdateRequestDto {
    private String phone;
    private String password;
    private List<String> skills;     
    private String source;
    private String location;
}
