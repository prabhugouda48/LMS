package com.lms.model;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "assignment_submissions")
public class AssignmentSubmission {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id", nullable = false)
    private Assignment assignment;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(name = "submission_date", nullable = false)
    private LocalDateTime submissionDate;
    
    @Column(length = 2000)
    private String content;
    
    @Column(name = "file_path")
    private String filePath;
    
    @Column(name = "submission_status")
    private String submissionStatus; // SUBMITTED, LATE, GRADED
    
    private Double score;
    
    @Column(length = 1000)
    private String feedback;
    
    @Column(name = "graded_by")
    private Long gradedById;
    
    @Column(name = "graded_date")
    private LocalDateTime gradedDate;
    
    @PrePersist
    protected void onCreate() {
        submissionDate = LocalDateTime.now();
        if (submissionStatus == null) {
            submissionStatus = "SUBMITTED";
        }
    }
}
