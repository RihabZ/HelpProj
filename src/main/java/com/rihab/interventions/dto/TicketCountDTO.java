package com.rihab.interventions.dto;

import java.util.Date;
import java.util.UUID;

import com.rihab.interventions.entities.Cause;
import com.rihab.interventions.entities.Demandeur;
import com.rihab.interventions.entities.Equipement;
import com.rihab.interventions.entities.Intervention;
import com.rihab.interventions.entities.InterventionNature;
import com.rihab.interventions.entities.InterventionType;
import com.rihab.interventions.entities.Technicien;
import com.rihab.interventions.entities.Ticket;

import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
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
//TicketCountDTO est conçu spécifiquement pour contenir le résultat agrégé de la
//requête (nombre de tickets par technicien par mois).
public class TicketCountDTO {
    private String technicienNom;
    private String technicienPrenom;
    private int mois;
    private long nombreTickets;
}
