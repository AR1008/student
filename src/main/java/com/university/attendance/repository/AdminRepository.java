package com.university.attendance.repository;

import com.university.attendance.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    // Any additional methods specific to Admin if needed
}