// FacultyService.java
package com.university.attendance.service;

import com.university.attendance.model.Course;
import com.university.attendance.model.Faculty;
import com.university.attendance.repository.CourseRepository;
import com.university.attendance.repository.FacultyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;
    private final CourseRepository courseRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public FacultyService(
            FacultyRepository facultyRepository,
            CourseRepository courseRepository,
            PasswordEncoder passwordEncoder) {
        this.facultyRepository = facultyRepository;
        this.courseRepository = courseRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Faculty> getAllFaculty() {
        return facultyRepository.findAll();
    }
    
    public Optional<Faculty> getFacultyById(Long id) {
        return facultyRepository.findById(id);
    }
    
    public Optional<Faculty> getFacultyByFacultyId(String facultyId) {
        return facultyRepository.findByFacultyId(facultyId);
    }
    
    @Transactional
    public Faculty createFaculty(Faculty faculty) {
        // Check if faculty ID already exists
        if (facultyRepository.existsByFacultyId(faculty.getFacultyId())) {
            throw new RuntimeException("Faculty ID already exists");
        }
        
        // Encrypt the password
        faculty.setPassword(passwordEncoder.encode(faculty.getPassword()));
        faculty.setCreatedAt(new Date());
        
        return facultyRepository.save(faculty);
    }
    
    @Transactional
    public Faculty updateFaculty(Faculty faculty) {
        // Check if faculty exists
        Faculty existingFaculty = facultyRepository.findById(faculty.getId())
                .orElseThrow(() -> new RuntimeException("Faculty not found"));
        
        // Check if updating faculty ID and if it exists
        if (!existingFaculty.getFacultyId().equals(faculty.getFacultyId()) && 
            facultyRepository.existsByFacultyId(faculty.getFacultyId())) {
            throw new RuntimeException("Faculty ID already exists");
        }
        
        // Only update password if it's changed
        if (faculty.getPassword() != null && !faculty.getPassword().isEmpty()) {
            faculty.setPassword(passwordEncoder.encode(faculty.getPassword()));
        } else {
            faculty.setPassword(existingFaculty.getPassword());
        }
        
        return facultyRepository.save(faculty);
    }
    
    @Transactional
    public void deleteFaculty(Long id) {
        facultyRepository.deleteById(id);
    }
    
    public List<Faculty> getFacultyByDepartment(String department) {
        return facultyRepository.findByDepartment(department);
    }
    
    public List<Course> getCoursesByFaculty(Long facultyId) {
        Faculty faculty = facultyRepository.findById(facultyId)
                .orElseThrow(() -> new RuntimeException("Faculty not found"));
        
        return courseRepository.findByFaculty(faculty);
    }
    
    @Transactional
    public Course assignCourseToFaculty(Long courseId, Long facultyId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        
        Faculty faculty = facultyRepository.findById(facultyId)
                .orElseThrow(() -> new RuntimeException("Faculty not found"));
        
        course.setFaculty(faculty);
        return courseRepository.save(course);
    }
}