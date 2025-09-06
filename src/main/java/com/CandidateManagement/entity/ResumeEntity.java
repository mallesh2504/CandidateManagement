package com.CandidateManagement.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResumeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String fileName;

	private String fileType;

	@Lob // store large binary data
	@JsonIgnore
	private byte[] fileData;
//	@Column(columnDefinition = "LONGBLOB") // MySQL; if Postgres remove columnDefinition


	@OneToOne
	@JoinColumn(name = "candidate_id", nullable = false, unique = true)
	@JsonBackReference
	private CandidateEntity candidate;
}
