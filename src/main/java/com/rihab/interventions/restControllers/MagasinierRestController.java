package com.rihab.interventions.restControllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.rihab.interventions.dto.MagasinierDTO;
import com.rihab.interventions.dto.TicketDTO;
import com.rihab.interventions.entities.Magasinier;
import com.rihab.interventions.entities.Role;
import com.rihab.interventions.entities.Ticket;
import com.rihab.interventions.service.MagasinierService;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class MagasinierRestController {


	@Autowired
	MagasinierService magasinierService;
	
	
	@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping(path="allMagasiniers",method = RequestMethod.GET)
public List<Magasinier> getAllMagasiniers() {
	return magasinierService.getAllMagasiniers();
}


@RequestMapping(value="/getbycodeMagasinier/{codeMagasinier}",method = RequestMethod.GET)
public Magasinier getMagasinierById(@PathVariable("codeMagasinier") long codeMagasinier) {
	return magasinierService.getMagasinier(codeMagasinier);
 }
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping(path="/addMagasinier",method = RequestMethod.POST)

public Magasinier createMagasinier(@RequestBody Magasinier manager) {
	return magasinierService.saveMagasinier(manager);
}


@RequestMapping(path="/updateMagasinier",method = RequestMethod.PUT)

public Magasinier updateMagasinier(@RequestBody Magasinier manager) {
		return magasinierService.updateMagasinier(manager);
}

//autorisation au admin seulement cette methode
@RequestMapping(value="/delMagasinier/{codeMagasinier}",method = RequestMethod.DELETE)
@PreAuthorize("hasAuthority('ADMIN')")
public void deleteDemandeur(@PathVariable("codeMagasinier") long codeMagasinier)
{
	magasinierService.deleteMagasinierByCode(codeMagasinier);
}

/*
@GetMapping("/allMag") // Définissez votre URL spécifique pour cette méthode
public List<MagasinierDTO> getAllMagasiniers() {
    List<Magasinier> magasiniers = magasinierService.getAllMagasiniers();
    return magasiniers.stream()
            .map(magasinier -> convertToDTO(magasinier))
            .collect(Collectors.toList());
}

private MagasinierDTO convertToDTO(Magasinier magasinier) {
	MagasinierDTO.MagasinierDTOBuilder builder = MagasinierDTO.builder()
            .codeMagasinier(magasinier.getCodeMagasinier())
            .nom(magasinier.getUser().getNom())
            .prenom(magasinier.getUser().getPrenom())
            .email(magasinier.getUser().getEmail())
            .username(magasinier.getUser().getUsername())
            .password(magasinier.getUser().getPassword())
            .tel(magasinier.getUser().getTel())
            .age(magasinier.getUser().getAge())
            .role(magasinier.getUser().getRole())
            .sexe(magasinier.getUser().getSexe())
          .idUser(magasinier.getUser().getId())
          .
            .dateEmbauche(magasinier.getUser().getDateEmbauche());
            return builder.build();
}

*/





}

