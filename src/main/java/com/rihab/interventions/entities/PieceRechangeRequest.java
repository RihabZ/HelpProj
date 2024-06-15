
package com.rihab.interventions.entities;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "codeDemande")
@Entity
public class PieceRechangeRequest {


	 @Id
	   @GeneratedValue(strategy = GenerationType.IDENTITY)
	   private long codeDemande;
	 
	private String statutDemande;
	private Double quantiteDemande;
	private String etat;
	
	private String autreArt;
	 private double quantitePieceRechange;
	 
	 private String done;
	 private String distingtion;
	 private String lien;
	@ManyToOne
  private Ticket ticket;


	@ManyToOne
  private Article article;

	
}
