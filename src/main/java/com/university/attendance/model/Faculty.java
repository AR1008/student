package com.university.attendance.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "faculty")
@PrimaryKeyJoinColumn(name = "user_id")
public class Faculty extends User {
    
    @Column(nullable = false, unique = true)
    private String facultyId;
    
    @Column(nullable = false)
    private String department;
    
    @Column(nullable = false)
    private String designation;
    
    @OneToMany(mappedBy = "faculty", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Course> courses = new HashSet<>();
    
    // Constructors
    public Faculty() {
        super();
        this.setRole("FACULTY");
    }
    
    // Getters and Setters
    public String getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(String facultyId) {
        this.facultyId = facultyId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }
}