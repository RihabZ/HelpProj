package com.rihab.interventions.service;


import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.rihab.interventions.dto.MonthlyTicketCountDTO;
import com.rihab.interventions.dto.TicketCountDTO;
import com.rihab.interventions.dto.TicketDTO;
import com.rihab.interventions.dto.TicketDurationDTO;
import com.rihab.interventions.dto.TicketStatisticsDTO;
import com.rihab.interventions.entities.Technicien;
//import com.rihab.interventions.dto.TicketUpdateDTO;
import com.rihab.interventions.entities.Ticket;

public interface TicketService {
	
	
	TicketDTO saveTicket(TicketDTO inter) ;
	 TicketDTO updateTicket(TicketDTO updatedTicketDTO);
void deleteTicket(Ticket inter);
 void deleteTicketByCode(String code);
 TicketDTO getTicket(String code);
List<TicketDTO> getAllTickets();

List<Ticket> findByInterDesignation(String desing);
List<Ticket> findByInterDesignationContains(String desing); 


List<Ticket> findByEquipementEqptCode(String eqptCode);
List<Ticket>findByInterventionNatureCode(long code);
//List<Ticket>findByTechnicienCodeTechnicien(long code);

//List<Ticket>findByDemandeurCodeDemandeur(long code);
List<Ticket> findByLoggedInTechnicien();
List<Ticket> findByLoggedInDemandeur();
//List<TicketDTO> getAllTicketDTOs();
//TicketStatisticsDTO getTicketStatistics();
//List<TicketDTO> getPlannedTickets();

Long countTotalTickets();
Long countPendingTickets();
Long countTodoTickets();
Long countDoneTickets();
Long countCancelledTickets();
Long countBlockedTickets();
//TicketDTO updateTicketSelective(String interCode, String interStatut);
TicketDTO updateTicketStatus(String interCode, String newStatus);
List<TicketDTO> getClosedTickets();
//List<TicketDTO> getAllTicketsWithDemandeurDetails();
List<TicketDTO> getArchivedTickets();
List<Ticket> getALLArchivedTickets();
List<Ticket> getAllTickets1();
//void addTicketToCalendar(Ticket ticket);
//List<TicketDTO> extractRealizableTicketInfo();


Long totalTicketsDemandeur();
Long attenteTicketsDemandeur();
Long todoTicketsDemandeur();
Long doneTicketsDemandeur();
Long cancelledTicketsDemandeur();
Long blockedTicketsDemandeur();

Map<String, Long> countTicketsByClient();

Long doneTicketsTechnicien();

//t9aren les durees
List<TicketDurationDTO> getDurationsForTicketsToRealize();

// f chhar 9edch réalisés
 List<MonthlyTicketCountDTO> getRealizedTicketsCountByMonth();
//requête (nombre de tickets par technicien par mois)ll manager 
 //List<TicketCountDTO> getTechnicianTicketStats();
 
 List<Map<String, Object>> countTicketsByTechnician();
 List<Map<String, Object>> countTicketsByTechnicianAndMonth();

 
}