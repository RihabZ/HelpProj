package com.rihab.interventions.dto;

import java.util.Date;

import com.rihab.interventions.entities.Role;

import jakarta.persistence.Column;
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
public class TechnicienDTO {
	 private String nom;
	    private String prenom;
	    private String email;
	    private String tel;
	    private int  age;
	    private Role role;
private String sexe;
private Date dateEmbauche;

private String responsable ;

   private String matricule;

   private String internet;
   

   private String numeroAbrege;
   

}