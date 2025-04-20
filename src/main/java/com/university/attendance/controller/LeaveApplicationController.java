package com.university.attendance.controller;

import com.university.attendance.model.Course;
import com.university.attendance.model.Faculty;
import com.university.attendance.model.LeaveApplication;
import com.university.attendance.model.Student;
import com.university.attendance.service.CourseService;
import com.university.attendance.service.FacultyService;
import com.university.attendance.service.LeaveApplicationService;
import com.university.attendance.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/leave-applications")
public class LeaveApplicationController {

    private final LeaveApplicationService leaveApplicationService;
    private final StudentService studentService;
    private final CourseService courseService;
    private final FacultyService facultyService;

    @Autowired
    public LeaveApplicationController(
            LeaveApplicationService leaveApplicationService,
            StudentService studentService,
            CourseService courseService,
            FacultyService facultyService) {
        this.leaveApplicationService = leaveApplicationService;
        this.studentService = studentService;
        this.courseService = courseService;
        this.facultyService = facultyService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<LeaveApplication> getLeaveApplicationById(@PathVariable Long id) {
        Optional<LeaveApplication> application = Optional.ofNullable(leaveApplicationService.getLeaveApplicationsByStudent(id)
                .stream()
                .findFirst()
                .orElse(null));
        
        return application.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<LeaveApplication>> getLeaveApplicationsByStudent(@PathVariable Long studentId) {
        List<LeaveApplication> applications = leaveApplicationService.getLeaveApplicationsByStudent(studentId);
        return ResponseEntity.ok(applications);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<LeaveApplication>> getLeaveApplicationsByCourse(@PathVariable Long courseId) {
        List<LeaveApplication> applications = leaveApplicationService.getLeaveApplicationsByCourse(courseId);
        return ResponseEntity.ok(applications);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<LeaveApplication>> getPendingLeaveApplications() {
        List<LeaveApplication> applications = leaveApplicationService.getPendingLeaveApplications();
        return ResponseEntity.ok(applications);
    }

    @GetMapping("/current")
    public ResponseEntity<List<LeaveApplication>> getCurrentLeaves() {
        List<LeaveApplication> applications = leaveApplicationService.getCurrentLeaves();
        return ResponseEntity.ok(applications);
    }

    @PostMapping
    public ResponseEntity<LeaveApplication> submitLeaveApplication(
            @RequestParam Long studentId,
            @RequestParam(required = false) Long courseId,
            @RequestParam String leaveType,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate,
            @RequestParam String reason,
            @RequestParam(required = false) String documentPath) {
        
        // Validate student
        Optional<Student> studentOpt = studentService.getStudentById(studentId);
        if (!studentOpt.isPresent()) {
            return ResponseEntity.badRequest().build();
        }
        
        // Create leave application
        LeaveApplication application = new LeaveApplication();
        application.setStudent(studentOpt.get());
        
        // Set course if provided
        if (courseId != null) {
            Optional<Course> courseOpt = courseService.getCourseById(courseId);
            courseOpt.ifPresent(application::setCourse);
        }
        
        application.setLeaveType(leaveType);
        application.setFromDate(fromDate);
        application.setToDate(toDate);
        application.setReason(reason);
        
        if (documentPath != null && !documentPath.isEmpty()) {
            application.setDocumentPath(documentPath);
        }
        
        LeaveApplication submittedApplication = leaveApplicationService.submitLeaveApplication(application);
        return ResponseEntity.ok(submittedApplication);
    }

    @PutMapping("/{id}/process")
    public ResponseEntity<LeaveApplication> processLeaveApplication(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam(required = false) String comments,
            @RequestParam Long facultyId) {
        
        // Validate faculty
        Optional<Faculty> facultyOpt = facultyService.getFacultyById(facultyId);
        if (!facultyOpt.isPresent()) {
            return ResponseEntity.badRequest().build();
        }
        
        LeaveApplication processedApplication = leaveApplicationService.processLeaveApplication(id, status, comments, facultyId);
        return ResponseEntity.ok(processedApplication);
    }
}