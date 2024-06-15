package com.rihab.interventions.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rihab.interventions.dto.UserDTO;
import com.rihab.interventions.entities.Demandeur;
import com.rihab.interventions.entities.Departement;
import com.rihab.interventions.entities.Role;
import com.rihab.interventions.entities.Technicien;
import com.rihab.interventions.entities.User;
import com.rihab.interventions.repos.TechnicienRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public  class TechnicienServiceImpl implements TechnicienService{

	
	@Autowired
	TechnicienRepository technicienRepository;



@Override
public Technicien saveTechnicien(Technicien demandeur)
{
return technicienRepository.save(demandeur);

}

@Override
public Technicien updateTechnicien(Technicien demandeur) {
return technicienRepository.save(demandeur);
}


@Override
public void deleteTechnicien(Technicien demandeur) {
	technicienRepository.delete(demandeur);
}


@Override
public void deleteTechnicienByCode(long code) {
	technicienRepository.deleteById(code);
}


@Override
public Technicien getTechnicien(long code) {
return technicienRepository.findById(code).get();
}


@Override
public List<Technicien> getAllTechniciens() {
return technicienRepository.findAll();
}






@Override
public List<Technicien> findByDepartementCodeDepart(long codeDepart)
{
	return technicienRepository.findByDepartementCodeDepart( codeDepart);
			
}


@Override
public Technicien getTechnicienByUsername(String username) {
    return technicienRepository.findByUserUsername(username);
}


public List<Object[]> countTechniciensByDepartement() {
    return technicienRepository.countByDepartement();
}


@Override  
public Technicien updateResponsable(Long id, String responsable) {
    Optional<Technicien> optionalTechnicien = technicienRepository.findById(id);
    if (optionalTechnicien.isPresent()) {
        Technicien technicien = optionalTechnicien.get();
        if ("O".equals(responsable) || "N".equals(responsable)) {
            technicien.setResponsable(responsable);
            return technicienRepository.save(technicien);
        } else {
            throw new IllegalArgumentException("La valeur de responsable doit être 'O' ou 'N'.");
        }
    } else {
        throw new EntityNotFoundException("Technicien avec l'ID " + id + " non trouvé.");
    }
}







}


