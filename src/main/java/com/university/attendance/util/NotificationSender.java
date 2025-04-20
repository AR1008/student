// NotificationSender.java
package com.university.attendance.util;

import com.university.attendance.model.Notification;
import org.springframework.stereotype.Component;

/**
 * Utility class to send real-time notifications to users.
 * In a real application, this could use WebSockets, push notifications, emails, etc.
 */
@Component
public class NotificationSender {

    /**
     * Sends a notification to the user.
     * 
     * @param notification The notification to send
     */
    public void sendNotification(Notification notification) {
        // In a real application, this would send actual notifications
        // For this demo, we'll just log it
        System.out.println("Notification sent to " + notification.getUser().getFullName() + ": " + notification.getTitle());
    }
    
    /**
     * Sends an email notification.
     * 
     * @param email Recipient email
     * @param subject Email subject
     * @param body Email body
     */
    public void sendEmailNotification(String email, String subject, String body) {
        // In a real application, this would send actual emails
        // For this demo, we'll just log it
        System.out.println("Email sent to " + email + ": " + subject);
    }
    
    /**
     * Sends an SMS notification.
     * 
     * @param phoneNumber Recipient phone number
     * @param message SMS message
     */
    public void sendSmsNotification(String phoneNumber, String message) {
        // In a real application, this would send actual SMS
        // For this demo, we'll just log it
        System.out.println("SMS sent to " + phoneNumber + ": " + message);
    }
}
