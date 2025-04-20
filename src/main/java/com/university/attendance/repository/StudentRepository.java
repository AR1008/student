package com.university.attendance.repository;

import com.university.attendance.model.Course;
import com.university.attendance.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByRollNumber(String rollNumber);
    List<Student> findByDepartment(String department);
    List<Student> findByYearAndSemester(Integer year, String semester);
    
    @Query("SELECT s FROM Student s JOIN s.courses c WHERE c.id = :courseId")
    List<Student> findByCourseId(@Param("courseId") Long courseId);
    
    boolean existsByRollNumber(String rollNumber);
}