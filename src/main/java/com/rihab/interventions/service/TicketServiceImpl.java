package com.rihab.interventions.service;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.rihab.interventions.dto.DemandeurDTO;
import com.rihab.interventions.dto.EquipementDTO;
import com.rihab.interventions.dto.MonthlyTicketCountDTO;
import com.rihab.interventions.dto.TicketCountDTO;
import com.rihab.interventions.dto.TicketDTO;
import com.rihab.interventions.dto.TicketDurationDTO;
import com.rihab.interventions.dto.TicketStatisticsDTO;
import com.rihab.interventions.dto.UserDTO;
import com.rihab.interventions.entities.CalendarEvent;
import com.rihab.interventions.entities.Client;
import com.rihab.interventions.entities.Demandeur;
import com.rihab.interventions.entities.Equipement;
import com.rihab.interventions.entities.Intervention;
import com.rihab.interventions.entities.Manager;
import com.rihab.interventions.entities.Technicien;
import com.rihab.interventions.entities.Ticket;
import com.rihab.interventions.entities.User;
import com.rihab.interventions.repos.ClientRepository;
import com.rihab.interventions.repos.TechnicienRepository;
import com.rihab.interventions.repos.TicketRepository;

import com.rihab.interventions.util.EmailService;


@Service
public class TicketServiceImpl implements TicketService {
	
	@Autowired
	TicketRepository ticketRepository;
	@Autowired
	DemandeurService demandeurService;
	@Autowired
	TechnicienService technicienService;
	@Autowired
	ManagerService managerService;
	@Autowired
    private EmailService emailService;
	
	@Autowired
	private AuthenticationService userManagerService;
	@Autowired
	private CalendarService calendarService;
	@Autowired
	ClientRepository clientRepository;
	
	@Autowired
	private RealTimeNotificationService realTimeNotificationService;

	 
	@Autowired
	ModelMap modelMapper;
	@Autowired
	TechnicienRepository technicienRepository;
/*
@Override
public TicketDTO saveTicket(TicketDTO inter)
{
	//inter.setInterCode(UUID.randomUUID());
return toTicketDTO(ticketRepository.save(toTicket(inter)));

}

*/
/*
public List<TicketDTO> getAllTicketsWithDemandeurDetails() {
    List<Ticket> tickets = ticketRepository.findAll();
    List<TicketDTO> ticketDTOs = new ArrayList<>();

    for (Ticket ticket : tickets) {
        TicketDTO ticketDTO = toTicketDTO(ticket);
        Demandeur demandeur = ticket.getDemandeur();
        if (demandeur != null) {
            UserDTO demandeurDTO = toUserDTO(demandeur.getUser()); // Convertir le demandeur en DTO
            ticketDTO.setDemandeurr(demandeurDTO);
        }
        ticketDTOs.add(ticketDTO);
    }

    return ticketDTOs;
}
public UserDTO toUserDTO(User user) {
    UserDTO userDTO = new UserDTO();
    userDTO.setId(user.getId());
    userDTO.setNom(user.getNom());
    userDTO.setPrenom(user.getPrenom());
    userDTO.setEmail(user.getEmail());
    userDTO.setTel(user.getTel());
    userDTO.setAge(user.getAge());
    userDTO.setRole(user.getRole());
    userDTO.setSexe(user.getSexe());
    userDTO.setCodeDemandeur(user.getDemandeur().getCodeDemandeur());
    userDTO.setPost(user.getDemandeur().getPost());
    userDTO.setAdresse(user.getDemandeur().getClient().getAdresse());
    userDTO.setCodePostal(user.getDemandeur().getClient().getCodePostal());
    userDTO.setVille(user.getDemandeur().getClient().getVille());
    userDTO.setEmailSociete(user.getDemandeur().getClient().getEmailSociete());
    userDTO.setTell(user.getDemandeur().getClient().getTel());
    userDTO.setUsername(user.getUsername());
    userDTO.setCodeClient(user.getDemandeur().getClient().getCodeClient());
    userDTO.setDateEmbauche(user.getDateEmbauche());

    // Autres attributs à convertir selon vos besoins

    return userDTO;
}
*/
@Override
public TicketDTO saveTicket(TicketDTO ticketDTO) {
    // Get the authenticated user
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || !authentication.isAuthenticated()) {
        // Handle unauthenticated users or return an error
        return null;
    }

    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    String username = userDetails.getUsername();

    Demandeur demandeur = demandeurService.getDemandeurByUsername(username);

    if (demandeur == null) {
        // Handle the case where the demandeur doesn't exist
        return null;
    }

    // Convert DTO to entity
    Ticket ticket = toTicket(ticketDTO);
    ticket.setIntervention(ticket.getIntervention()); 
    ticket.setDemandeur(demandeur);
    ticket.setInterCode("I24-" + generateShortUUID().substring(0, 4));

    // Save the entity
    Ticket savedTicket = ticketRepository.save(ticket);

    // Send email to all managers
    List<UserDTO> managers = userManagerService.getAllManagers();
    for (UserDTO manager : managers) {
        emailService.sendNewTicketEmail(manager.getEmail(), demandeur.getUser().getNom() + " " + demandeur.getUser().getPrenom(), ticketDTO.getInterDesignation());
  
    // Create a notification
 //   notificationService.createNotification("Nouveau ticket créé par " + demandeur.getUser().getNom() + " " + demandeur.getUser().getPrenom() + ": " + ticketDTO.getInterDesignation(), manager.getUser());
   // notificationService.sendNotification(manager.getId(), "Nouveau ticket créé par " + demandeur.getUser().getNom());
     // Include image ID in the message
     // Obtenez l'ID de l'image du demandeur
        Long demandeurImageId = demandeur.getUser().getImage() != null ? demandeur.getUser().getImage().getIdImage() : null;
        String notificationMessage = "Un nouveau ticket est créé par " + demandeur.getUser().getNom() + " " + demandeur.getUser().getPrenom();

        // Envoyez la notification avec l'ID de l'image du demandeur
        realTimeNotificationService.sendNotification(manager.getId(), notificationMessage, demandeurImageId);
    }


