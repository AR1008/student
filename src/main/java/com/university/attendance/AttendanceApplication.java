package com.university.attendance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.method.HandlerMethod;
import java.util.Map;

@SpringBootApplication
public class AttendanceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AttendanceApplication.class, args);
    }
    
    // @Bean
    // public PasswordEncoder passwordEncoder() {
    //     return new BCryptPasswordEncoder();
    // }
    
    // Create upload directory for leave application documents
    @Bean
    public void createUploadDirectories() {
        try {
            java.nio.file.Files.createDirectories(java.nio.file.Paths.get("uploads/documents"));
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
    
    // Debug helper to print all request mappings
    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            System.out.println("Let's inspect the request mappings:");
            
            RequestMappingHandlerMapping requestMappingHandlerMapping = ctx.getBean("requestMappingHandlerMapping", RequestMappingHandlerMapping.class);
            Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping.getHandlerMethods();
            
            map.forEach((key, value) -> System.out.println("Mapping: " + key + " -> " + value));
        };
    }
}