// AttendanceRepository.java
package com.university.attendance.repository;

import com.university.attendance.model.Attendance;
import com.university.attendance.model.Course;
import com.university.attendance.model.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    
    List<Attendance> findByCourse(Course course);
    
    List<Attendance> findByFaculty(Faculty faculty);
    
    List<Attendance> findByDate(Date date);
    
    List<Attendance> findByCourseAndDateBetween(Course course, Date startDate, Date endDate);
    
    List<Attendance> findByStatus(String status);
    
    @Query("SELECT a FROM Attendance a WHERE a.course.id = :courseId")
    List<Attendance> findByCourseId(@Param("courseId") Long courseId);
    
    @Query("SELECT a FROM Attendance a WHERE a.faculty.id = :facultyId")
    List<Attendance> findByFacultyId(@Param("facultyId") Long facultyId);
    
    @Query("SELECT a FROM Attendance a WHERE a.date = :date AND a.course.id = :courseId")
    List<Attendance> findByDateAndCourseId(@Param("date") Date date, @Param("courseId") Long courseId);
    
    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.course.id = :courseId")
    Long countByCourseId(@Param("courseId") Long courseId);
    
    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.faculty.id = :facultyId")
    Long countByFacultyId(@Param("facultyId") Long facultyId);
}