package com.lms.repository;

import com.lms.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
    
    List<Course> findByInstructorId(Long instructorId);
    
    Optional<Course> findByCode(String code);
    
    boolean existsByCode(String code);
    
    @Query("SELECT c FROM Course c WHERE c.instructor.id = :instructorId")
    Page<Course> findCoursesByInstructor(@Param("instructorId") Long instructorId, Pageable pageable);
    
    @Query("SELECT c FROM Course c WHERE " +
           "LOWER(c.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.code) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Course> searchCourses(@Param("searchTerm") String searchTerm);
    
    @Query("SELECT c FROM Course c JOIN c.enrollments e WHERE e.user.id = :userId")
    List<Course> findEnrolledCourses(@Param("userId") Long userId);
    
    @Query("SELECT COUNT(e) FROM Course c JOIN c.enrollments e WHERE c.id = :courseId")
    long getEnrollmentCount(@Param("courseId") Long courseId);
}
