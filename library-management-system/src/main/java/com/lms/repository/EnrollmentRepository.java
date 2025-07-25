package com.lms.repository;

import com.lms.model.Enrollment;
import com.lms.model.Enrollment.EnrollmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    
    List<Enrollment> findByUserId(Long userId);
    
    List<Enrollment> findByCourseId(Long courseId);
    
    Optional<Enrollment> findByUserIdAndCourseId(Long userId, Long courseId);
    
    @Query("SELECT e FROM Enrollment e WHERE e.user.id = :userId AND e.status = :status")
    List<Enrollment> findByUserIdAndStatus(@Param("userId") Long userId, 
                                         @Param("status") EnrollmentStatus status);
    
    @Query("SELECT e FROM Enrollment e WHERE e.course.id = :courseId")
    Page<Enrollment> findByCourseIdPaginated(@Param("courseId") Long courseId, 
                                            Pageable pageable);
    
    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.course.id = :courseId AND e.status = :status")
    long countByCourseIdAndStatus(@Param("courseId") Long courseId, 
                                 @Param("status") EnrollmentStatus status);
    
    @Query("SELECT AVG(e.finalGrade) FROM Enrollment e WHERE e.course.id = :courseId AND e.finalGrade IS NOT NULL")
    Double getAverageGradeByCourse(@Param("courseId") Long courseId);
    
    boolean existsByUserIdAndCourseId(Long userId, Long courseId);
}
