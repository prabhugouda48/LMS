package com.lms.controller;

import com.lms.model.Course;
import com.lms.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PostMapping
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<Course> createCourse(@Valid @RequestBody Course course) {
        return ResponseEntity.ok(courseService.createCourse(course));
    }

    @GetMapping
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.getCourseById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('INSTRUCTOR') and @courseService.isInstructor(#id, principal.id)")
    public ResponseEntity<Course> updateCourse(@PathVariable Long id, @Valid @RequestBody Course course) {
        return ResponseEntity.ok(courseService.updateCourse(id, course));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('INSTRUCTOR') and @courseService.isInstructor(#id, principal.id)")
    public ResponseEntity<?> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{courseId}/enroll/{userId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> enrollStudent(@PathVariable Long courseId, @PathVariable Long userId) {
        courseService.enrollStudent(courseId, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{courseId}/unenroll/{userId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> unenrollStudent(@PathVariable Long courseId, @PathVariable Long userId) {
        courseService.unenrollStudent(courseId, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/instructor/{instructorId}")
    public List<Course> getCoursesByInstructor(@PathVariable Long instructorId) {
        return courseService.getCoursesByInstructor(instructorId);
    }

    @GetMapping("/student/{studentId}")
    public List<Course> getEnrolledCourses(@PathVariable Long studentId) {
        return courseService.getEnrolledCourses(studentId);
    }
}
