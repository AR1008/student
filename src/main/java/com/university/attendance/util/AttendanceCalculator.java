// AttendanceCalculator.java
package com.university.attendance.util;

import com.university.attendance.model.AttendanceRecord;
import com.university.attendance.model.Course;
import com.university.attendance.model.LeaveApplication;
import com.university.attendance.model.Student;
import com.university.attendance.repository.AttendanceRecordRepository;
import com.university.attendance.repository.LeaveApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class to perform various attendance calculations.
 */
@Component
public class AttendanceCalculator {

    private final AttendanceRecordRepository attendanceRecordRepository;
    private final LeaveApplicationRepository leaveApplicationRepository;

    @Autowired
    public AttendanceCalculator(
            AttendanceRecordRepository attendanceRecordRepository,
            LeaveApplicationRepository leaveApplicationRepository) {
        this.attendanceRecordRepository = attendanceRecordRepository;
        this.leaveApplicationRepository = leaveApplicationRepository;
    }

    /**
     * Calculates the attendance percentage for a student in a course.
     * 
     * @param studentId The student ID
     * @param courseId The course ID
     * @return Attendance percentage
     */
    public double calculateAttendancePercentage(Long studentId, Long courseId) {
        List<AttendanceRecord> records = attendanceRecordRepository.findByStudentIdAndCourseId(studentId, courseId);
        
        if (records.isEmpty()) {
            return 0;
        }
        
        long totalSessions = records.size();
        long presentSessions = records.stream()
                .filter(r -> r.getStatus().equals("PRESENT") || r.getStatus().equals("EXCUSED"))
                .count();
        
        return (presentSessions * 100.0) / totalSessions;
    }
    
    /**
     * Checks if a student has adequate attendance for a course.
     * 
     * @param student The student
     * @param course The course
     * @return true if attendance is adequate, false otherwise
     */
    public boolean hasAdequateAttendance(Student student, Course course) {
        double attendancePercentage = calculateAttendancePercentage(student.getId(), course.getId());
        return attendancePercentage >= course.getMinAttendancePercentage();
    }
    
    /**
     * Generates attendance statistics for a course.
     * 
     * @param courseId The course ID
     * @return Map containing attendance statistics
     */
    public Map<String, Object> generateCourseAttendanceStats(Long courseId) {
        Map<String, Object> stats = new HashMap<>();
        
        // This would be more complex in a real application
        // For this demo, we'll return some basic statistics
        
        return stats;
    }
    
    /**
     * Counts approved leave days for a student.
     * 
     * @param studentId The student ID
     * @param fromDate Start date
     * @param toDate End date
     * @return Number of approved leave days
     */
    public int countApprovedLeaveDays(Long studentId, Date fromDate, Date toDate) {
        // This would be more complex in a real application
        // For this demo, we'll return a placeholder value
        return 0;
    }
}