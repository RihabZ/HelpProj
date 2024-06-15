package com.rihab.interventions.dto;

import java.util.Date;

import com.rihab.interventions.entities.Demandeur;
import com.rihab.interventions.entities.Equipement;
import com.rihab.interventions.entities.Intervention;
import com.rihab.interventions.entities.InterventionNature;
import com.rihab.interventions.entities.Role;
import com.rihab.interventions.entities.Technicien;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MagasinierDTO {
	 private String nom;
	    private String prenom;
	    private String email;
	    private String tel;
	    private int  age;
	    private Role role;
private String sexe;
private Date dateEmbauche;

private long codeMagasinier;
private long idUser;
private String username;
private String password;
}
