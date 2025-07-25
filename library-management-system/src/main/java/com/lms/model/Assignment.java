package com.lms.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "assignments")
public class Assignment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(length = 2000)
    private String description;
    
    @Column(name = "max_score")
    private Double maxScore;
    
    @Column(name = "due_date", nullable = false)
    private LocalDateTime dueDate;
    
    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    @ToString.Exclude
    private Course course;
    
    @Column(name = "submission_type")
    @Enumerated(EnumType.STRING)
    private SubmissionType submissionType;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @OneToMany(mappedBy = "assignment", cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<AssignmentSubmission> submissions = new HashSet<>();
    
    public enum SubmissionType {
        FILE,
        TEXT,
        LINK
    }
    
    @PrePersist
    protected void onCreate() {
        creationDate = LocalDateTime.now();
        if (isActive == null) {
            isActive = true;
        }
        if (submissionType == null) {
            submissionType = SubmissionType.TEXT;
        }
    }
}
