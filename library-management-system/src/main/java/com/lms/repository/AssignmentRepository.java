package com.lms.repository;

import com.lms.model.Assignment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    
    List<Assignment> findByCourseId(Long courseId);
    
    @Query("SELECT a FROM Assignment a WHERE a.course.id = :courseId AND a.isActive = true")
    List<Assignment> findActiveByCourseId(@Param("courseId") Long courseId);
    
    @Query("SELECT a FROM Assignment a WHERE a.course.id = :courseId AND a.dueDate > :now")
    List<Assignment> findUpcomingByCourseId(@Param("courseId") Long courseId, 
                                           @Param("now") LocalDateTime now);
    
    @Query("SELECT a FROM Assignment a WHERE a.course.id IN " +
           "(SELECT e.course.id FROM Enrollment e WHERE e.user.id = :userId)")
    Page<Assignment> findByStudentId(@Param("userId") Long userId, Pageable pageable);
    
    @Query("SELECT a FROM Assignment a WHERE a.course.instructor.id = :instructorId")
    List<Assignment> findByInstructorId(@Param("instructorId") Long instructorId);
    
    @Query("SELECT COUNT(s) FROM AssignmentSubmission s WHERE s.assignment.id = :assignmentId")
    long getSubmissionCount(@Param("assignmentId") Long assignmentId);
    
    @Query("SELECT AVG(s.score) FROM AssignmentSubmission s " +
           "WHERE s.assignment.id = :assignmentId AND s.score IS NOT NULL")
    Double getAverageScore(@Param("assignmentId") Long assignmentId);
    
    @Query("SELECT a FROM Assignment a WHERE a.dueDate < :now AND " +
           "a.id NOT IN (SELECT s.assignment.id FROM AssignmentSubmission s WHERE s.user.id = :userId)")
    List<Assignment> findOverdueAssignments(@Param("userId") Long userId, 
                                          @Param("now") LocalDateTime now);
}
