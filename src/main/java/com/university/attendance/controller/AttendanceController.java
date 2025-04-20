package com.university.attendance.controller;

import com.university.attendance.model.AttendanceRecord;
import com.university.attendance.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @Autowired
    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<AttendanceRecord>> getStudentAttendance(@PathVariable Long studentId) {
        List<AttendanceRecord> records = attendanceService.getAttendanceRecordsByStudent(studentId);
        return ResponseEntity.ok(records);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<AttendanceRecord>> getCourseAttendance(@PathVariable Long courseId,
                                                                   @RequestParam(required = false) Long studentId) {
        
        List<AttendanceRecord> records;
        
        if (studentId != null) {
            records = attendanceService.getAttendanceRecordsByStudentAndCourse(studentId, courseId);
        } else {
            // This would need additional implementation to return all records for a course
            // For simplicity, we'll return an empty list here
            records = List.of();
        }
        
        return ResponseEntity.ok(records);
    }

    @GetMapping("/percentage/{studentId}/{courseId}")
    public ResponseEntity<Map<String, Object>> getAttendancePercentage(@PathVariable Long studentId,
                                                                     @PathVariable Long courseId) {
        
        double percentage = attendanceService.calculateAttendancePercentage(studentId, courseId);
        
        return ResponseEntity.ok(Map.of(
            "studentId", studentId,
            "courseId", courseId,
            "attendancePercentage", percentage
        ));
    }
}