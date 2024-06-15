package com.rihab.interventions.restControllers;

import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.rihab.interventions.entities.Notification;
import com.rihab.interventions.entities.User;
import com.rihab.interventions.repos.UserRepository;

import com.rihab.interventions.service.RealTimeNotificationService;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class NotificationRestController {

	@Autowired
    private RealTimeNotificationService notificationService;

    @Autowired
    private UserRepository userService;
    //niquement les notifications non lues
    @GetMapping("/notifications")
    public List<Notification> getNotifications(Authentication authentication) {
        User user = userService.findByUsername(authentication.getName()).orElse(null);
        if (user != null) {
            return notificationService.getNotificationsForUser(user);
        }
        return List.of();
    }
    
    @GetMapping("/allNotifications")
    public List<Notification> getAllNotifications(Authentication authentication) {
        User user = userService.findByUsername(authentication.getName()).orElse(null);
        if (user != null) {
            return notificationService.getAllNotificationsForUser(user); // Utiliser la nouvelle méthode
        }
        return List.of();
    }
//marquer  notifications comme lues:
    @PutMapping("/markAllAsRead")
    public ResponseEntity<Void> markAllAsRead(Authentication authentication) {
        if (authentication != null && authentication.getName() != null) {
            User user = userService.findByUsername(authentication.getName()).orElse(null);
            if (user != null) {
                notificationService.markAllAsRead(user);
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.status(404).build(); // Retourne 404 si l'utilisateur n'est pas trouvé
    }
    
    /*
    @MessageMapping("/notify")
    @SendTo("/queue/notifications")
    public void sendNotification(NotificationMessage message) {
        realTimeNotificationService.sendNotification(message.getUserId(), message.getMessage());
    }
    */
    @PostMapping("/send")
    public void sendNotification(@RequestParam Long userId, @RequestParam String message, @RequestParam Long idImage) {
        notificationService.sendNotification(userId, message, idImage);
    }
    
    
}
