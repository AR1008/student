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
import com.university.attendance.model.Faculty;
import com.university.attendance.model.LeaveApplication;
import com.university.attendance.service.CourseService;
import com.university.attendance.service.FacultyService;
import com.university.attendance.service.LeaveApplicationService;

@Controller
@RequestMapping("/faculty")
public class FacultyController {
    
    @Autowired
    private FacultyService facultyService;
    
    @Autowired
    private CourseService courseService;
    
    @Autowired
    private LeaveApplicationService leaveApplicationService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        try {
            // For demo, use fixed faculty ID
            Long facultyId = 2L;
            
            // Get faculty and courses
            Optional<Faculty> facultyOpt = facultyService.getFacultyById(facultyId);
            if (!facultyOpt.isPresent()) {
                model.addAttribute("error", "Faculty not found");
                return "error";
            }
            
            Faculty faculty = facultyOpt.get();
            List<Course> courses = courseService.getCoursesByFaculty(facultyId);
            
            // Get pending leave applications
            List<LeaveApplication> pendingApplications = new ArrayList<>();
            
            // Add mock data for demo
            model.addAttribute("faculty", faculty);
            model.addAttribute("courses", courses);
            model.addAttribute("pendingApplications", pendingApplications);
            model.addAttribute("unreadNotifications", 0);
            
            return "faculty/dashboard";
        } catch (Exception e) {
            model.addAttribute("error", "Error loading dashboard: " + e.getMessage());
            return "error";
        }
    }
    
    @GetMapping("/mark-attendance")
    public String markAttendance(@RequestParam(required = false) Long courseId, Model model) {
        // TODO: Implement mark attendance logic
        return "faculty/mark-attendance";
    }
    
    @GetMapping("/process-leave")
    public String processLeave(Model model) {
        // TODO: Implement process leave logic
        return "faculty/process-leave";
    }
    
    @GetMapping("/reports")
    public String reports(@RequestParam(required = false) Long courseId, Model model) {
        // TODO: Implement reports logic
        return "faculty/reports";
    }
}