    // Convert the saved entity to DTO
    return toTicketDTO(savedTicket);
}

private String generateShortUUID() {
    UUID uuid = UUID.randomUUID();
    long lsb = uuid.getLeastSignificantBits();
    long msb = uuid.getMostSignificantBits();
    return Long.toHexString(msb ^ lsb);
}
/*	
	@Override
	public List<TicketDTO> getAllTicketDTOs() {
	    List<Ticket> tickets = ticketRepository.findAll();
	    return tickets.stream()
	            .map(this::mapToTicketDTO)
	            .collect(Collectors.toList());
	}

	private TicketDTO mapToTicketDTO(Ticket ticket) {
	    TicketDTO dto = new TicketDTO();
	    dto.setInterCode((UUID.randomUUID()));
	    dto.setInterDesignation(ticket.getInterDesignation());
	    dto.setEquipement(mapToEquipementDTO(ticket.getEquipement()));
	   
	    // Map other fields if needed
	    return dto;
	}


public EquipementDTO mapToEquipementDTO(Equipement equipement) {
    EquipementDTO dto = new EquipementDTO();
    dto.setCode(equipement.getEqptCode());
    dto.setArticleCode(equipement.getArticleCode());
    dto.setCentreCode(equipement.getCentreCode());
   dto.setDateDemontage(equipement.getDateDemontage());
   dto.setDateFabrication(equipement.getDateFabrication());
   dto.setDateFinGarantie(equipement.getDateFinGarantie());
   dto.setDateInstallation(equipement.getDateInstallation());
   dto.setDateMiseEnService(equipement.getDateMiseEnService());
   dto.setDateRemplacement(equipement.getDateRemplacement());
   dto.setDesignation(equipement.getEqptDesignation());
   dto.setFamille(equipement.getFamille());
   dto.setEqptDtAchat(equipement.getEqptDtAchat());
   dto.setEqptDtCreation(equipement.getEqptDtCreation());
  dto.setDateLivraison(equipement.getDateLivraison());
   dto.setEqptGarantie(equipement.getEqptGarantie());
   dto.setEqptDureeGarantie(equipement.getEqptDureeGarantie());
   dto.setEqptPrix(equipement.getEqptPrix());
   dto.setEqptMachine(equipement.getEqptMachine());
dto.setType(equipement.getType());
dto.setPostCode(equipement.getPostCode());
dto.setRessCode(equipement.getRessCode());
dto.setSiteCode(equipement.getSiteCode());
dto.setEqptEnService(equipement.getEqptEnService());
dto.setEqptId(equipement.getEqptId());
dto.setEqptGarTypeDtRef(equipement.getEqptGarTypeDtRef());
dto.setEqptMachine(equipement.getEqptMachine());
// Map other fields
    return dto;
}
	
	
	*/
/*	
@Override
public TicketDTO updateTicket(TicketDTO inter) {
	return toTicketDTO(ticketRepository.save(toTicket(inter)));
}

*/
/*@Override
public TicketDTO updateTicket(TicketDTO updatedTicketDTO) {
    // Vérifiez d'abord si le ticket à mettre à jour existe dans la base de données
    Optional<Ticket> optionalTicket = ticketRepository.findByInterCode(updatedTicketDTO.getInterCode());

    if (optionalTicket.isPresent()) {
        Ticket existingTicket = optionalTicket.get();

        // Mettez à jour les champs du ticket existant avec les nouvelles valeurs
        existingTicket.setInterDesignation(updatedTicketDTO.getInterDesignation());
        existingTicket.setInterPriorite(updatedTicketDTO.getInterPriorite());
        existingTicket.setInterStatut(updatedTicketDTO.getInterStatut());
        existingTicket.setMachineArret(updatedTicketDTO.getMachineArret());
        existingTicket.setDateArret(updatedTicketDTO.getDateArret());
        existingTicket.setDureeArret(updatedTicketDTO.getDureeArret());
        existingTicket.setDateCreation(updatedTicketDTO.getDateCreation());
        existingTicket.setDatePrevue(updatedTicketDTO.getDatePrevue());
        existingTicket.setDureePrevue(updatedTicketDTO.getDureePrevue());
        existingTicket.setDescription(updatedTicketDTO.getDescription());
        existingTicket.setSousContrat(updatedTicketDTO.getSousContrat());
        existingTicket.setSousGarantie(updatedTicketDTO.getSousGarantie());
        existingTicket.setEquipement(updatedTicketDTO.getEquipement());
        existingTicket.setDemandeur(updatedTicketDTO.getDemandeur());
        existingTicket.setTechnicien(updatedTicketDTO.getTechnicien());
        existingTicket.setInterventionNature(updatedTicketDTO.getInterventionNature());
     // Enregistrez les modifications dans la base de données
        Ticket updatedTicket = ticketRepository.save(existingTicket);

        // Convertissez le ticket mis à jour en DTO et renvoyez-le
        return toTicketDTO(updatedTicket);
    } else {
        // Gérez le cas où le ticket n'existe pas dans la base de données
        return null; // Ou lancez une exception appropriée
    }
}

*/
//else {
        // Gérez le cas où le ticket n'existe pas dans la base de données
        //return null; // Ou lancez une exception appropriée
    //}
