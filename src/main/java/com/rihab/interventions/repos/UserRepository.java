package com.rihab.interventions.repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rihab.interventions.entities.Role;
import com.rihab.interventions.entities.User;



public interface UserRepository extends JpaRepository<User, Long> {

   // Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);

void deleteById(Long userId);

List<User> findByRole(Role role);

long countByRole(Role role);

Object findByEmail(String email);
}