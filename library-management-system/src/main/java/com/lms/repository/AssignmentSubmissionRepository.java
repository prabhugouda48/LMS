package com.lms.repository;

import com.lms.model.AssignmentSubmission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AssignmentSubmissionRepository extends JpaRepository<AssignmentSubmission, Long> {
    
    Optional<AssignmentSubmission> findByAssignmentIdAndUserId(Long assignmentId, Long userId);
    
    List<AssignmentSubmission> findByAssignmentId(Long assignmentId);
    
    @Query("SELECT s FROM AssignmentSubmission s WHERE s.assignment.id = :assignmentId " +
           "ORDER BY s.submissionDate")
    Page<AssignmentSubmission> findByAssignmentIdPaginated(@Param("assignmentId") Long assignmentId, 
                                                          Pageable pageable);
    
    @Query("SELECT s FROM AssignmentSubmission s WHERE s.user.id = :userId AND s.assignment.course.id = :courseId")
    List<AssignmentSubmission> findByUserIdAndCourseId(@Param("userId") Long userId, 
                                                      @Param("courseId") Long courseId);
    
    @Query("SELECT s FROM AssignmentSubmission s WHERE s.submissionStatus = 'SUBMITTED' " +
           "AND s.assignment.course.instructor.id = :instructorId")
    List<AssignmentSubmission> findPendingGradingByInstructor(@Param("instructorId") Long instructorId);
    
    @Query("SELECT COUNT(s) FROM AssignmentSubmission s WHERE s.assignment.id = :assignmentId " +
           "AND s.submissionStatus = :status")
    long countByAssignmentIdAndStatus(@Param("assignmentId") Long assignmentId, 
                                    @Param("status") String status);
    
    @Query("SELECT AVG(s.score) FROM AssignmentSubmission s " +
           "WHERE s.user.id = :userId AND s.assignment.course.id = :courseId AND s.score IS NOT NULL")
    Double getAverageScoreForCourse(@Param("userId") Long userId, @Param("courseId") Long courseId);
    
    boolean existsByAssignmentIdAndUserId(Long assignmentId, Long userId);
}
