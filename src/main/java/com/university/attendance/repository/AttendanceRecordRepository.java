package com.university.attendance.repository;

import com.university.attendance.model.Attendance;
import com.university.attendance.model.AttendanceRecord;
import com.university.attendance.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecord, Long> {
    List<AttendanceRecord> findByAttendance(Attendance attendance);
    List<AttendanceRecord> findByStudent(Student student);
    List<AttendanceRecord> findByAttendanceAndStudent(Attendance attendance, Student student);
    List<AttendanceRecord> findByStatus(String status);
    
    @Query("SELECT ar FROM AttendanceRecord ar JOIN ar.attendance a WHERE ar.student.id = :studentId AND a.course.id = :courseId")
    List<AttendanceRecord> findByStudentIdAndCourseId(@Param("studentId") Long studentId, @Param("courseId") Long courseId);
    
    @Query("SELECT ar FROM AttendanceRecord ar JOIN ar.attendance a WHERE ar.student.id = :studentId AND a.course.id = :courseId AND a.date BETWEEN :startDate AND :endDate")
    List<AttendanceRecord> findByStudentIdAndCourseIdAndDateBetween(
            @Param("studentId") Long studentId, 
            @Param("courseId") Long courseId, 
            @Param("startDate") Date startDate, 
            @Param("endDate") Date endDate);
    
    @Query("SELECT COUNT(ar) FROM AttendanceRecord ar JOIN ar.attendance a WHERE ar.student.id = :studentId AND a.course.id = :courseId AND ar.status = :status")
    Long countByStudentIdAndCourseIdAndStatus(
            @Param("studentId") Long studentId, 
            @Param("courseId") Long courseId, 
            @Param("status") String status);
}