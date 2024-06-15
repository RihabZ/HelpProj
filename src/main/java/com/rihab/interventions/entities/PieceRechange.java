package com.rihab.interventions.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data 
@NoArgsConstructor 
@AllArgsConstructor
@Entity
public class PieceRechange {
	
	 @EmbeddedId
	    private PieceRechangeId id;

	 @ManyToOne
	 @JoinColumn(referencedColumnName = "EQPT_CODE")
	 @MapsId("eqptCode")
	   private Equipement equipement;

	 @ManyToOne
	    @JoinColumn(referencedColumnName = "codeArticle", insertable = false, updatable = false)
	    @MapsId("codeArticle")
	    private Article article;

		
	    @Column(name = "EQPR_VALEUR_FREQUENCE")
	    private Integer eqprValeurFrequence;

	    @Column(name = "EQPR_UNIT_FREQUENCE", length = 10)
	    private String eqprUnitFrequence;

	    @Column(name = "EQPR_QTE_UE", nullable = false)
	    private Double eqprQte;


	 
}
