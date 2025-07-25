package com.lms.repository;

import com.lms.model.User;
import com.lms.model.User.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    @Query("SELECT u FROM User u WHERE u.role = :role")
    List<User> findAllByRole(@Param("role") UserRole role);
    
    @Query("SELECT u FROM User u JOIN u.enrollments e WHERE e.course.id = :courseId")
    List<User> findAllByCourseId(@Param("courseId") Long courseId);
    
    @Query("SELECT u FROM User u WHERE u.role = 'INSTRUCTOR' AND " +
           "(LOWER(u.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<User> searchInstructors(@Param("searchTerm") String searchTerm);
    
    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.id = :userId AND u.role = 'INSTRUCTOR'")
    boolean isInstructor(@Param("userId") Long userId);
}
