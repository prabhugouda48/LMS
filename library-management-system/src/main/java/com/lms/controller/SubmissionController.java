package com.lms.controller;

import com.lms.model.Submission;
import com.lms.service.SubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/submissions")
public class SubmissionController {

    @Autowired
    private SubmissionService submissionService;

    @PostMapping("/assignments/{assignmentId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Submission> submitAssignment(
            @PathVariable Long assignmentId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(required = false) String comments) {
        return ResponseEntity.ok(submissionService.submitAssignment(assignmentId, file, comments));
    }

    @GetMapping("/assignments/{assignmentId}")
    @PreAuthorize("hasRole('INSTRUCTOR') and @assignmentService.isInstructorOfAssignment(#assignmentId, principal.id)")
    public List<Submission> getSubmissionsByAssignment(@PathVariable Long assignmentId) {
        return submissionService.getSubmissionsByAssignment(assignmentId);
    }

    @GetMapping("/{id}/file")
    @PreAuthorize("@submissionService.canAccessSubmission(#id, principal.id)")
    public ResponseEntity<Resource> downloadSubmissionFile(@PathVariable Long id) {
        Resource resource = submissionService.getSubmissionFile(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @GetMapping("/students/{studentId}")
    @PreAuthorize("#studentId == principal.id or hasRole('INSTRUCTOR')")
    public List<Submission> getSubmissionsByStudent(@PathVariable Long studentId) {
        return submissionService.getSubmissionsByStudent(studentId);
    }

    @GetMapping("/{id}")
    @PreAuthorize("@submissionService.canAccessSubmission(#id, principal.id)")
    public ResponseEntity<Submission> getSubmissionById(@PathVariable Long id) {
        return ResponseEntity.ok(submissionService.getSubmissionById(id));
    }

    @PutMapping("/{id}/grade")
    @PreAuthorize("hasRole('INSTRUCTOR') and @assignmentService.isInstructorOfSubmission(#id, principal.id)")
    public ResponseEntity<Submission> gradeSubmission(
            @PathVariable Long id,
            @RequestParam Double score,
            @RequestParam(required = false) String feedback) {
        return ResponseEntity.ok(submissionService.gradeSubmission(id, score, feedback));
    }

    @GetMapping("/{id}/download")
    @PreAuthorize("@submissionService.canAccessSubmission(#id, principal.id)")
    public ResponseEntity<byte[]> downloadSubmission(@PathVariable Long id) {
        return submissionService.downloadSubmission(id);
    }
}
