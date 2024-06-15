package com.rihab.interventions.entities;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor 
@AllArgsConstructor
@Builder
@Entity
public class Article {

		@Id
	   @GeneratedValue(strategy = GenerationType.IDENTITY)
		 private Long codeArticle;
	
		private String nomArticle;
		private String marqueArticle;
		 private Double qteArticle;
		 
		
}
