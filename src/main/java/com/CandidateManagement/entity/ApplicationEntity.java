package com.CandidateManagement.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "candidate_id", nullable = false)
    private CandidateEntity candidate;

    private Long jobId;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ApplicationStatus status; 

    private String interviewDetails;   
    
    private String offerLetterUrl;
}
