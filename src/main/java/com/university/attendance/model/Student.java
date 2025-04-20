package com.university.attendance.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "students")
@PrimaryKeyJoinColumn(name = "user_id")
public class Student extends User {
    
    @Column(nullable = false, unique = true)
    private String rollNumber;
    
    @Column(nullable = false)
    private String department;
    
    @Column(nullable = false)
    private Integer year;
    
    @Column(nullable = false)
    private String semester;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "student_courses",
        joinColumns = @JoinColumn(name = "student_id"),
        inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private Set<Course> courses = new HashSet<>();
    
    // Biometric identifier
    @Column(name = "biometric_id")
    private String biometricId;
    
    // Constructors
    public Student() {
        super();
        this.setRole("STUDENT");
    }
    
    // Getters and Setters
    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }
    
    public void addCourse(Course course) {
        this.courses.add(course);
        course.getStudents().add(this);
    }
    
    public void removeCourse(Course course) {
        this.courses.remove(course);
        course.getStudents().remove(this);
    }

    public String getBiometricId() {
        return biometricId;
    }

    public void setBiometricId(String biometricId) {
        this.biometricId = biometricId;
    }
}