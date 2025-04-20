package com.university.attendance.model;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "attendances")
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faculty_id", nullable = false)
    private Faculty faculty;
    
    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date date;
    
    @Temporal(TemporalType.TIME)
    @Column(nullable = false)
    private Date startTime;
    
    @Temporal(TemporalType.TIME)
    @Column(nullable = false)
    private Date endTime;
    
    @Column(nullable = false)
    private String status; // PENDING, COMPLETED
    
    @OneToMany(mappedBy = "attendance", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<AttendanceRecord> attendanceRecords = new HashSet<>();
    
    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Set<AttendanceRecord> getAttendanceRecords() {
        return attendanceRecords;
    }

    public void setAttendanceRecords(Set<AttendanceRecord> attendanceRecords) {
        this.attendanceRecords = attendanceRecords;
    }
}