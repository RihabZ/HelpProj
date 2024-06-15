package com.rihab.interventions.dto;

import java.util.Date;

import com.rihab.interventions.entities.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
	
public class ArticleDTO {
	private Long codeArticle;
	
	 private Double qteArticle;

		private String nomArticle;
		private String marqueArticle;
		
	 
}
