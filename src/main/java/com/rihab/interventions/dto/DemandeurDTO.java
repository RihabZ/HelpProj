package com.rihab.interventions.dto;

import java.util.Date;

import com.rihab.interventions.entities.Role;

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
public class DemandeurDTO {
	 private String nom;
	    private String prenom;
	    private String email;
	    private String tel;
	    private int  age;
	    private Role role;
private String sexe;
private Date dateEmbauche;

private long codeDemandeur;
private String post;
private long codeClient;
private String username;
private String password;

private String adresse;
private String ville;
private long codePostal;
private String telS;

private String emailSociete;
private Date dateEntree;

}