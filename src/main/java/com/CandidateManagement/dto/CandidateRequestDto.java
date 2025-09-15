package com.CandidateManagement.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CandidateRequestDto {

	@NotNull(message = "First name is required")
	@NotBlank(message = "First name cannot be blank")
	private String firstName;

	@NotNull(message = "Last name is required")
	@NotBlank(message = "Last name cannot be blank")
	private String lastName;

	@NotNull(message = "Email is required")
	@NotBlank(message = "Email cannot be blank")
	private String email;

	@NotNull(message = "Phone number is required")
	@NotBlank(message = "Phone number cannot be blank")
	private String phone;

	@NotNull(message = "Password is required")
	@NotBlank(message = "Password cannot be blank")
	private String password;

	@NotEmpty(message = "Skills list cannot be empty")
	private List<String> skills;

	private String source;
	@NotNull(message = "Location is required")
	@NotBlank(message = "Location cannot be blank")

	private String location;
}
