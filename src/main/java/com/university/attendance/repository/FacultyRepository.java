package com.university.attendance.repository;

import com.university.attendance.model.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    Optional<Faculty> findByFacultyId(String facultyId);
    List<Faculty> findByDepartment(String department);
    boolean existsByFacultyId(String facultyId);
}