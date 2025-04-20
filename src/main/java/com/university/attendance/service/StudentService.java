// StudentService.java
package com.university.attendance.service;

import com.university.attendance.model.Course;
import com.university.attendance.model.Student;
import com.university.attendance.repository.CourseRepository;
import com.university.attendance.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public StudentService(
            StudentRepository studentRepository,
            CourseRepository courseRepository,
            PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
    
    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }
    
    public Optional<Student> getStudentByRollNumber(String rollNumber) {
        return studentRepository.findByRollNumber(rollNumber);
    }
    
    @Transactional
    public Student createStudent(Student student) {
        // Check if roll number already exists
        if (studentRepository.existsByRollNumber(student.getRollNumber())) {
            throw new RuntimeException("Roll number already exists");
        }
        
        // Encrypt the password
        student.setPassword(passwordEncoder.encode(student.getPassword()));
        student.setCreatedAt(new Date());
        
        return studentRepository.save(student);
    }
    
    @Transactional
    public Student updateStudent(Student student) {
        // Check if student exists
        Student existingStudent = studentRepository.findById(student.getId())
                .orElseThrow(() -> new RuntimeException("Student not found"));
        
        // Check if updating roll number and if it exists
        if (!existingStudent.getRollNumber().equals(student.getRollNumber()) && 
            studentRepository.existsByRollNumber(student.getRollNumber())) {
            throw new RuntimeException("Roll number already exists");
        }
        
        // Only update password if it's changed
        if (student.getPassword() != null && !student.getPassword().isEmpty()) {
            student.setPassword(passwordEncoder.encode(student.getPassword()));
        } else {
            student.setPassword(existingStudent.getPassword());
        }
        
        return studentRepository.save(student);
    }
    
    @Transactional
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }
    
    @Transactional
    public Student enrollStudentInCourse(Long studentId, Long courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        
        student.addCourse(course);
        return studentRepository.save(student);
    }
    
    @Transactional
    public Student removeStudentFromCourse(Long studentId, Long courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        
        student.removeCourse(course);
        return studentRepository.save(student);
    }
    
    public List<Student> getStudentsByDepartment(String department) {
        return studentRepository.findByDepartment(department);
    }
    
    public List<Student> getStudentsByYearAndSemester(Integer year, String semester) {
        return studentRepository.findByYearAndSemester(year, semester);
    }
    
    public List<Student> getStudentsByCourse(Long courseId) {
        return studentRepository.findByCourseId(courseId);
    }
    
    @Transactional
    public Student updateBiometricId(Long studentId, String biometricId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        
        student.setBiometricId(biometricId);
        return studentRepository.save(student);
    }
}