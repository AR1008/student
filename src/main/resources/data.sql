-- Insert initial admin user
INSERT INTO users (id, username, password, full_name, email, role, created_at) 
VALUES (1, 'admin', '$2a$10$hKDVYxLefVHV/vtuPhWD3OigtRyOykRLDdUAp80Z1crSoS1lFqaFS', 'System Administrator', 'admin@example.com', 'ADMIN', CURRENT_TIMESTAMP());

INSERT INTO admins (user_id) 
VALUES (1);

-- Insert faculty users
INSERT INTO users (id, username, password, full_name, email, role, created_at) 
VALUES (2, 'faculty1', '$2a$10$hKDVYxLefVHV/vtuPhWD3OigtRyOykRLDdUAp80Z1crSoS1lFqaFS', 'John Smith', 'john.smith@example.com', 'FACULTY', CURRENT_TIMESTAMP());

INSERT INTO faculty (user_id, faculty_id, department, designation) 
VALUES (2, 'FAC001', 'Computer Science', 'Assistant Professor');

INSERT INTO users (id, username, password, full_name, email, role, created_at) 
VALUES (3, 'faculty2', '$2a$10$hKDVYxLefVHV/vtuPhWD3OigtRyOykRLDdUAp80Z1crSoS1lFqaFS', 'Sarah Johnson', 'sarah.johnson@example.com', 'FACULTY', CURRENT_TIMESTAMP());

INSERT INTO faculty (user_id, faculty_id, department, designation) 
VALUES (3, 'FAC002', 'Electronics', 'Associate Professor');

-- Insert student users
INSERT INTO users (id, username, password, full_name, email, role, created_at) 
VALUES (4, 'student1', '$2a$10$hKDVYxLefVHV/vtuPhWD3OigtRyOykRLDdUAp80Z1crSoS1lFqaFS', 'Alice Brown', 'alice.brown@example.com', 'STUDENT', CURRENT_TIMESTAMP());

INSERT INTO students (user_id, roll_number, department, year, semester) 
VALUES (4, 'CS001', 'Computer Science', 2, '3');

INSERT INTO users (id, username, password, full_name, email, role, created_at) 
VALUES (5, 'student2', '$2a$10$hKDVYxLefVHV/vtuPhWD3OigtRyOykRLDdUAp80Z1crSoS1lFqaFS', 'Bob Wilson', 'bob.wilson@example.com', 'STUDENT', CURRENT_TIMESTAMP());

INSERT INTO students (user_id, roll_number, department, year, semester) 
VALUES (5, 'CS002', 'Computer Science', 2, '3');

-- Insert courses
INSERT INTO courses (id, course_code, course_name, department, semester, credits, min_attendance_percentage, faculty_id) 
VALUES (1, 'CS301', 'Data Structures and Algorithms', 'Computer Science', 3, 4, 75, 2);

INSERT INTO courses (id, course_code, course_name, department, semester, credits, min_attendance_percentage, faculty_id) 
VALUES (2, 'CS302', 'Database Management Systems', 'Computer Science', 3, 4, 75, 2);

INSERT INTO courses (id, course_code, course_name, department, semester, credits, min_attendance_percentage, faculty_id) 
VALUES (3, 'EC301', 'Digital Electronics', 'Electronics', 3, 4, 75, 3);

-- Enroll students in courses
INSERT INTO student_courses (student_id, course_id) 
VALUES (4, 1), (4, 2), (5, 1), (5, 2);