//}

   
 /*
 //Méthode pour extraire les informations des tickets réalisables
public List<TicketDTO> extractRealizableTicketInfo() {
    List<Ticket> tickets = ticketRepository.findAll();
    return tickets.stream()
            .filter(ticket -> "A réaliser".equals(ticket.getInterStatut()))
            .map(this::mapToTicketDTO)
            .collect(Collectors.toList());
}

// Mapper Ticket à TicketDTO
private TicketDTO mapToTicketDTO(Ticket ticket) {
    // Implémentez la logique pour mapper les champs du ticket à TicketDTO
    // Exemple:
    TicketDTO ticketDTO = new TicketDTO();
    ticketDTO.setInterCode(ticket.getInterCode());
    ticketDTO.setInterDesignation(ticket.getInterDesignation());
    ticketDTO.setDatePrevue(ticket.getDatePrevue());
    // Ajoutez d'autres champs si nécessaire
    return ticketDTO;
}

*/


    
    
/*
existingTicket.getIntervention().setDateCloture(updatedTicketDTO.getDateCloture());
existingTicket.getIntervention().setDescriptionPanne(updatedTicketDTO.getDescriptionPanne());
existingTicket.getIntervention().setDtRealisation(updatedTicketDTO.getDtRealisation());
existingTicket.getIntervention().setDureeRealisation(updatedTicketDTO.getDureeRealisation());
existingTicket.getIntervention().setCompteRendu(updatedTicketDTO.getCompteRendu());
existingTicket.getIntervention().setInterventionObservation(updatedTicketDTO.getInterventionObservation());
existingTicket.getIntervention().setInterMtHebergement(updatedTicketDTO.getInterMtHebergement());
existingTicket.getIntervention().setInterMtDeplacement(updatedTicketDTO.getInterMtDeplacement());
existingTicket.getIntervention().setDifficulté(updatedTicketDTO.getDifficulté());
existingTicket.getIntervention().setInterventionType(updatedTicketDTO.getInterventionType());
existingTicket.getIntervention().setCause(updatedTicketDTO.getCause());
*/


/*
@Override
public TicketDTO updateTicketSelective(String interCode, String interStatut) {
    Ticket ticket = ticketRepository.findByInterCode(interCode)
    		.orElseThrow();

    if (interStatut != null) {
        ticket.setInterStatut(interStatut);
    }
    // Vous pouvez ajouter d'autres champs à mettre à jour sélectivement ici

    return toTicketDTO(ticketRepository.save(ticket));
}
*/
@Override
public TicketDTO updateTicket(TicketDTO updatedTicketDTO) {
    System.out.println("Starting updateTicket...");
    System.out.println("Ticket Code: " + updatedTicketDTO.getInterCode());

    Optional<Ticket> optionalTicket = ticketRepository.findByInterCode(updatedTicketDTO.getInterCode());

    if (!optionalTicket.isPresent()) {
        System.out.println("Ticket not found with code: " + updatedTicketDTO.getInterCode());
        return null;
    }

    Ticket existingTicket = optionalTicket.get();
    String oldStatut = existingTicket.getInterStatut();

    // Mise à jour des champs du ticket
    if (updatedTicketDTO.getInterDesignation() != null) existingTicket.setInterDesignation(updatedTicketDTO.getInterDesignation());
    if (updatedTicketDTO.getInterPriorite() != null) existingTicket.setInterPriorite(updatedTicketDTO.getInterPriorite());
    if (updatedTicketDTO.getInterStatut() != null) existingTicket.setInterStatut(updatedTicketDTO.getInterStatut());
    if (updatedTicketDTO.getMachineArret() != null) existingTicket.setMachineArret(updatedTicketDTO.getMachineArret());
    if (updatedTicketDTO.getDateArret() != null) existingTicket.setDateArret(updatedTicketDTO.getDateArret());
    if (updatedTicketDTO.getDureeArret() != null) existingTicket.setDureeArret(updatedTicketDTO.getDureeArret());
    if (updatedTicketDTO.getDateCreation() != null) existingTicket.setDateCreation(updatedTicketDTO.getDateCreation());
    if (updatedTicketDTO.getDatePrevue() != null) existingTicket.setDatePrevue(updatedTicketDTO.getDatePrevue());
    if (updatedTicketDTO.getDureePrevue() != null) existingTicket.setDureePrevue(updatedTicketDTO.getDureePrevue());
    if (updatedTicketDTO.getDescription() != null) existingTicket.setDescription(updatedTicketDTO.getDescription());
    if (updatedTicketDTO.getSousContrat() != null) existingTicket.setSousContrat(updatedTicketDTO.getSousContrat());
    if (updatedTicketDTO.getSousGarantie() != null) existingTicket.setSousGarantie(updatedTicketDTO.getSousGarantie());
    if (updatedTicketDTO.getEquipement() != null) existingTicket.setEquipement(updatedTicketDTO.getEquipement());
    if (updatedTicketDTO.getDemandeur() != null) existingTicket.setDemandeur(updatedTicketDTO.getDemandeur());
    if (updatedTicketDTO.getInterventionNature() != null) existingTicket.setInterventionNature(updatedTicketDTO.getInterventionNature());
    if (updatedTicketDTO.getTechnicien() != null) {
        existingTicket.setTechnicien(updatedTicketDTO.getTechnicien());
    }

    Ticket updatedTicket = ticketRepository.save(existingTicket);
    System.out.println("Ticket updated and saved.");

    if ("A réaliser".equals(updatedTicketDTO.getInterStatut()) && !"A réaliser".equals(oldStatut)) {
        Technicien technicien = existingTicket.getTechnicien();
        if (technicien != null && technicien.getUser() != null) {
            System.out.println("Technicien found: " + technicien.getUser().getUsername());

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                System.out.println("User is not authenticated");
                return null;
            }

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();

            Manager manager = managerService.findByUsername(username);
            if (manager == null) {
                System.out.println("Manager not found for username: " + username);
                return null;
            }

            Long managerImageId = manager.getUser().getImage() != null ? manager.getUser().getImage().getIdImage() : null;
            String notificationMessage = "Vous avez été assigné à un nouveau ticket " + existingTicket.getInterCode() + " par " + manager.getUser().getNom() + " " + manager.getUser().getPrenom();
            
            System.out.println("Sending notification to technicien with code: " + technicien.getCodeTechnicien());
            System.out.println("Notification message: " + notificationMessage);
            System.out.println("Manager image ID: " + managerImageId);

            realTimeNotificationService.sendNotification(technicien.getUser().getId(), notificationMessage, managerImageId);
            System.out.println("Notification sent successfully.");
        }
    }

    return toTicketDTO(updatedTicket);
}


