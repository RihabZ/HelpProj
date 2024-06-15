package com.rihab.interventions.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.rihab.interventions.entities.Technicien;

public interface TechnicienRepository extends JpaRepository<Technicien, Long> {
	

	 
	 List<Technicien> findByDepartementCodeDepart(long codeDepart);

	Technicien findByUserUsername(String username);

	@Query("SELECT t.departement.nomDepart, COUNT(t) FROM Technicien t GROUP BY t.departement")
    List<Object[]> countByDepartement();
    @Query("SELECT t FROM Technicien t")
    List<Technicien> findAllTechnicians();
}