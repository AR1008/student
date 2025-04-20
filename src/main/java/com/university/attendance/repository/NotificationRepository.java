package com.university.attendance.repository;

import com.university.attendance.model.Notification;
import com.university.attendance.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUser(User user);
    List<Notification> findByUserAndIsRead(User user, boolean isRead);
    List<Notification> findByUserAndType(User user, String type);
    Long countByUserAndIsRead(User user, boolean isRead);
}