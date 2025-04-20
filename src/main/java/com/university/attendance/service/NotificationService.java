// NotificationService.java
package com.university.attendance.service;

import com.university.attendance.model.Notification;
import com.university.attendance.model.User;
import com.university.attendance.repository.NotificationRepository;
import com.university.attendance.repository.UserRepository;
import com.university.attendance.util.NotificationSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final NotificationSender notificationSender;

    @Autowired
    public NotificationService(
            NotificationRepository notificationRepository,
            UserRepository userRepository,
            NotificationSender notificationSender) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.notificationSender = notificationSender;
    }

    public Notification createNotification(User user, String title, String message, String type) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setType(type);
        notification.setRead(false);
        notification.setCreatedAt(new Date());
        
        Notification savedNotification = notificationRepository.save(notification);
        
        // Send real-time notification (could be via WebSocket, email, SMS, etc.)
        notificationSender.sendNotification(savedNotification);
        
        return savedNotification;
    }
    
    public List<Notification> getUserNotifications(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        return notificationRepository.findByUser(user);
    }
    
    public List<Notification> getUserUnreadNotifications(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        return notificationRepository.findByUserAndIsRead(user, false);
    }
    
    public Notification markNotificationAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        
        notification.setRead(true);
        notification.setReadAt(new Date());
        
        return notificationRepository.save(notification);
    }
    
    public void markAllNotificationsAsRead(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        List<Notification> unreadNotifications = notificationRepository.findByUserAndIsRead(user, false);
        
        for (Notification notification : unreadNotifications) {
            notification.setRead(true);
            notification.setReadAt(new Date());
            notificationRepository.save(notification);
        }
    }
    
    public long countUnreadNotifications(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        return notificationRepository.countByUserAndIsRead(user, false);
    }
}
