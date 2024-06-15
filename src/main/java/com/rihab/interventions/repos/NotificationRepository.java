package com.rihab.interventions.repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rihab.interventions.entities.Notification;
import com.rihab.interventions.entities.User;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

	   List<Notification> findByUserAndSeenFalse(User user);
	   List<Notification> findByUser(User user); 

}
