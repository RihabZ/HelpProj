package com.rihab.interventions.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rihab.interventions.entities.Client;
import com.rihab.interventions.entities.User;

public interface ClientRepository extends JpaRepository<Client, Long> {

	Client findByNomSociete(String nomSociete);


	

}
