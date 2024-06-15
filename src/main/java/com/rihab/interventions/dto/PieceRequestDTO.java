package com.rihab.interventions.dto;

import java.util.Date;

import com.rihab.interventions.entities.Demandeur;
import com.rihab.interventions.entities.Equipement;
import com.rihab.interventions.entities.Intervention;
import com.rihab.interventions.entities.InterventionNature;
import com.rihab.interventions.entities.Technicien;
import com.rihab.interventions.entities.Ticket;

import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToOne;
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
public class PieceRequestDTO {

	  private long codeDemande;
		 
		private String statutDemande;
		private Double quantiteDemande;
		private String etat;
		private String autreArt;
		 private double quantitePieceRechange;
		 
		 private String done;
		 private String distingtion;
		 private String lien;
	  private TicketDTO ticket;
	  private ArticleDTO article;
	 
}
