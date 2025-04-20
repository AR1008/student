// BiometricVerifier.java
package com.university.attendance.util;

import com.university.attendance.model.Student;
import org.springframework.stereotype.Component;

/**
 * A simple utility class that simulates biometric verification.
 * In a real application, this would interact with actual biometric hardware/software.
 */
@Component
public class BiometricVerifier {

    /**
     * Verifies a student's identity using biometric data.
     * 
     * @param student The student to verify
     * @param biometricData The biometric data provided for verification
     * @return true if verification is successful, false otherwise
     */
    public boolean verifyIdentity(Student student, String biometricData) {
        // In a real application, this would use actual biometric verification
        // For this demo, we'll do a simple check
        if (student.getBiometricId() == null || student.getBiometricId().isEmpty()) {
            // No biometric ID registered for this student
            return false;
        }
        
        // For demo purposes, any non-empty biometric data will be accepted
        // as the verification is just simulated
        return biometricData != null && !biometricData.isEmpty();
    }
    
    /**
     * Registers biometric data for a student.
     * 
     * @param student The student to register
     * @param biometricData The biometric data to register
     * @return A unique biometric ID
     */
    public String registerBiometricData(Student student, String biometricData) {
        // In a real application, this would process and store actual biometric data
        // For this demo, we'll generate a simple ID
        String biometricId = "BIO-" + student.getId() + "-" + System.currentTimeMillis();
        return biometricId;
    }
}
