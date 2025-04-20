package com.university.attendance.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "leave_applications")
public class LeaveApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = true)
    private Course course; // Optional - if leave is for a specific course
    
    @Column(nullable = false)
    private String leaveType; // MEDICAL, PERSONAL, EVENT
    
    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date fromDate;
    
    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date toDate;
    
    @Column(nullable = false, length = 500)
    private String reason;
    
    @Column(nullable = true, length = 255)
    private String documentPath;
    
    @Column(nullable = false)
    private String status; // PENDING, APPROVED, REJECTED
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewed_by", nullable = true)
    private Faculty reviewedBy;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = true)
    private Date reviewedAt;
    
    @Column(nullable = true, length = 255)
    private String comments;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date appliedAt;
    
    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDocumentPath() {
        return documentPath;
    }

    public void setDocumentPath(String documentPath) {
        this.documentPath = documentPath;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Faculty getReviewedBy() {
        return reviewedBy;
    }

    public void setReviewedBy(Faculty reviewedBy) {
        this.reviewedBy = reviewedBy;
    }

    public Date getReviewedAt() {
        return reviewedAt;
    }

    public void setReviewedAt(Date reviewedAt) {
        this.reviewedAt = reviewedAt;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Date getAppliedAt() {
        return appliedAt;
    }

    public void setAppliedAt(Date appliedAt) {
        this.appliedAt = appliedAt;
    }
    
    @PrePersist
    protected void onCreate() {
        appliedAt = new Date();
        status = "PENDING";
    }
}