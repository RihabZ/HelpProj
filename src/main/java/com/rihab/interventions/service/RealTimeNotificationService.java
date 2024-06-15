package com.rihab.interventions.service;


import com.rihab.interventions.entities.Notification;
import com.rihab.interventions.entities.User;
import com.rihab.interventions.repos.NotificationRepository;
import com.rihab.interventions.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class RealTimeNotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sendNotification(Long userId, String message, Long idImage) {
        System.out.println("Starting sendNotification...");
        System.out.println("User ID: " + userId);
        System.out.println("Message: " + message);
        System.out.println("Image ID: " + idImage);
        
        User user = userRepository.findById(userId).orElse(null);
        
        if (user != null) {
            System.out.println("User found: " + user.getUsername());
            
            Notification notification = Notification.builder()
                .message(message)
                .createdAt(new Date())
                .seen(false)
                .user(user)
                .idImage(idImage)
                .build();
            
            notificationRepository.save(notification);
            System.out.println("Notification saved: " + notification.getMessage());
            
            messagingTemplate.convertAndSendToUser(
                user.getUsername(),
                "/queue/notifications",
                notification.getMessage()
            );
            
            System.out.println("Notification sent to user: " + user.getUsername());
        } else {
            System.out.println("User not found with ID: " + userId);
        }
        System.out.println("End of sendNotification.");
    }


    public List<Notification> getNotificationsForUser(User user) {
        return notificationRepository.findByUserAndSeenFalse(user);
    }
//retournera toutes les notifications pour un utilisateur donné.
 public List<Notification> getAllNotificationsForUser(User user) {
        return notificationRepository.findByUser(user); // Nouvelle méthode pour toutes les notifications
    }
    
    public Notification save(Notification notification) {
        return notificationRepository.save(notification);
    }

    public Notification findById(Long id) {
        return notificationRepository.findById(id).orElse(null);
    }

 // Nouvelle méthode pour marquer une notification comme lue
    public void markAllAsRead(User user) {
        if (user != null) {
            List<Notification> unreadNotifications = notificationRepository.findByUserAndSeenFalse(user);
            for (Notification notification : unreadNotifications) {
                notification.setSeen(true);
            }
            notificationRepository.saveAll(unreadNotifications);
        }
    }
    
    
    
    
    
}
