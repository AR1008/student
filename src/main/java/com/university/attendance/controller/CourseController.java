package com.university.attendance.controller;

import com.university.attendance.model.Course;
import com.university.attendance.model.Faculty;
import com.university.attendance.model.Student;
import com.university.attendance.service.CourseService;
import com.university.attendance.service.FacultyService;
import com.university.attendance.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;
    private final StudentService studentService;
    private final FacultyService facultyService;

    @Autowired
    public CourseController(
            CourseService courseService,
            StudentService studentService,
            FacultyService facultyService) {
        this.courseService = courseService;
        this.studentService = studentService;
        this.facultyService = facultyService;
    }

    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
        Optional<Course> course = courseService.getCourseById(id);
        return course.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/code/{courseCode}")
    public ResponseEntity<Course> getCourseByCourseCode(@PathVariable String courseCode) {
        Optional<Course> course = courseService.getCourseByCourseCode(courseCode);
        return course.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/department/{department}")
    public ResponseEntity<List<Course>> getCoursesByDepartment(@PathVariable String department) {
        List<Course> courses = courseService.getCoursesByDepartment(department);
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/semester/{semester}")
    public ResponseEntity<List<Course>> getCoursesBySemester(@PathVariable Integer semester) {
        List<Course> courses = courseService.getCoursesBySemester(semester);
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/faculty/{facultyId}")
    public ResponseEntity<List<Course>> getCoursesByFaculty(@PathVariable Long facultyId) {
        List<Course> courses = courseService.getCoursesByFaculty(facultyId);
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/{courseId}/students")
    public ResponseEntity<List<Student>> getStudentsInCourse(@PathVariable Long courseId) {
        List<Student> students = studentService.getStudentsByCourse(courseId);
        return ResponseEntity.ok(students);
    }

    @PostMapping
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        Course createdCourse = courseService.createCourse(course);
        return ResponseEntity.ok(createdCourse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable Long id, @RequestBody Course course) {
        course.setId(id);
        Course updatedCourse = courseService.updateCourse(course);
        return ResponseEntity.ok(updatedCourse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{courseId}/faculty/{facultyId}")
    public ResponseEntity<Course> assignFacultyToCourse(@PathVariable Long courseId, @PathVariable Long facultyId) {
        Course course = courseService.assignFacultyToCourse(courseId, facultyId);
        return ResponseEntity.ok(course);
    }

    @DeleteMapping("/{courseId}/faculty")
    public ResponseEntity<Course> removeFacultyFromCourse(@PathVariable Long courseId) {
        Course course = courseService.removeFacultyFromCourse(courseId);
        return ResponseEntity.ok(course);
    }

    @PutMapping("/{courseId}/students/{studentId}")
    public ResponseEntity<Student> enrollStudentInCourse(@PathVariable Long courseId, @PathVariable Long studentId) {
        Student student = studentService.enrollStudentInCourse(studentId, courseId);
        return ResponseEntity.ok(student);
    }

    @DeleteMapping("/{courseId}/students/{studentId}")
    public ResponseEntity<Student> removeStudentFromCourse(@PathVariable Long courseId, @PathVariable Long studentId) {
        Student student = studentService.removeStudentFromCourse(studentId, courseId);
        return ResponseEntity.ok(student);
    }
}