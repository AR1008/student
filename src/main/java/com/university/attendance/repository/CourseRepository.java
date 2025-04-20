package com.university.attendance.repository;

import com.university.attendance.model.Course;
import com.university.attendance.model.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findByCourseCode(String courseCode);
    List<Course> findByDepartment(String department);
    List<Course> findBySemester(Integer semester);
    List<Course> findByFaculty(Faculty faculty);
    boolean existsByCourseCode(String courseCode);
}