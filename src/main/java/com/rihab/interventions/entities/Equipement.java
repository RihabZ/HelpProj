package com.rihab.interventions.entities;

import java.util.Date;
import java.util.List;
import java.util.Random;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "eqptCode")
public class Equipement {
    @Id
    @Column(name = "EQPT_CODE", columnDefinition = "VARCHAR(5)")
    private String eqptCode;

    @Column(name = "EQPT_DESIGNATION", columnDefinition = "VARCHAR(50)", nullable = false)
    private String eqptDesignation;

 

 	@PrePersist
    public void generateEqtyCode() {
        if (eqptCode == null || eqptCode.isEmpty()) {
        	eqptCode = generateRandomEqtyCode();
        }
    }

    private String generateRandomEqtyCode() {
        // Générer un code d'équipement aléatoire complet
        Random random = new Random();
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            code.append(characters.charAt(random.nextInt(characters.length())));
        }
        return code.toString();
    }
	
    
    
@Column(name = "EQPT_IDENTIFIANT", columnDefinition = "VARCHAR(50)")
    private String eqptId;

    private double eqptPrix;
    
    @Column(name = "EQPT_DT_ACHAT")
    private Date eqptDtAchat;
    
    @Column(name = "EQPT_GARANTIE",columnDefinition = "VARCHAR(1) DEFAULT 'O'")
    private String eqptGarantie;

    @Column(name = "EQPT_CRITIQUE", columnDefinition = "VARCHAR(1) DEFAULT 'N'")
    private String eqptCritique;

    @Column(name = "EQPT_LOCALISATION", columnDefinition = "VARCHAR(200)")
    private String eqptLocalisation;

    @Column(name = "EQPT_EN_SERVICE", columnDefinition = "VARCHAR(1) DEFAULT 'O'")
    private String eqptEnService;

    @Column(name = "EQPT_GAR_TYPE_DT_REF")
    private String eqptGarTypeDtRef;

    @Column(name = "EQPT_USER_CB1", columnDefinition = "VARCHAR(1) DEFAULT '0'")
    private String eqptUserCB1;

    @Column(name = "EQPT_MACHINE", columnDefinition = "VARCHAR(1) DEFAULT 'N'")
    private String eqptMachine;

    private Date eqptDtCreation;

    @Column(name = "LOGIN_CRE", columnDefinition = "VARCHAR(20)")
    private String eqptLoginCreation;

    private Date eqptDtMaj;

    @Column(name = "LOGIN_MAJ", columnDefinition = "VARCHAR(30)")
    private String eqptLoginMaj;

    private double eqptDureeGarantie;

    @Column(name = "EQPT_DT_FIN_GARANTIE")
    private Date dateFinGarantie;

  
    @Column(name = "EQPT_LOT_NUMERO", columnDefinition = "VARCHAR(20)")
    private String eqptLotNumero;

    @Column(name = "EQPT_NUMERO_SERIE", columnDefinition = "VARCHAR(20)")
    private String eqptNumeroSerie;

    @Column(name = "EQPT_DT_FABRICATION")
    private Date dateFabrication;

    @Column(name = "EQPT_DT_INSTALLATION")
    private Date dateInstallation;

    @Column(name = "EQPT_DT_MISE_EN_SERVICE")
    private Date dateMiseEnService;

   
    @Column(name = "EQPT_DT_DEMONTAGE")
    private Date dateDemontage;

    @Column(name = "EQPT_DT_REMPLACEMENT")
    private Date dateRemplacement;

    @Column(name = "EQPT_DT_LIVRAISON")
    private Date dateLivraison;

    @ManyToOne
    private Type type;

    @ManyToOne
    private Client client;
    @JsonIgnore
    @OneToMany(mappedBy = "equipement")
    private List<Ticket> tickets;

    @ManyToOne
    private Famille famille;

   
   
}
