package com.university.attendance.service;
import com.university.attendance.model.Admin;
import com.university.attendance.model.Course;
import com.university.attendance.model.Faculty;
import com.university.attendance.model.Student;
import com.university.attendance.repository.AdminRepository;
import com.university.attendance.repository.CourseRepository;
import com.university.attendance.repository.FacultyRepository;
import com.university.attendance.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;
    private final CourseRepository courseRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminService(
            AdminRepository adminRepository,
            StudentRepository studentRepository,
            FacultyRepository facultyRepository,
            CourseRepository courseRepository,
            PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
        this.courseRepository = courseRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<Admin> getAdminById(Long id) {
        return adminRepository.findById(id);
    }
    
    @Transactional
    public Admin createAdmin(Admin admin) {
        // Encrypt the password
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        admin.setCreatedAt(new Date());
        
        return adminRepository.save(admin);
    }
    
    @Transactional
    public Admin updateAdmin(Admin admin) {
        // Check if admin exists
        Admin existingAdmin = adminRepository.findById(admin.getId())
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        
        // Only update password if it's changed
        if (admin.getPassword() != null && !admin.getPassword().isEmpty()) {
            admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        } else {
            admin.setPassword(existingAdmin.getPassword());
        }
        
        return adminRepository.save(admin);
    }
    
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // Count total students
        long totalStudents = studentRepository.count();
        stats.put("totalStudents", totalStudents);
        
        // Count total faculty
        long totalFaculty = facultyRepository.count();
        stats.put("totalFaculty", totalFaculty);
        
        // Count total courses
        long totalCourses = courseRepository.count();
        stats.put("totalCourses", totalCourses);
        
        // Get departments and count for each
        List<Student> students = studentRepository.findAll();
        Map<String, Long> departmentStats = new HashMap<>();
        
        for (Student student : students) {
            String department = student.getDepartment();
            departmentStats.put(department, departmentStats.getOrDefault(department, 0L) + 1);
        }
        
        stats.put("departmentStats", departmentStats);
        
        return stats;
    }
    
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
    
    public List<Faculty> getAllFaculty() {
        return facultyRepository.findAll();
    }
    
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }
    
    @Transactional
    public void deleteStudent(Long studentId) {
        studentRepository.deleteById(studentId);
    }
    
    @Transactional
    public void deleteFaculty(Long facultyId) {
        facultyRepository.deleteById(facultyId);
    }
    
    @Transactional
    public void deleteCourse(Long courseId) {
        courseRepository.deleteById(courseId);
    }
}