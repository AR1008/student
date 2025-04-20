Student Attendance Management System
A comprehensive web-based solution for managing student attendance in educational institutions.
Features
Digital attendance marking with biometric verification
Student self-check-in capability
Leave application workflow
Real-time attendance tracking and reporting
Course management
User management (Admin, Faculty, Students)
Notifications system
Technology Stack
Backend: Java with Spring Boot, Spring MVC, Spring Data JPA
Frontend: Thymeleaf, Bootstrap, jQuery
Database: MySQL
Security: Spring Security with BCrypt password encoding
Build Tool: Maven
Prerequisites
JDK 11 or higher
Maven 3.6 or higher
MySQL 8.0 or higher
Setup Instructions
1. Clone the Repository
bash
git clone https://github.com/yourusername/student-attendance-system.git
cd student-attendance-system
2. Configure Database
Create a MySQL database and update the database configuration in src/main/resources/application.properties:
properties
spring.datasource.url=jdbc:mysql://localhost:3306/attendance_db?createDatabaseIfNotExist=true&useSSL=false
spring.datasource.username=your_username
spring.datasource.password=your_password
3. Build the Project
bash
mvn clean install
4. Run the Application
bash
mvn spring-boot:run
The application will be accessible at http://localhost:8080/attendance
5. Initial Login Credentials
Role	Username	Password
Admin	admin	password
Faculty	faculty1	password
Student	student1	password
Project Structure
student-attendance-system/
├── src/
│   ├── main/
│   │   ├── java/com/university/attendance/
│   │   │   ├── controller/     # MVC Controllers
│   │   │   ├── model/          # Entity classes
│   │   │   ├── repository/     # Data access interfaces
│   │   │   ├── service/        # Business logic
│   │   │   └── util/           # Utility classes
│   │   └── resources/
│   │       ├── static/         # CSS, JS, images
│   │       ├── templates/      # Thymeleaf templates
│   │       └── application.properties
│   └── test/                   # Unit and integration tests
├── database/
│   └── schema.sql              # Database schema
└── pom.xml                     # Maven configuration
Use Cases Implemented
Student Use Cases
View attendance
Mark attendance (with biometric verification)
Submit leave applications
View notifications
Faculty Use Cases
Create attendance sessions
Mark student attendance
Process leave applications
Generate reports
Admin Use Cases
Manage students
Manage faculty
Manage courses
View reports
Notes for Developers
The biometric verification is simulated for demonstration purposes
For real-world deployment, consider enhancing security measures
The notification system uses console logging for demonstration; in production, implement a proper notification service
Contributing
Fork the repository
Create your feature branch (git checkout -b feature/amazing-feature)
Commit your changes (git commit -m 'Add some amazing feature')
Push to the branch (git push origin feature/amazing-feature)
Open a Pull Request
