package com.CandidateManagement.dto;

import java.util.List;
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
	private List<String> skills;
	private String source;
	private String location;
}
