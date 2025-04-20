// LeaveApplicationRepository.java
package com.university.attendance.repository;

import com.university.attendance.model.Course;
import com.university.attendance.model.Faculty;
import com.university.attendance.model.LeaveApplication;
import com.university.attendance.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface LeaveApplicationRepository extends JpaRepository<LeaveApplication, Long> {
    List<LeaveApplication> findByStudent(Student student);
    List<LeaveApplication> findByStudentAndStatus(Student student, String status);
    List<LeaveApplication> findByCourse(Course course);
    List<LeaveApplication> findByReviewedBy(Faculty faculty);
    List<LeaveApplication> findByStatus(String status);
    List<LeaveApplication> findByFromDateLessThanEqualAndToDateGreaterThanEqual(Date currentDate, Date currentDate2);
}