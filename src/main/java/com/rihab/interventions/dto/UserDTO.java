package com.rihab.interventions.dto;

import java.util.Date;

import com.rihab.interventions.entities.Departement;
import com.rihab.interventions.entities.Role;
import com.rihab.interventions.entities.User;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
	
	public class UserDTO {
	@Column(name = "user_id") // Spécifie le nom de la colonne d'identifiant
	private Long id;
	    private String nom;
	    private String prenom;
	    private String email;
	    private String tel;
	    private int  age;
	    private Role role;
private String sexe;

private Date dateNaiss;


private long codeDemandeur;
private String post;
private long codeClient;
private String nomSociete;
private String username;
private String password;

private String nomDepart;

private long codeTechnicien;

private String responsable ;
 private String matricule;
 private String internet;
 private String numeroAbrege;
private long codeDepart;

private long codeManager;
private long codeMagasinier;
private Date dateEmbauche;

private String cin;

private String currentPassword;
private String newPassword;
private String confirmationPassword;


//Image information
private Long imageId;
private String imagePath;

private String token;


	}

