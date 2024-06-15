package com.rihab.interventions.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rihab.interventions.entities.Image;

public interface ImageRepository extends JpaRepository<Image , Long> {
}
