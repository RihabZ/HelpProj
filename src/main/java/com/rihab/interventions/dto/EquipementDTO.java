package com.rihab.interventions.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rihab.interventions.entities.Client;
import com.rihab.interventions.entities.Demandeur;
import com.rihab.interventions.entities.Equipement;
import com.rihab.interventions.entities.Famille;
import com.rihab.interventions.entities.Intervention;
import com.rihab.interventions.entities.InterventionNature;
import com.rihab.interventions.entities.Technicien;
import com.rihab.interventions.entities.Ticket;
import com.rihab.interventions.entities.Type;

import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
	public class EquipementDTO {

		 private String code;
		    private String eqptDesignation;
		    private String eqptId;

		    private double eqptPrix;
		
		    private Date eqptDtAchat; 
		    private String eqptGarantie;

		   
		    private String eqptEnService;

		 
		    private String eqptGarTypeDtRef;


		    private String eqptMachine;

		    private Date eqptDtCreation;

		   
		  
		    private double eqptDureeGarantie;

		  
		    private Date dateFinGarantie;

		    private String siteCode;

		   
		    private String centreCode;


		   
		    private String articleCode;
		    
		    private String eqptLotNumero;

		   
		    private String eqptNumeroSerie;
 
		    private Date dateFabrication;

		   
		    private Date dateInstallation;

		    private Date dateMiseEnService;

		 
		    private String postCode;

		   
		    private String ressCode;

		   
		    private Date dateDemontage;

		   
		    private Date dateRemplacement;

		   
		    private Date dateLivraison;

		    private Type type;

		    private Client client;

		   
		    private Famille  famille;

		  //Image information
		    private Long imageId;
		    private String imagePath;






}
