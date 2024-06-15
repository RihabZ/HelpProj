package com.rihab.interventions.service;

import java.util.List;
import java.util.Map;

import com.rihab.interventions.entities.Departement;
import com.rihab.interventions.entities.Equipement;
import com.rihab.interventions.entities.Technicien;

public interface TechnicienService {


Technicien saveTechnicien(Technicien technicien);
Technicien updateTechnicien(Technicien technicien);
void deleteTechnicien(Technicien technicien);
void deleteTechnicienByCode(long code);
 Technicien getTechnicien(long code);
List<Technicien> getAllTechniciens();

List<Technicien> findByDepartementCodeDepart(long codeDepart);
Technicien getTechnicienByUsername(String username);

List<Object[]> countTechniciensByDepartement();

Technicien updateResponsable(Long id, String responsable);
}
