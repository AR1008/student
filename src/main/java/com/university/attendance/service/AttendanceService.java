// AttendanceService.java
package com.university.attendance.service;

import com.university.attendance.model.*;
import com.university.attendance.repository.AttendanceRecordRepository;
import com.university.attendance.repository.AttendanceRepository;
import com.university.attendance.repository.CourseRepository;
import com.university.attendance.repository.StudentRepository;
import com.university.attendance.util.BiometricVerifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final AttendanceRecordRepository attendanceRecordRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final NotificationService notificationService;
    private final BiometricVerifier biometricVerifier;

    @Autowired
    public AttendanceService(
            AttendanceRepository attendanceRepository,
            AttendanceRecordRepository attendanceRecordRepository,
            StudentRepository studentRepository,
            CourseRepository courseRepository,
            NotificationService notificationService,
            BiometricVerifier biometricVerifier) {
        this.attendanceRepository = attendanceRepository;
        this.attendanceRecordRepository = attendanceRecordRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.notificationService = notificationService;
        this.biometricVerifier = biometricVerifier;
    }

    @Transactional
    public Attendance createAttendanceSession(Attendance attendance) {
        attendance.setStatus("PENDING");
        Attendance savedAttendance = attendanceRepository.save(attendance);
        
        // Create attendance records for all students in this course
        Course course = attendance.getCourse();
        List<Student> students = studentRepository.findByCourseId(course.getId());
        
        for (Student student : students) {
            AttendanceRecord record = new AttendanceRecord();
            record.setAttendance(savedAttendance);
            record.setStudent(student);
            record.setStatus("ABSENT"); // Default status
            
            attendanceRecordRepository.save(record);
        }
        
        return savedAttendance;
    }

    @Transactional
    public AttendanceRecord markAttendance(Long attendanceId, Long studentId, String status, String verificationMethod) {
        Attendance attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new RuntimeException("Attendance session not found"));
        
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        
        // Find existing record or create new one
        AttendanceRecord record = attendanceRecordRepository.findByAttendanceAndStudent(attendance, student)
                .stream()
                .findFirst()
                .orElseGet(() -> {
                    AttendanceRecord newRecord = new AttendanceRecord();
                    newRecord.setAttendance(attendance);
                    newRecord.setStudent(student);
                    return newRecord;
                });
        
        record.setStatus(status);
        record.setVerificationMethod(verificationMethod);
        record.setMarkedAt(new Date());
        
        AttendanceRecord savedRecord = attendanceRecordRepository.save(record);
        
        // Send notification to student
        String title = "Attendance Marked for " + attendance.getCourse().getCourseName();
        String message = "Your attendance has been marked as " + status + " for " + 
                         attendance.getCourse().getCourseName() + " on " + attendance.getDate();
        
        notificationService.createNotification(student, title, message, "ATTENDANCE");
        
        return savedRecord;
    }
    
    @Transactional
    public AttendanceRecord studentSelfCheckIn(Long attendanceId, Long studentId, String biometricData) {
        // Verify student identity using biometric data
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        
        boolean verificationSuccessful = biometricVerifier.verifyIdentity(student, biometricData);
        
        if (!verificationSuccessful) {
            throw new RuntimeException("Biometric verification failed");
        }
        
        return markAttendance(attendanceId, studentId, "PRESENT", "BIOMETRIC");
    }
    
    @Transactional
    public void completeAttendanceSession(Long attendanceId) {
        Attendance attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new RuntimeException("Attendance session not found"));
        
        attendance.setStatus("COMPLETED");
        attendanceRepository.save(attendance);
    }
    
    public List<Attendance> getAttendancesByCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        
        return attendanceRepository.findByCourse(course);
    }
    
    public List<AttendanceRecord> getAttendanceRecordsByStudent(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        
        return attendanceRecordRepository.findByStudent(student);
    }
    
    public List<AttendanceRecord> getAttendanceRecordsByStudentAndCourse(Long studentId, Long courseId) {
        return attendanceRecordRepository.findByStudentIdAndCourseId(studentId, courseId);
    }
    
    public double calculateAttendancePercentage(Long studentId, Long courseId) {
        long totalSessions = attendanceRecordRepository.findByStudentIdAndCourseId(studentId, courseId).size();
        
        if (totalSessions == 0) {
            return 0;
        }
        
        long presentSessions = attendanceRecordRepository.countByStudentIdAndCourseIdAndStatus(studentId, courseId, "PRESENT");
        
        return (presentSessions * 100.0) / totalSessions;
    }
    
    public Map<String, Object> generateAttendanceStats(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        
        List<Student> students = studentRepository.findByCourseId(courseId);
        List<Attendance> attendances = attendanceRepository.findByCourse(course);
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalSessions", attendances.size());
        
        List<Map<String, Object>> studentStats = new ArrayList<>();
        
        for (Student student : students) {
            Map<String, Object> studentStat = new HashMap<>();
            studentStat.put("studentId", student.getId());
            studentStat.put("rollNumber", student.getRollNumber());
            studentStat.put("name", student.getFullName());
            
            double attendancePercentage = calculateAttendancePercentage(student.getId(), courseId);
            studentStat.put("attendancePercentage", attendancePercentage);
            
            if (attendancePercentage < course.getMinAttendancePercentage()) {
                studentStat.put("status", "BELOW_MINIMUM");
            } else {
                studentStat.put("status", "ADEQUATE");
            }
            
            studentStats.add(studentStat);
        }
        
        stats.put("students", studentStats);
        
        return stats;
    }
}