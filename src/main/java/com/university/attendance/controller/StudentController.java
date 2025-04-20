package com.university.attendance.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.university.attendance.model.Course;
import com.university.attendance.model.Student;
import com.university.attendance.service.StudentService;

@Controller
@RequestMapping("/student")
public class StudentController {
    
    @Autowired
    private StudentService studentService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        try {
            // For demo, use fixed student ID
            Long studentId = 4L;
            
            // Get student
            Optional<Student> studentOpt = studentService.getStudentById(studentId);
            if (!studentOpt.isPresent()) {
                model.addAttribute("error", "Student not found");
                return "error";
            }
            
            Student student = studentOpt.get();
            
            // Create mock attendance report for demo
            Map<String, Object> mockReport = createMockAttendanceReport(student);
            
            model.addAttribute("student", student);
            model.addAttribute("attendanceReport", mockReport);
            model.addAttribute("unreadNotifications", 0);
            
            return "student/dashboard";
        } catch (Exception e) {
            model.addAttribute("error", "Error loading dashboard: " + e.getMessage());
            return "error";
        }
    }
    
    @GetMapping("/view-attendance")
    public String viewAttendance(@RequestParam(required = false) Long courseId, Model model) {
        // TODO: Implement view attendance logic
        return "student/view-attendance";
    }
    
    @GetMapping("/mark-attendance")
    public String markAttendance(Model model) {
        // TODO: Implement mark attendance logic
        return "student/mark-attendance";
    }
    
    @GetMapping("/submit-leave")
    public String submitLeave(Model model) {
        // TODO: Implement submit leave logic
        return "student/submit-leave";
    }
    
    // Helper method to create mock attendance report for demo
    private Map<String, Object> createMockAttendanceReport(Student student) {
        Map<String, Object> report = new HashMap<>();
        
        report.put("studentId", student.getId());
        report.put("rollNumber", student.getRollNumber());
        report.put("name", student.getFullName());
        report.put("department", student.getDepartment());
        report.put("year", student.getYear());
        report.put("semester", student.getSemester());
        report.put("overallAttendancePercentage", 85.5);
        
        List<Map<String, Object>> courseAttendance = new ArrayList<>();
        
        // Add mock course attendance data
        for (Course course : student.getCourses()) {
            Map<String, Object> courseData = new HashMap<>();
            courseData.put("courseId", course.getId());
            courseData.put("courseCode", course.getCourseCode());
            courseData.put("courseName", course.getCourseName());
            courseData.put("attendancePercentage", 85.5);
            courseData.put("status", "ADEQUATE");
            
            courseAttendance.add(courseData);
        }
        
        report.put("courses", courseAttendance);
        
        // Add mock leave applications
        List<Map<String, Object>> leaveApplications = new ArrayList<>();
        Map<String, Object> leave = new HashMap<>();
        leave.put("id", 1L);
        leave.put("type", "Medical");
        leave.put("fromDate", new Date());
        leave.put("toDate", new Date());
        leave.put("status", "PENDING");
        leave.put("courseName", "All Courses");
        
        leaveApplications.add(leave);
        report.put("leaveApplications", leaveApplications);
        
        return report;
    }
}