-- Database schema for Student Attendance Management System

-- Users table (parent table for all user types)
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    role VARCHAR(20) NOT NULL, -- ADMIN, FACULTY, STUDENT
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP NULL
);

-- Students table
CREATE TABLE students (
    user_id BIGINT PRIMARY KEY,
    roll_number VARCHAR(20) NOT NULL UNIQUE,
    department VARCHAR(50) NOT NULL,
    year INT NOT NULL,
    semester VARCHAR(20) NOT NULL,
    biometric_id VARCHAR(100) NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Faculty table
CREATE TABLE faculty (
    user_id BIGINT PRIMARY KEY,
    faculty_id VARCHAR(20) NOT NULL UNIQUE,
    department VARCHAR(50) NOT NULL,
    designation VARCHAR(50) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Admin table
CREATE TABLE admins (
    user_id BIGINT PRIMARY KEY,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Courses table
CREATE TABLE courses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    course_code VARCHAR(20) NOT NULL UNIQUE,
    course_name VARCHAR(100) NOT NULL,
    department VARCHAR(50) NOT NULL,
    semester INT NOT NULL,
    credits INT NOT NULL,
    min_attendance_percentage INT NOT NULL,
    faculty_id BIGINT NULL,
    FOREIGN KEY (faculty_id) REFERENCES faculty(user_id) ON DELETE SET NULL
);

-- Student-Course mapping (many-to-many)
CREATE TABLE student_courses (
    student_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    PRIMARY KEY (student_id, course_id),
    FOREIGN KEY (student_id) REFERENCES students(user_id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE
);

-- Attendance sessions
CREATE TABLE attendances (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    course_id BIGINT NOT NULL,
    faculty_id BIGINT NOT NULL,
    date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    status VARCHAR(20) NOT NULL, -- PENDING, COMPLETED
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE,
    FOREIGN KEY (faculty_id) REFERENCES faculty(user_id) ON DELETE CASCADE
);

-- Individual attendance records
CREATE TABLE attendance_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    attendance_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL, -- PRESENT, ABSENT, LATE, EXCUSED
    verification_method VARCHAR(20) NULL, -- BIOMETRIC, MANUAL, SELF
    marked_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    remarks VARCHAR(255) NULL,
    FOREIGN KEY (attendance_id) REFERENCES attendances(id) ON DELETE CASCADE,
    FOREIGN KEY (student_id) REFERENCES students(user_id) ON DELETE CASCADE
);

-- Leave applications
CREATE TABLE leave_applications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    course_id BIGINT NULL, -- NULL if it's for all courses
    leave_type VARCHAR(20) NOT NULL, -- MEDICAL, PERSONAL, EVENT
    from_date DATE NOT NULL,
    to_date DATE NOT NULL,
    reason VARCHAR(500) NOT NULL,
    document_path VARCHAR(255) NULL,
    status VARCHAR(20) NOT NULL, -- PENDING, APPROVED, REJECTED
    reviewed_by BIGINT NULL,
    reviewed_at TIMESTAMP NULL,
    comments VARCHAR(255) NULL,
    applied_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES students(user_id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE,
    FOREIGN KEY (reviewed_by) REFERENCES faculty(user_id) ON DELETE SET NULL
);

-- Notifications
CREATE TABLE notifications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    title VARCHAR(100) NOT NULL,
    message VARCHAR(500) NOT NULL,
    type VARCHAR(20) NOT NULL, -- ATTENDANCE, LEAVE, SYSTEM
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    read_at TIMESTAMP NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Insert sample data for testing

-- Admin user
INSERT INTO users (username, password, full_name, email, role)
VALUES ('admin', '$2a$10$hKDVYxLefVHV/vtuPhWD3OigtRyOykRLDdUAp80Z1crSoS1lFqaFS', 'System Administrator', 'admin@example.com', 'ADMIN');

INSERT INTO admins (user_id)
VALUES (LAST_INSERT_ID());

-- Faculty users
INSERT INTO users (username, password, full_name, email, role)
VALUES ('faculty1', '$2a$10$hKDVYxLefVHV/vtuPhWD3OigtRyOykRLDdUAp80Z1crSoS1lFqaFS', 'John Smith', 'john.smith@example.com', 'FACULTY');

INSERT INTO faculty (user_id, faculty_id, department, designation)
VALUES (LAST_INSERT_ID(), 'FAC001', 'Computer Science', 'Assistant Professor');

INSERT INTO users (username, password, full_name, email, role)
VALUES ('faculty2', '$2a$10$hKDVYxLefVHV/vtuPhWD3OigtRyOykRLDdUAp80Z1crSoS1lFqaFS', 'Sarah Johnson', 'sarah.johnson@example.com', 'FACULTY');

INSERT INTO faculty (user_id, faculty_id, department, designation)
VALUES (LAST_INSERT_ID(), 'FAC002', 'Electronics', 'Associate Professor');

-- Student users
INSERT INTO users (username, password, full_name, email, role)
VALUES ('student1', '$2a$10$hKDVYxLefVHV/vtuPhWD3OigtRyOykRLDdUAp80Z1crSoS1lFqaFS', 'Alice Brown', 'alice.brown@example.com', 'STUDENT');

INSERT INTO students (user_id, roll_number, department, year, semester)
VALUES (LAST_INSERT_ID(), 'CS001', 'Computer Science', 2, '3');

INSERT INTO users (username, password, full_name, email, role)
VALUES ('student2', '$2a$10$hKDVYxLefVHV/vtuPhWD3OigtRyOykRLDdUAp80Z1crSoS1lFqaFS', 'Bob Wilson', 'bob.wilson@example.com', 'STUDENT');

INSERT INTO students (user_id, roll_number, department, year, semester)
VALUES (LAST_INSERT_ID(), 'CS002', 'Computer Science', 2, '3');

INSERT INTO users (username, password, full_name, email, role)
VALUES ('student3', '$2a$10$hKDVYxLefVHV/vtuPhWD3OigtRyOykRLDdUAp80Z1crSoS1lFqaFS', 'Charlie Davis', 'charlie.davis@example.com', 'STUDENT');

INSERT INTO students (user_id, roll_number, department, year, semester)
VALUES (LAST_INSERT_ID(), 'EC001', 'Electronics', 2, '3');

-- Courses
INSERT INTO courses (course_code, course_name, department, semester, credits, min_attendance_percentage, faculty_id)
VALUES ('CS301', 'Data Structures and Algorithms', 'Computer Science', 3, 4, 75, 2);

INSERT INTO courses (course_code, course_name, department, semester, credits, min_attendance_percentage, faculty_id)
VALUES ('CS302', 'Database Management Systems', 'Computer Science', 3, 4, 75, 2);

INSERT INTO courses (course_code, course_name, department, semester, credits, min_attendance_percentage, faculty_id)
VALUES ('EC301', 'Digital Electronics', 'Electronics', 3, 4, 75, 3);

-- Enroll students in courses
INSERT INTO student_courses (student_id, course_id)
VALUES (4, 1), (4, 2), (5, 1), (5, 2), (6, 3);

-- Note: The password for all users is 'password'
-- The hash is a BCrypt encoding of 'password'