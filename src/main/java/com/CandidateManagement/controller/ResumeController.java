package com.CandidateManagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.CandidateManagement.dto.ResumeResponseDto;
import com.CandidateManagement.entity.ResumeEntity;
import com.CandidateManagement.exception.CandidateNotFoundException;
import com.CandidateManagement.exception.ResumeIdNotFoundException;
import com.CandidateManagement.exception.ResumeandCandidateNotMatched;
import com.CandidateManagement.service.ResumeService;

@RestController
@RequestMapping("/resume")
public class ResumeController {

	@Autowired
	private ResumeService resumeService;

	
	@PostMapping("/uploadfile")
	public ResponseEntity<?> uploadResumeFile(@RequestParam("file") MultipartFile file,
			@RequestParam("candidateId") Long candidateId) {
		try {
			ResumeEntity resume = resumeService.uploadResumeFile(file, candidateId);
			return ResponseEntity.ok("Resume uploaded successfully with Resume ID: " + resume.getId()+ "Candidate Id"+candidateId);
		} catch (CandidateNotFoundException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("Upload failed");
		}
	}

	
	@PutMapping("/updatefile/{id}")
	public ResponseEntity<?> updateResumeFile(@PathVariable Long id, @RequestParam("file") MultipartFile file,
			@RequestParam("candidateId") Long candidateId) {
		try {
			ResumeEntity updatedResume = resumeService.updateResumeFile(id, file, candidateId);
			return ResponseEntity.ok("Resume updated successfully with ID: " + updatedResume.getId());
		} 
		catch (ResumeIdNotFoundException | ResumeandCandidateNotMatched e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} 
		catch (Exception e) {
			return ResponseEntity.internalServerError().body("Update failed");
		}
	}

	
	@GetMapping("/download/{id}")
	public ResponseEntity<byte[]> downloadResume(@PathVariable Long id) throws ResumeIdNotFoundException {
		ResumeEntity resume = resumeService.downloadResume(id);

		return ResponseEntity.ok()
				.header("Content-Disposition", "attachment: filename=\"" + resume.getFileName() + "\"")
				.header("Content-Type", resume.getFileType()).body(resume.getFileData());
	}

	
	@GetMapping("/get/{id}/{candidateId}")
	public ResponseEntity<ResumeResponseDto>  getResume(@PathVariable Long id, @PathVariable Long candidateId)
			throws ResumeIdNotFoundException, ResumeandCandidateNotMatched {
		 return resumeService.getResume(id, candidateId);
		 
	}

	
	@DeleteMapping("/delete/{id}/{candidateId}")
	public String deleteResume(@PathVariable Long id, @PathVariable Long candidateId)
			throws ResumeIdNotFoundException, ResumeandCandidateNotMatched {
		return resumeService.deleteResume(id, candidateId);
	}
}
