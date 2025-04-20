// LeaveApplicationService.java
package com.university.attendance.service;

import com.university.attendance.model.Course;
import com.university.attendance.model.Faculty;
import com.university.attendance.model.LeaveApplication;
import com.university.attendance.model.Student;
import com.university.attendance.repository.CourseRepository;
import com.university.attendance.repository.FacultyRepository;
import com.university.attendance.repository.LeaveApplicationRepository;
import com.university.attendance.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class LeaveApplicationService {

    private final LeaveApplicationRepository leaveApplicationRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final FacultyRepository facultyRepository;
    private final NotificationService notificationService;

    @Autowired
    public LeaveApplicationService(
            LeaveApplicationRepository leaveApplicationRepository,
            StudentRepository studentRepository,
            CourseRepository courseRepository,
            FacultyRepository facultyRepository,
            NotificationService notificationService) {
        this.leaveApplicationRepository = leaveApplicationRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.facultyRepository = facultyRepository;
        this.notificationService = notificationService;
    }

    @Transactional
    public LeaveApplication submitLeaveApplication(LeaveApplication leaveApplication) {
        Student student = studentRepository.findById(leaveApplication.getStudent().getId())
                .orElseThrow(() -> new RuntimeException("Student not found"));
        
        // If course-specific leave, verify the course
        if (leaveApplication.getCourse() != null) {
            Course course = courseRepository.findById(leaveApplication.getCourse().getId())
                    .orElseThrow(() -> new RuntimeException("Course not found"));
            
            // Check if student is enrolled in this course
            if (!student.getCourses().contains(course)) {
                throw new RuntimeException("Student is not enrolled in this course");
            }
        }
        
        // Ensure dates are valid
        if (leaveApplication.getFromDate().after(leaveApplication.getToDate())) {
            throw new RuntimeException("From date cannot be after to date");
        }
        
        LeaveApplication savedApplication = leaveApplicationRepository.save(leaveApplication);
        
        // Notify faculty if course-specific
        if (savedApplication.getCourse() != null) {
            Faculty faculty = savedApplication.getCourse().getFaculty();
            String title = "New Leave Application";
            String message = "Student " + student.getFullName() + " has applied for leave from " +
                            savedApplication.getFromDate() + " to " + savedApplication.getToDate();
            
            notificationService.createNotification(faculty, title, message, "LEAVE");
        }
        
        return savedApplication;
    }
    
    @Transactional
    public LeaveApplication processLeaveApplication(Long applicationId, String status, String comments, Long facultyId) {
        LeaveApplication application = leaveApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Leave application not found"));
        
        Faculty faculty = facultyRepository.findById(facultyId)
                .orElseThrow(() -> new RuntimeException("Faculty not found"));
        
        application.setStatus(status);
        application.setComments(comments);
        application.setReviewedBy(faculty);
        application.setReviewedAt(new Date());
        
        LeaveApplication updatedApplication = leaveApplicationRepository.save(application);
        
        // Notify student about the application status
        String title = "Leave Application " + status;
        String message = "Your leave application from " + application.getFromDate() + 
                         " to " + application.getToDate() + " has been " + status.toLowerCase();
        
        if (comments != null && !comments.trim().isEmpty()) {
            message += ". Comments: " + comments;
        }
        
        notificationService.createNotification(application.getStudent(), title, message, "LEAVE");
        
        return updatedApplication;
    }
    
    public List<LeaveApplication> getLeaveApplicationsByStudent(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        
        return leaveApplicationRepository.findByStudent(student);
    }
    
    public List<LeaveApplication> getLeaveApplicationsByCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        
        return leaveApplicationRepository.findByCourse(course);
    }
    
    public List<LeaveApplication> getPendingLeaveApplications() {
        return leaveApplicationRepository.findByStatus("PENDING");
    }
    
    public List<LeaveApplication> getCurrentLeaves() {
        Date now = new Date();
        return leaveApplicationRepository.findByFromDateLessThanEqualAndToDateGreaterThanEqual(now, now);
    }
}