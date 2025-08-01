package com.lms.controller;

import com.lms.dto.DashboardStats;
import com.lms.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public DashboardStats getAdminDashboard() {
        return dashboardService.getAdminDashboardStats();
    }

    @GetMapping("/instructor/{instructorId}")
    @PreAuthorize("hasRole('INSTRUCTOR') and #instructorId == principal.id")
    public DashboardStats getInstructorDashboard(@PathVariable Long instructorId) {
        return dashboardService.getInstructorDashboardStats(instructorId);
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasRole('STUDENT') and #studentId == principal.id")
    public DashboardStats getStudentDashboard(@PathVariable Long studentId) {
        return dashboardService.getStudentDashboardStats(studentId);
    }
}
