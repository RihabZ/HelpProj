package com.rihab.interventions.repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rihab.interventions.entities.Departement;
import com.rihab.interventions.entities.User;


public interface DepartementRepository extends JpaRepository<Departement, Long> {

	Optional<User> findByNomDepart(String nomDepart);
	

}
