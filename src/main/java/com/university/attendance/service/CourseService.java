// CourseService.java
package com.university.attendance.service;

import com.university.attendance.model.Course;
import com.university.attendance.model.Faculty;
import com.university.attendance.repository.CourseRepository;
import com.university.attendance.repository.FacultyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final FacultyRepository facultyRepository;

    @Autowired
    public CourseService(
            CourseRepository courseRepository,
            FacultyRepository facultyRepository) {
        this.courseRepository = courseRepository;
        this.facultyRepository = facultyRepository;
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }
    
    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }
    
    public Optional<Course> getCourseByCourseCode(String courseCode) {
        return courseRepository.findByCourseCode(courseCode);
    }
    
    @Transactional
    public Course createCourse(Course course) {
        // Check if course code already exists
        if (courseRepository.existsByCourseCode(course.getCourseCode())) {
            throw new RuntimeException("Course code already exists");
        }
        
        // Check if faculty exists if assigned
        if (course.getFaculty() != null) {
            facultyRepository.findById(course.getFaculty().getId())
                .orElseThrow(() -> new RuntimeException("Faculty not found"));
        }
        
        return courseRepository.save(course);
    }
    
    @Transactional
    public Course updateCourse(Course course) {
        // Check if course exists
        Course existingCourse = courseRepository.findById(course.getId())
                .orElseThrow(() -> new RuntimeException("Course not found"));
        
        // Check if updating course code and if it exists
        if (!existingCourse.getCourseCode().equals(course.getCourseCode()) && 
            courseRepository.existsByCourseCode(course.getCourseCode())) {
            throw new RuntimeException("Course code already exists");
        }
        
        // Check if faculty exists if assigned
        if (course.getFaculty() != null) {
            facultyRepository.findById(course.getFaculty().getId())
                .orElseThrow(() -> new RuntimeException("Faculty not found"));
        }
        
        return courseRepository.save(course);
    }
    
    @Transactional
    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }
    
    public List<Course> getCoursesByDepartment(String department) {
        return courseRepository.findByDepartment(department);
    }
    
    public List<Course> getCoursesBySemester(Integer semester) {
        return courseRepository.findBySemester(semester);
    }
    
    public List<Course> getCoursesByFaculty(Long facultyId) {
        Faculty faculty = facultyRepository.findById(facultyId)
                .orElseThrow(() -> new RuntimeException("Faculty not found"));
        
        return courseRepository.findByFaculty(faculty);
    }
    
    @Transactional
    public Course assignFacultyToCourse(Long courseId, Long facultyId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        
        Faculty faculty = facultyRepository.findById(facultyId)
                .orElseThrow(() -> new RuntimeException("Faculty not found"));
        
        course.setFaculty(faculty);
        return courseRepository.save(course);
    }
    
    @Transactional
    public Course removeFacultyFromCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        
        course.setFaculty(null);
        return courseRepository.save(course);
    }
}