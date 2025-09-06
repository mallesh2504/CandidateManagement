package com.CandidateManagement.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OfferLetterResponseDto {
    private Long applicationId;   
    private String offerLetterUrl; 
}
