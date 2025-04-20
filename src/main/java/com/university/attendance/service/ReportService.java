// ReportService.java
package com.university.attendance.service;

import com.university.attendance.model.*;
import com.university.attendance.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ReportService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final AttendanceRepository attendanceRepository;
    private final AttendanceRecordRepository attendanceRecordRepository;
    private final LeaveApplicationRepository leaveApplicationRepository;

    @Autowired
    public ReportService(
            StudentRepository studentRepository,
            CourseRepository courseRepository,
            AttendanceRepository attendanceRepository,
            AttendanceRecordRepository attendanceRecordRepository,
            LeaveApplicationRepository leaveApplicationRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.attendanceRepository = attendanceRepository;
        this.attendanceRecordRepository = attendanceRecordRepository;
        this.leaveApplicationRepository = leaveApplicationRepository;
    }

    public Map<String, Object> generateStudentAttendanceReport(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        
        Map<String, Object> report = new HashMap<>();
        report.put("studentId", student.getId());
        report.put("rollNumber", student.getRollNumber());
        report.put("name", student.getFullName());
        report.put("department", student.getDepartment());
        report.put("year", student.getYear());
        report.put("semester", student.getSemester());
        
        List<Map<String, Object>> courseAttendance = new ArrayList<>();
        
        for (Course course : student.getCourses()) {
            Map<String, Object> courseData = new HashMap<>();
            courseData.put("courseId", course.getId());
            courseData.put("courseCode", course.getCourseCode());
            courseData.put("courseName", course.getCourseName());
            
            List<AttendanceRecord> records = attendanceRecordRepository.findByStudentIdAndCourseId(studentId, course.getId());
            
            long totalSessions = records.size();
            long presentCount = records.stream().filter(r -> r.getStatus().equals("PRESENT")).count();
            long absentCount = records.stream().filter(r -> r.getStatus().equals("ABSENT")).count();
            long lateCount = records.stream().filter(r -> r.getStatus().equals("LATE")).count();
            long excusedCount = records.stream().filter(r -> r.getStatus().equals("EXCUSED")).count();
            
            double attendancePercentage = totalSessions > 0 ? (presentCount * 100.0) / totalSessions : 0;
            
            courseData.put("totalSessions", totalSessions);
            courseData.put("presentCount", presentCount);
            courseData.put("absentCount", absentCount);
            courseData.put("lateCount", lateCount);
            courseData.put("excusedCount", excusedCount);
            courseData.put("attendancePercentage", attendancePercentage);
            
            if (attendancePercentage < course.getMinAttendancePercentage()) {
                courseData.put("status", "BELOW_MINIMUM");
                courseData.put("minimumRequired", course.getMinAttendancePercentage());
            } else {
                courseData.put("status", "ADEQUATE");
            }
            
            courseAttendance.add(courseData);
        }
        
        report.put("courses", courseAttendance);
        
        // Calculate overall attendance percentage
        double overallAttendancePercentage = courseAttendance.stream()
                .mapToDouble(c -> (double) c.get("attendancePercentage"))
                .average()
                .orElse(0);
        
        report.put("overallAttendancePercentage", overallAttendancePercentage);
        
        // Get leave applications
        List<LeaveApplication> leaveApplications = leaveApplicationRepository.findByStudent(student);
        List<Map<String, Object>> leaves = new ArrayList<>();
        
        for (LeaveApplication leave : leaveApplications) {
            Map<String, Object> leaveData = new HashMap<>();
            leaveData.put("id", leave.getId());
            leaveData.put("fromDate", leave.getFromDate());
            leaveData.put("toDate", leave.getToDate());
            leaveData.put("type", leave.getLeaveType());
            leaveData.put("status", leave.getStatus());
            
            if (leave.getCourse() != null) {
                leaveData.put("courseCode", leave.getCourse().getCourseCode());
                leaveData.put("courseName", leave.getCourse().getCourseName());
            } else {
                leaveData.put("courseCode", null);
                leaveData.put("courseName", null);
            }
            
            leaves.add(leaveData);
        }
        
        report.put("leaveApplications", leaves);
        
        return report;
    }
    
    public Map<String, Object> generateCourseAttendanceReport(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        
        Map<String, Object> report = new HashMap<>();
        report.put("courseId", course.getId());
        report.put("courseCode", course.getCourseCode());
        report.put("courseName", course.getCourseName());
        report.put("department", course.getDepartment());
        report.put("semester", course.getSemester());
        report.put("faculty", course.getFaculty() != null ? course.getFaculty().getFullName() : "Not Assigned");
        
        List<Attendance> attendances = attendanceRepository.findByCourse(course);
        report.put("totalSessions", attendances.size());
        
        List<Student> students = studentRepository.findByCourseId(courseId);
        List<Map<String, Object>> studentAttendance = new ArrayList<>();
        
        for (Student student : students) {
            Map<String, Object> studentData = new HashMap<>();
            studentData.put("studentId", student.getId());
            studentData.put("rollNumber", student.getRollNumber());
            studentData.put("name", student.getFullName());
            
            List<AttendanceRecord> records = attendanceRecordRepository.findByStudentIdAndCourseId(student.getId(), courseId);
            
            long totalSessions = records.size();
            long presentCount = records.stream().filter(r -> r.getStatus().equals("PRESENT")).count();
            long absentCount = records.stream().filter(r -> r.getStatus().equals("ABSENT")).count();
            long lateCount = records.stream().filter(r -> r.getStatus().equals("LATE")).count();
            long excusedCount = records.stream().filter(r -> r.getStatus().equals("EXCUSED")).count();
            
            double attendancePercentage = totalSessions > 0 ? (presentCount * 100.0) / totalSessions : 0;
            
            studentData.put("totalSessions", totalSessions);
            studentData.put("presentCount", presentCount);
            studentData.put("absentCount", absentCount);
            studentData.put("lateCount", lateCount);
            studentData.put("excusedCount", excusedCount);
            studentData.put("attendancePercentage", attendancePercentage);
            
            if (attendancePercentage < course.getMinAttendancePercentage()) {
                studentData.put("status", "BELOW_MINIMUM");
            } else {
                studentData.put("status", "ADEQUATE");
            }
            
            studentAttendance.add(studentData);
        }
        
        report.put("students", studentAttendance);
        
        // Calculate class average attendance
        double averageAttendance = studentAttendance.stream()
                .mapToDouble(s -> (double) s.get("attendancePercentage"))
                .average()
                .orElse(0);
        
        report.put("averageAttendancePercentage", averageAttendance);
        
        // Statistics
        long studentsWithAdequateAttendance = studentAttendance.stream()
                .filter(s -> s.get("status").equals("ADEQUATE"))
                .count();
        
        long studentsWithInadequateAttendance = studentAttendance.stream()
                .filter(s -> s.get("status").equals("BELOW_MINIMUM"))
                .count();
        
        report.put("studentsWithAdequateAttendance", studentsWithAdequateAttendance);
        report.put("studentsWithInadequateAttendance", studentsWithInadequateAttendance);
        report.put("adequateAttendancePercentage", 
                  students.size() > 0 ? (studentsWithAdequateAttendance * 100.0) / students.size() : 0);
        
        return report;
    }
    
    public Map<String, Object> generateDepartmentReport(String department) {
        List<Student> students = studentRepository.findByDepartment(department);
        List<Course> courses = courseRepository.findByDepartment(department);
        
        Map<String, Object> report = new HashMap<>();
        report.put("department", department);
        report.put("totalStudents", students.size());
        report.put("totalCourses", courses.size());
        
        // Calculate overall department attendance
        double departmentAttendance = 0;
        int totalRecords = 0;
        
        for (Student student : students) {
            for (Course course : student.getCourses()) {
                List<AttendanceRecord> records = attendanceRecordRepository.findByStudentIdAndCourseId(student.getId(), course.getId());
                
                long presentCount = records.stream().filter(r -> r.getStatus().equals("PRESENT")).count();
                totalRecords += records.size();
                departmentAttendance += presentCount;
            }
        }
        
        double departmentAttendancePercentage = totalRecords > 0 ? (departmentAttendance * 100.0) / totalRecords : 0;
        report.put("departmentAttendancePercentage", departmentAttendancePercentage);
        
        // Course-wise statistics
        List<Map<String, Object>> courseStats = new ArrayList<>();
        
        for (Course course : courses) {
            Map<String, Object> courseStat = new HashMap<>();
            courseStat.put("courseId", course.getId());
            courseStat.put("courseCode", course.getCourseCode());
            courseStat.put("courseName", course.getCourseName());
            
            List<Student> enrolledStudents = studentRepository.findByCourseId(course.getId());
            courseStat.put("enrolledStudents", enrolledStudents.size());
            
            double courseAttendance = 0;
            int courseRecords = 0;
            
            for (Student student : enrolledStudents) {
                List<AttendanceRecord> records = attendanceRecordRepository.findByStudentIdAndCourseId(student.getId(), course.getId());
                
                long presentCount = records.stream().filter(r -> r.getStatus().equals("PRESENT")).count();
                courseRecords += records.size();
                courseAttendance += presentCount;
            }
            
            double courseAttendancePercentage = courseRecords > 0 ? (courseAttendance * 100.0) / courseRecords : 0;
            courseStat.put("attendancePercentage", courseAttendancePercentage);
            
            courseStats.add(courseStat);
        }
        
        report.put("courses", courseStats);
        
        return report;
    }
}