@Override
public void deleteTicket(Ticket inter) {
	ticketRepository.delete(inter);
}


@Override
public void deleteTicketByCode(String code) {
	ticketRepository.deleteById(code);
}


@Override
public TicketDTO getTicket(String code) {
	return toTicketDTO(ticketRepository.findById(code).get());
}


@Override
public List<TicketDTO> getAllTickets() {
return ticketRepository.findAll().stream()
		.map(this::toTicketDTO)
		.collect(Collectors.toList());
}



@Override
public List<Ticket> findByInterDesignation(String desing)
{
return ticketRepository.findByInterDesignation(desing);
}
@Override
public List<Ticket> findByInterDesignationContains(String desing)
{
return ticketRepository.findByInterDesignationContains(desing);
}




@Override
public List<Ticket> findByEquipementEqptCode(String eqptCode)
{
return ticketRepository.findByEquipementEqptCode( eqptCode);}


@Override
public List<Ticket> findByInterventionNatureCode(long code)
{
return ticketRepository.findByInterventionNatureCode( code);

}


/*
@Override
public List<Ticket> findByTechnicienCodeTechnicien(long codeTechnicien)
{
return ticketRepository.findByTechnicienCodeTechnicien( codeTechnicien);

}
*/@Override
public List<Ticket> findByLoggedInTechnicien() {
    // Obtenir l'utilisateur connecté
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    // Vérifier si l'utilisateur est authentifié et est un utilisateur avec les rôles appropriés
    if (authentication != null && authentication.isAuthenticated()) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // Récupérer les détails du technicien à partir de userDetails
        String username = userDetails.getUsername();
        Technicien technicien = technicienService.getTechnicienByUsername(username);

        // Vérifier si le technicien existe
        if (technicien != null) {
            // Obtenez la date actuelle
            LocalDate currentDate = LocalDate.now();

            // Calculez la date limite pour les tickets non archivés (date actuelle - 2 mois)
            LocalDate limitDate = currentDate.minusMonths(2);

            // Convertissez la limite de date en objet Date
            Date limitDateAsDate = Date.from(limitDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

            // Récupérer tous les tickets attribués à ce technicien avec la date de clôture non archivée
            List<Ticket> tickets = ticketRepository.findAllByTechnicienCodeTechnicien(technicien.getCodeTechnicien(), limitDateAsDate);

            // Filtrer les tickets avec le statut "réalisé" ou "à réaliser"
            List<Ticket> filteredTickets = tickets.stream()
                    .filter(ticket -> ticket.getInterStatut().equals("Réalisé") || ticket.getInterStatut().equals("A réaliser"))
                    .collect(Collectors.toList());

            return filteredTickets;
        }
    }
    // Si aucune condition n'est remplie ou si le technicien n'existe pas, retourner une liste vide
    return new ArrayList<>();
}


@Override
public TicketDTO updateTicketStatus(String interCode, String interStatut) {
    Optional<Ticket> optionalTicket = ticketRepository.findByInterCode(interCode);

    if (optionalTicket.isPresent()) {
        Ticket existingTicket = optionalTicket.get();
        existingTicket.setInterStatut(interStatut);
        Ticket updatedTicket = ticketRepository.save(existingTicket);
        return toTicketDTO(updatedTicket);
    } else {
        // Gérer le cas où le ticket n'existe pas
        return null; // Ou lancez une exception appropriée
    }
}

