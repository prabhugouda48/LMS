package com.lms.controller;

import com.lms.model.Assignment;
import com.lms.service.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {

    @Autowired
    private AssignmentService assignmentService;

    @PostMapping("/courses/{courseId}")
    @PreAuthorize("hasRole('INSTRUCTOR') and @courseService.isInstructor(#courseId, principal.id)")
    public ResponseEntity<Assignment> createAssignment(@PathVariable Long courseId,
                                                     @Valid @RequestBody Assignment assignment) {
        return ResponseEntity.ok(assignmentService.createAssignment(assignment, courseId));
    }

    @GetMapping("/courses/{courseId}")
    public List<Assignment> getAssignmentsByCourse(@PathVariable Long courseId) {
        return assignmentService.getAssignmentsByCourse(courseId);
    }

    @GetMapping("/courses/{courseId}/upcoming")
    public List<Assignment> getUpcomingAssignments(@PathVariable Long courseId) {
        return assignmentService.getUpcomingAssignments(courseId);
    }

    @GetMapping("/courses/{courseId}/past")
    public List<Assignment> getPastAssignments(@PathVariable Long courseId) {
        return assignmentService.getPastAssignments(courseId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Assignment> getAssignmentById(@PathVariable Long id) {
        return ResponseEntity.ok(assignmentService.getAssignmentById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('INSTRUCTOR') and @assignmentService.isInstructorOfAssignment(#id, principal.id)")
    public ResponseEntity<Assignment> updateAssignment(@PathVariable Long id,
                                                     @Valid @RequestBody Assignment assignment) {
        return ResponseEntity.ok(assignmentService.updateAssignment(id, assignment));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('INSTRUCTOR') and @assignmentService.isInstructorOfAssignment(#id, principal.id)")
    public ResponseEntity<?> deleteAssignment(@PathVariable Long id) {
        assignmentService.deleteAssignment(id);
        return ResponseEntity.ok().build();
    }
}
