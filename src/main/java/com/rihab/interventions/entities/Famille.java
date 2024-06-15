package com.rihab.interventions.entities;

import java.util.Date;
import java.util.List;
import java.util.Random;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Famille {
	@Id
    @Column(name = "EQFA_CODE", nullable = false , columnDefinition = "VARCHAR(20)")
    private String eqfaCode;

	
	@PrePersist
    public void generateEqtyCode() {
        if (eqfaCode == null || eqfaCode.isEmpty()) {
        	eqfaCode = generateRandomEqtyCode();
        }
    }

    private String generateRandomEqtyCode() {
        // Générer un code d'équipement aléatoire complet
        Random random = new Random();
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            code.append(characters.charAt(random.nextInt(characters.length())));
        }
        return code.toString();
    }
    @Column(name = "EQFA_LIBELLE", nullable = false,columnDefinition = "VARCHAR(30)")
   private String eqfaLibelle;

    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DT_CRE")
    private Date dtCre;

    @Column(name = "LOGIN_CRE", length = 30)
    private String loginCre;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DT_MAJ")
    private Date dtMaj;

    @Column(name = "LOGIN_MAJ", length = 30)
    private String loginMaj;

	
	@JsonIgnore
	@OneToMany(mappedBy = "famille")
	private List<Equipement> equipements;


	

	@Override
	public String toString() {
		return "Famille [eqfaCode=" + eqfaCode + ", eqfaLibelle=" + eqfaLibelle + ", dtCre=" + dtCre + ", loginCre="
				+ loginCre + ", dtMaj=" + dtMaj + ", loginMaj=" + loginMaj + ", equipements=" + equipements + "]";
	}


	
	
}