@Override
public List<Ticket> findByLoggedInDemandeur() {
    // Obtenir l'utilisateur connecté
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    // Vérifier si l'utilisateur est authentifié et est un utilisateur avec les rôles appropriés
    if (authentication != null && authentication.isAuthenticated()) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // Récupérer les détails du demandeur à partir de userDetails
        String username = userDetails.getUsername(); // Supposons que le nom d'utilisateur soit l'identifiant du demandeur
        Demandeur demandeur = demandeurService.getDemandeurByUsername(username); // Supposez que vous avez une méthode dans le service pour rechercher le demandeur par nom d'utilisateur

        // Vérifier si le demandeur existe
        if (demandeur != null) {
            // Obtenez la date actuelle
            LocalDate currentDate = LocalDate.now();

            // Calculez la date limite pour les tickets récents (date actuelle - 2 mois)
            LocalDate limitDate = currentDate.minusMonths(2);

            // Convertissez la limite de date en objet Date
            Date limitDateAsDate = Date.from(limitDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

            // Récupérez tous les tickets récents associés à ce demandeur
            return ticketRepository.findAllByDemandeurCodeDemandeur(demandeur.getCodeDemandeur(), limitDateAsDate);
        }
    }
    // Si aucune condition n'est remplie ou si le demandeur n'existe pas, retourner une liste vide
    return new ArrayList<>();
}

	
	public Ticket toTicket(TicketDTO request) {
		String interCode = "I24-" + generateShortUUID().substring(0, 4); // Tronquer pour garantir que la longueur totale ne dépasse pas 10 caractères

		// Créer une nouvelle instance d'intervention
	    Intervention intervention = new Intervention();
		// Utilisation de interCode dans le reste du code
	    return Ticket.builder()
	            .interCode(interCode)
	            .intervention(intervention)
	            .interDesignation(request.getInterDesignation())
	            .interPriorite(request.getInterPriorite())
	            .interStatut(request.getInterStatut())
	            .machineArret(request.getMachineArret())
	            .dateArret(request.getDateArret())
	            .dureeArret(request.getDureeArret())
	            .dateCreation(request.getDateCreation())
	            .datePrevue(request.getDatePrevue())
	            .dureePrevue(request.getDureePrevue())
	            .description(request.getDescription())
	            .sousContrat(request.getSousContrat())
	            .sousGarantie(request.getSousGarantie())
	           
	           //.intervention(request.getIntervention())
	            .equipement(request.getEquipement())
	            .demandeur(request.getDemandeur())
	            .technicien(request.getTechnicien())
	            .interventionNature(request.getInterventionNature())
	            // Map other fields if needed
	            .build();
	}

	public TicketDTO toTicketDTO(Ticket request) {
	    TicketDTO.TicketDTOBuilder builder = TicketDTO.builder()
	          
	    		.interCode(request.getInterCode())
	            .interDesignation(request.getInterDesignation())
	            .interPriorite(request.getInterPriorite())
	            .interStatut(request.getInterStatut())
	            .machineArret(request.getMachineArret())
	            .dateArret(request.getDateArret())
	            .dureeArret(request.getDureeArret())
	            .dateCreation(request.getDateCreation())
	            .datePrevue(request.getDatePrevue())
	            .dureePrevue(request.getDureePrevue())
	            .description(request.getDescription())
	            .sousContrat(request.getSousContrat())
	            .sousGarantie(request.getSousGarantie())
	           
	            .intervention(request.getIntervention())
	            .equipement(request.getEquipement())
	            .demandeur(request.getDemandeur())
	            .technicien(request.getTechnicien())
	            .interventionNature(request.getInterventionNature());
	    // Map other fields if needed
	            
	    return builder.build();
	}
	
//dash bord manager:
	    @Override
	    public Long countTotalTickets() {
	        return ticketRepository.count();
	    }

	    @Override
	    public Long countPendingTickets() {
	        return ticketRepository.countByInterStatut("en attente");
	    }

	    @Override
	    public Long countTodoTickets() {
	        return ticketRepository.countByInterStatut("à réaliser");
	    }

	    @Override
	    public Long countDoneTickets() {
	        return ticketRepository.countByInterStatut("réalisé");
	    }

	    @Override
	    public Long countCancelledTickets() {
	        return ticketRepository.countByInterStatut("annulé");
	    }

	    @Override
	    public Long countBlockedTickets() {
	        return ticketRepository.countByInterStatut("bloqué");
	    }
