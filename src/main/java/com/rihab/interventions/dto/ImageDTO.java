package com.rihab.interventions.dto;

import java.util.Date;

import com.rihab.interventions.entities.Demandeur;
import com.rihab.interventions.entities.Equipement;
import com.rihab.interventions.entities.Intervention;
import com.rihab.interventions.entities.InterventionNature;
import com.rihab.interventions.entities.Technicien;

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
public class ImageDTO {
	private Long idImage;
  private String   path;
}
