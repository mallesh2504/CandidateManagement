package com.CandidateManagement.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(CandidateAlreadyExistException.class)
	public ResponseEntity<Map<String, Object>> handleCandidateAlreadyExistException(CandidateAlreadyExistException ex,
			WebRequest request) {
		Map<String, Object> error = new HashMap<>();
		error.put("error", "Candidate Already Exist");
		error.put("message", ex.getMessage());
		return new ResponseEntity<>(error, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(CandidateNotFoundException.class)
	public ResponseEntity<Map<String, Object>> handleCandidateNotFoundException(CandidateNotFoundException ex,
			WebRequest request) {
		Map<String, Object> error = new HashMap<>();
		error.put("error", "Candidate Not Found");
		error.put("message", ex.getMessage());
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(AlreadyAppliedException.class)
	public ResponseEntity<Map<String, Object>> handleAlreadyAppliedException(AlreadyAppliedException ex,
			WebRequest request) {
		Map<String, Object> error = new HashMap<>();
		error.put("error", "Candidate Your Already Applied For This Job");
		error.put("message", ex.getMessage());
		return new ResponseEntity<>(error, HttpStatus.NOT_ACCEPTABLE);
	}

	@ExceptionHandler(ApplicationNotFoundException.class)
	public ResponseEntity<Map<String, Object>> handleApplicationNotFoundException(ApplicationNotFoundException ex,
			WebRequest request) {
		Map<String, Object> error = new HashMap<>();
		error.put("error", "Entered Application Id is Not Found");
		error.put("message", ex.getMessage());
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(InvalidCredentialsException.class)
	public ResponseEntity<Map<String, Object>> handleInvalidCredentialsException(InvalidCredentialsException ex,
			WebRequest request) {
		Map<String, Object> error = new HashMap<>();
		error.put("error", "Entered Details Are Invalid");
		error.put("message", ex.getMessage());
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(InterviewNotFoundException.class)
	public ResponseEntity<Map<String, Object>> handleInterviewNotFoundException(InterviewNotFoundException ex,
			WebRequest request) {
		Map<String, Object> error = new HashMap<>();
		error.put("error", "Interview Not Found With This Id ");
		error.put("message", ex.getMessage());
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}

}
