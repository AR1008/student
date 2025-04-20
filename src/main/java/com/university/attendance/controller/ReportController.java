// ReportController.java
package com.university.attendance.controller;

import com.university.attendance.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<Map<String, Object>> getStudentReport(@PathVariable Long studentId) {
        Map<String, Object> report = reportService.generateStudentAttendanceReport(studentId);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<Map<String, Object>> getCourseReport(@PathVariable Long courseId) {
        Map<String, Object> report = reportService.generateCourseAttendanceReport(courseId);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/department/{department}")
    public ResponseEntity<Map<String, Object>> getDepartmentReport(@PathVariable String department) {
        Map<String, Object> report = reportService.generateDepartmentReport(department);
        return ResponseEntity.ok(report);
    }
}