/*
	    @Override
	    public List<TicketDTO> getPlannedTickets() {
	        Date currentDate = new Date();
	        return ticketRepository.findByDatePrevueAfter(currentDate).stream()
	                .map(ticket -> {
	                    TicketDTO ticketDTO = toTicketDTO(ticket);
	                    ticketDTO.setInterCode(ticket.getInterCode());
	                    ticketDTO.setInterDesignation(ticket.getInterDesignation());
	                    ticketDTO.setDatePrevue(ticket.getDatePrevue());
	                    ticketDTO.setDureePrevue(ticket.getDureePrevue());
	                    return ticketDTO;
	                })
	                .collect(Collectors.toList());
	    }
*/
	   /* 
	    public List<Ticket> findTicketsByDatePrevue(Date datePrevue) {
	        // Convertir Date en LocalDateTime
	        LocalDateTime startDateTime = datePrevue.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
	        LocalDateTime endDateTime = startDateTime.plusDays(1);

	        // Convertir LocalDateTime en Date
	        Date startDate = Date.from(startDateTime.atZone(ZoneId.systemDefault()).toInstant());
	        Date endDate = Date.from(endDateTime.atZone(ZoneId.systemDefault()).toInstant());

	        return ticketRepository.findByDatePrevueGreaterThanEqualAndDatePrevueLessThanEqual(startDate, endDate);
	    }

	    @Scheduled(fixedRate = 60000) // Exécution toutes les minutes
	    public void addNewTicketsToCalendar() {
	        Date currentDate = new Date();
	        List<Ticket> newTickets = findTicketsByDatePrevue(currentDate);
	        calendarService.addNewTicketsToCalendar(newTickets);
	    }

	  */

	    
	    @Override
	    public List<TicketDTO> getClosedTickets() {
	    	
	    	// Obtenir l'utilisateur connecté
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

	        // Vérifier si l'utilisateur est authentifié et est un utilisateur avec les rôles appropriés
	        if (authentication != null && authentication.isAuthenticated()) {
	            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

	            // Récupérer les détails du demandeur à partir de userDetails
	            String username = userDetails.getUsername(); // Supposons que le nom d'utilisateur soit l'identifiant du demandeur
	            Demandeur demandeur = demandeurService.getDemandeurByUsername(username); // Supposez que vous avez une méthode dans le service pour rechercher le demandeur par nom d'utilisateur
	
	         // Vérifier si le demandeur existe
	            if (demandeur != null) {
	    	
	    	
	        // Obtenez la date actuelle
	        LocalDate currentDate = LocalDate.now();

	        // Calculez la date limite pour les tickets clôturés (date de clôture + 2 mois)
	        LocalDate limitDate = currentDate.minusMonths(2);

	        // Convertissez la limite de date en objet Date
	        Date limitDateAsDate = Date.from(limitDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

	        // Récupérez tous les tickets avec une intervention clôturée avant la limite de date
	        List<Ticket> closedTickets = ticketRepository.findAllByDemandeurCodeDemandeurAndInterventionDateClotureBefore(demandeur.getCodeDemandeur(),limitDateAsDate);

	        // Convertissez les tickets en DTO
	        List<TicketDTO> closedTicketsDTO = closedTickets.stream()
	                .map(this::toTicketDTO)
	                .collect(Collectors.toList());

	        return closedTicketsDTO;
	    }

	    
	    
	

	        }
			return null;
	        
	    }

	    @Override
	    public List<TicketDTO> getArchivedTickets() {
	        // Obtenir l'utilisateur connecté
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

	        // Vérifier si l'utilisateur est authentifié et est un utilisateur avec les rôles appropriés
	        if (authentication != null && authentication.isAuthenticated()) {
	            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

	            // Récupérer les détails du technicien à partir de userDetails
	            String username = userDetails.getUsername();
	            Technicien technicien = technicienService.getTechnicienByUsername(username);

	            // Vérifier si le technicien existe
	            if (technicien != null) {
	                // Obtenez la date actuelle
	                LocalDate currentDate = LocalDate.now();

	                // Calculez la date limite pour les tickets clôturés (date de clôture + 2 mois)
	                LocalDate limitDate = currentDate.minusMonths(2);

	                // Convertissez la limite de date en objet Date
	                Date limitDateAsDate = Date.from(limitDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

	                // Récupérez tous les tickets clôturés dont la date de clôture de l'intervention a dépassé la limite de date
	                List<Ticket> closedTickets = ticketRepository.findAllByTechnicienCodeTechnicienAndInterventionDateClotureBefore(technicien.getCodeTechnicien(), limitDateAsDate);

	                // Convertissez les tickets en DTO
	                List<TicketDTO> closedTicketsDTO = closedTickets.stream()
	                        .map(this::toTicketDTO)
	                        .collect(Collectors.toList());

	                return closedTicketsDTO;
	            }
	        }
	        // Si aucune condition n'est remplie ou si le technicien n'existe pas, retourner une liste vide
	        return new ArrayList<>();
	    }

	    
	    @Override
	    public List<Ticket> getALLArchivedTickets() {
	        // Obtenez la date actuelle
	        LocalDate currentDate = LocalDate.now();

	        // Calculez la date limite pour les tickets archivés (date de clôture + 2 mois)
	        LocalDate limitDate = currentDate.minusMonths(2);

	        // Convertissez la limite de date en objet Date
	        Date limitDateAsDate = Date.from(limitDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

	        // Récupérez tous les tickets archivés dont la date de clôture de l'intervention a dépassé la limite de date
	        List<Ticket> archivedTickets = ticketRepository.findAllArchivedByInterventionDateClotureBefore(limitDateAsDate);

	        return archivedTickets;
	    }
  
	    
	    
	    @Override
	    public List<Ticket> getAllTickets1() {
	        // Obtenez la date actuelle
	        LocalDate currentDate = LocalDate.now();

	        // Calculez la date limite pour les tickets non archivés (date de clôture + 2 mois)
	        LocalDate limitDate = currentDate.minusMonths(2);

	        // Convertissez la limite de date en objet Date
	        Date limitDateAsDate = Date.from(limitDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

	        // Récupérez tous les tickets qui ne sont pas archivés et dont la date de clôture de l'intervention est après la limite de date
	        List<Ticket> activeTickets = ticketRepository.findAllByInterventionDateClotureAfterOrEqualTo(limitDateAsDate);

	        return activeTickets;
	    }

	    
	    @Override
	    public Long totalTicketsDemandeur() {
	        // Get the authenticated user
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

	        if (authentication == null || !authentication.isAuthenticated()) {
	            // Handle unauthenticated users or return an error
	            return null;
	        }

	        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	        String username = userDetails.getUsername();

	        Demandeur demandeur = demandeurService.getDemandeurByUsername(username);

	        if (demandeur == null) {
	            // Handle the case where the demandeur doesn't exist
	            return null;
	        }

	        return ticketRepository.countByDemandeur(demandeur);
	    }

	    @Override
	    public Long attenteTicketsDemandeur() {
	        // Get the authenticated user
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

	        if (authentication == null || !authentication.isAuthenticated()) {
	            // Handle unauthenticated users or return an error
	            return null;
	        }

	        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	        String username = userDetails.getUsername();

	        Demandeur demandeur = demandeurService.getDemandeurByUsername(username);

	        if (demandeur == null) {
	            // Handle the case where the demandeur doesn't exist
	            return null;
	        }

	        return ticketRepository.countByDemandeurAndInterStatut(demandeur, "en attente");
	    }

	    @Override
	    public Long todoTicketsDemandeur() {
	        // Get the authenticated user
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

	        if (authentication == null || !authentication.isAuthenticated()) {
	            // Handle unauthenticated users or return an error
	            return null;
	        }

	        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	        String username = userDetails.getUsername();

	        Demandeur demandeur = demandeurService.getDemandeurByUsername(username);

	        if (demandeur == null) {
	            // Handle the case where the demandeur doesn't exist
	            return null;
	        }

	        return ticketRepository.countByDemandeurAndInterStatut(demandeur, "à réaliser");
	    }

	 
	    
	    
	    @Override
	    public Long doneTicketsDemandeur() {
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

	        if (authentication == null || !authentication.isAuthenticated()) {
	            // Handle unauthenticated users or return an error
	            return null;
	        }

	        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	        String username = userDetails.getUsername();

	        Demandeur demandeur = demandeurService.getDemandeurByUsername(username);
	        if (demandeur == null) {
	            // Handle the case where the demandeur doesn't exist
	            return null;
	        }

	        Long count = ticketRepository.countByDemandeurAndInterStatut(demandeur, "réalisé");
	        // Log the result for debugging
	        System.out.println("Count of 'réalisé' tickets: " + count);
	        return count;
	    }

	    
	    
	    
	    @Override
	    public Long doneTicketsTechnicien() {
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

	        if (authentication == null || !authentication.isAuthenticated()) {
	            // Handle unauthenticated users or return an error
	            return null;
	        }

	        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	        String username = userDetails.getUsername();

	        Technicien technicien = technicienService.getTechnicienByUsername(username);
	        if (technicien == null) {
	            // Handle the case where the demandeur doesn't exist
	            return null;
	        }

	        Long count = ticketRepository.countByTechnicienAndInterStatut(technicien, "réalisé");
	        // Log the result for debugging
	        System.out.println("Count of 'réalisé' tickets: " + count);
	        return count;
	    }

	    
	    
	    
	    
	    
	    
	    

	    @Override
	    public Long cancelledTicketsDemandeur() {
	        // Get the authenticated user
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

	        if (authentication == null || !authentication.isAuthenticated()) {
	            // Handle unauthenticated users or return an error
	            return null;
	        }

	        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	        String username = userDetails.getUsername();

	        Demandeur demandeur = demandeurService.getDemandeurByUsername(username);

	        if (demandeur == null) {
	            // Handle the case where the demandeur doesn't exist
	            return null;
	        }

	        return ticketRepository.countByDemandeurAndInterStatut(demandeur, "annulé");
	    }

	    @Override
	    public Long blockedTicketsDemandeur() {
	        // Get the authenticated user
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

	        if (authentication == null || !authentication.isAuthenticated()) {
	            // Handle unauthenticated users or return an error
	            return null;
	        }

	        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	        String username = userDetails.getUsername();

	        Demandeur demandeur = demandeurService.getDemandeurByUsername(username);

	        if (demandeur == null) {
	            // Handle the case where the demandeur doesn't exist
	            return null;
	        }

	        return ticketRepository.countByDemandeurAndInterStatut(demandeur, "bloqué");
	    }

	    public Map<String, Long> countTicketsByClient() {
	        // Récupérer tous les clients
	        List<Client> clients = clientRepository.findAll();

	        // Initialiser la map pour stocker le nombre de tickets par client
	        Map<String, Long> ticketsByClient = new HashMap<>();

	        // Parcourir les clients et compter les tickets associés
	        for (Client client : clients) {
	            Long totalTickets = 0L;
	            List<Demandeur> demandeurs = client.getDemandeurs();
	            for (Demandeur demandeur : demandeurs) {
	                Long ticketsDemandeur = ticketRepository.countByDemandeur(demandeur);
	                totalTickets += ticketsDemandeur;
	            }
	            // Ajouter le nom de la société et le nombre de tickets associés à la map
	            ticketsByClient.put(client.getNomSociete(), totalTickets);
	        }

	        return ticketsByClient;
	    }
	

	    
	    @Override
	    public List<TicketDurationDTO> getDurationsForTicketsToRealize() {
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	        if (authentication == null || !authentication.isAuthenticated()) {
	            // Handle unauthenticated users or return an error
	            return null;
	        }

	        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	        String username = userDetails.getUsername();

	        Demandeur demandeur = demandeurService.getDemandeurByUsername(username);
	        if (demandeur == null) {
	            // Handle the case where the demandeur doesn't exist
	            return null;
	        }

	        List<Ticket> tickets = ticketRepository.findByDemandeurAndInterStatut(demandeur, "à réaliser");
	        return tickets.stream()
	                      .map(this::toTicketDurationDTO)
	                      .collect(Collectors.toList());
	    }

	    private TicketDurationDTO toTicketDurationDTO(Ticket ticket) {
	        Intervention intervention = ticket.getIntervention();
	        return TicketDurationDTO.builder()
	                                .interCode(ticket.getInterCode())
	                                .interDesignation(ticket.getInterDesignation())
	                                .datePrevue(ticket.getDatePrevue())
	                                .dureePrevue(ticket.getDureePrevue())
	                                .dureeRealisation(intervention != null ? intervention.getDureeRealisation() : null)
	                                .build();
	    }
	// f chhar 9adch mn réalisés
	    @Override
	    public List<MonthlyTicketCountDTO> getRealizedTicketsCountByMonth() {
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	        if (authentication == null || !authentication.isAuthenticated()) {
	            // Handle unauthenticated users or return an error
	            return null;
	        }

	        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	        String username = userDetails.getUsername();

	        Demandeur demandeur = demandeurService.getDemandeurByUsername(username);
	        if (demandeur == null) {
	            // Handle the case where the demandeur doesn't exist
	            return null;
	        }

	        List<Ticket> tickets = ticketRepository.findByDemandeurAndInterStatut(demandeur, "réalisé");

	        // Group tickets by month of intervention closure date and count them
	        Map<Integer, Long> ticketsByMonth = tickets.stream()
	                .filter(ticket -> ticket.getIntervention() != null && ticket.getIntervention().getDateCloture() != null)
	                .collect(Collectors.groupingBy(ticket -> {
	                    LocalDate closureDate = ticket.getIntervention().getDateCloture().toInstant()
	                            .atZone(ZoneId.systemDefault()).toLocalDate();
	                    return closureDate.getMonthValue();
	                }, Collectors.counting()));

	        // Create a list of MonthlyTicketCountDTO for all months (1-12)
	        List<MonthlyTicketCountDTO> monthlyTicketCounts = new ArrayList<>();
	        for (int month = 1; month <= 12; month++) {
	            monthlyTicketCounts.add(new MonthlyTicketCountDTO(month, ticketsByMonth.getOrDefault(month, 0L)));
	        }

	        return monthlyTicketCounts;
	    }
	
	    
	    
	  //requête (nombre de tickets par technicien par mois)ll manager 

	    @Override
	    public List<Map<String, Object>> countTicketsByTechnician() {
	        List<Object[]> delayedResults = ticketRepository.countDelayedTicketsByTechnician();
	        List<Object[]> onTimeResults = ticketRepository.countOnTimeTicketsByTechnician();

	        // Initialize a map to store the results
	        Map<String, Map<String, Long>> technicianCounts = new HashMap<>();

	        // Fill the map with delayed ticket results
	        for (Object[] result : delayedResults) {
	            String nom = (String) result[0];
	            String prenom = (String) result[1];
	            long count = (long) result[2];

	            String technicianKey = nom + " " + prenom;
	            technicianCounts.computeIfAbsent(technicianKey, k -> new HashMap<>()).put("delayed", count);
	        }

	        // Fill the map with on-time ticket results
	        for (Object[] result : onTimeResults) {
	            String nom = (String) result[0];
	            String prenom = (String) result[1];
	            long count = (long) result[2];

	            String technicianKey = nom + " " + prenom;
	            technicianCounts.computeIfAbsent(technicianKey, k -> new HashMap<>()).put("onTime", count);
	        }

	        // Prepare the final formatted results, excluding technicians with no tickets
	        List<Map<String, Object>> formattedResults = new ArrayList<>();
	        for (Map.Entry<String, Map<String, Long>> entry : technicianCounts.entrySet()) {
	            String[] nameParts = entry.getKey().split(" ");
	            String nom = nameParts[0];
	            String prenom = nameParts[1];
	            Map<String, Long> counts = entry.getValue();
	            long delayedCount = counts.getOrDefault("delayed", 0L);
	            long onTimeCount = counts.getOrDefault("onTime", 0L);

	            if (delayedCount > 0 || onTimeCount > 0) {
	                Map<String, Object> formattedResult = new HashMap<>();
	                formattedResult.put("technicienNom", nom);
	                formattedResult.put("technicienPrenom", prenom);
	                formattedResult.put("nombreTicketsEnRetard", delayedCount);
	                formattedResult.put("nombreTicketsATemps", onTimeCount);
	                formattedResults.add(formattedResult);
	            }
	        }

	        return formattedResults;
	    }
@Override
public List<Map<String, Object>> countTicketsByTechnicianAndMonth() {
    // Authentication and authorization
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || !authentication.isAuthenticated()) {
        // Handle unauthenticated users or return an error
        return null; // or throw an appropriate exception
    }

    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    String username = userDetails.getUsername();

    Technicien technicien = technicienService.getTechnicienByUsername(username);


    if (technicien == null) {
        // Handle the case where the technicien doesn't exist
        return null; // or throw an appropriate exception
    }

    Long technicienId = technicien.getCodeTechnicien();

    // Fetch tickets for the authenticated technicien and process them
    List<Object[]> delayedResults = ticketRepository.countDelayedTicketsByTechnicianAndMonth(technicienId);
    List<Object[]> onTimeResults = ticketRepository.countOnTimeTicketsByTechnicianAndMonth(technicienId);

    // Initialize a map to store the results by month
    Map<Integer, Map<String, Long>> monthlyCounts = new HashMap<>();

    // Fill the map with delayed ticket results
    for (Object[] result : delayedResults) {
        int month = (int) result[0];
        long count = (long) result[1];

        monthlyCounts.computeIfAbsent(month, k -> new HashMap<>()).put("delayed", count);
    }

    // Fill the map with on-time ticket results
    for (Object[] result : onTimeResults) {
        int month = (int) result[0];
        long count = (long) result[1];

        monthlyCounts.computeIfAbsent(month, k -> new HashMap<>()).put("onTime", count);
    }

    // Prepare the final formatted results
    List<Map<String, Object>> formattedResults = new ArrayList<>();
    for (Map.Entry<Integer, Map<String, Long>> entry : monthlyCounts.entrySet()) {
        int month = entry.getKey();
        Map<String, Long> counts = entry.getValue();
        long delayedCount = counts.getOrDefault("delayed", 0L);
        long onTimeCount = counts.getOrDefault("onTime", 0L);

        if (delayedCount > 0 || onTimeCount > 0) {
            Map<String, Object> formattedResult = new HashMap<>();
            formattedResult.put("mois", month);
            formattedResult.put("nombreTicketsEnRetard", delayedCount);
            formattedResult.put("nombreTicketsATemps", onTimeCount);
            formattedResults.add(formattedResult);
        }
    }

    return formattedResults;
}


	    
	    
	    
	    
	    
	    
}