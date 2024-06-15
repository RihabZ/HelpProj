package com.rihab.interventions.restControllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.rihab.interventions.dto.EquipementDTO;
import com.rihab.interventions.dto.TypeDTO;
import com.rihab.interventions.entities.Equipement;
import com.rihab.interventions.entities.Famille;
import com.rihab.interventions.entities.Type;
import com.rihab.interventions.service.EquipementService;
import com.rihab.interventions.service.EquipementServiceImpl;


@RestController
@RequestMapping("/api")
@CrossOrigin
public class EquipementRESTController {

		@Autowired
		EquipementService equipementService;
		@Autowired
		EquipementServiceImpl equipementServiceImpl;
		
		
		
		@PreAuthorize("hasAnyAuthority('TECHNICIEN', 'MANAGER')")
	@RequestMapping(path="all",method = RequestMethod.GET)
	public List<Equipement> getAllEquipements() {
		return equipementService.getAllEquipements();
	}
	
	
	@RequestMapping(value="/getbyeqptcode/{eqptCode}",method = RequestMethod.GET)
	public Equipement getEquipementById(@PathVariable("eqptCode") String eqptCode) {
		return equipementService.getEquipement(eqptCode);
	 }
	 @PreAuthorize("hasAuthority('TECHNICIEN')")
	    @PostMapping("/addeqpt")
		public Equipement createEquipement(@RequestBody Equipement equipement) {
			return equipementService.save(equipement);
		}
	//autorisation au admin seulement cette methode
	@RequestMapping(value="/deleqpt/{eqptCode}",method = RequestMethod.DELETE)
	@PreAuthorize("hasAuthority('TECHNICIEN')")
	public void deleteEquipement(@PathVariable("eqptCode") String eqptCode)
	{
		equipementService.deleteEquipementByCode(eqptCode);
	}

	
	@RequestMapping(value="/eqptstype/{eqtyCode}",method = RequestMethod.GET)
	public List<Equipement> getEquipementsByTypeCodeType(@PathVariable("eqtyCode") String eqtyCode) {
			return equipementService.findByTypeEqtyCode(eqtyCode);
	}
	
	@PreAuthorize("hasAuthority('TECHNICIEN')")
	@PutMapping("/updateEqpt/{id}")
	public Equipement updateEquipement(@RequestBody Equipement equipement) {
		return equipementService.updateEquipement(equipement);
}

	@PreAuthorize("hasAuthority('TECHNICIEN')")
	@RequestMapping(value="/searchDesignation/{eqptDesignation}",method = RequestMethod.GET)
	 public List<Equipement>getEquipementByDesignation(@PathVariable("eqptDesignation") String eqptDesignation) {
        return equipementService.findByEqptDesignation(eqptDesignation);
    }
	
	@PreAuthorize("hasAuthority('TECHNICIEN')")
	@RequestMapping(value="/searchDesignationContains/{eqptDesignation}",method = RequestMethod.GET)
    public List<Equipement> getEquipementByEqptDesignationContains(@PathVariable("eqptDesignation") String eqptDesignation) {
        return equipementService.findByEqptDesignationContains(eqptDesignation);
    }

	@PreAuthorize("hasAuthority('TECHNICIEN')")
	@GetMapping("/CodeAndDesignation")
	public List<EquipementDTO> getAllEquipementCodesAndLibelle() {
	    List<Equipement> equipements = equipementService.getAllEquipements();
	    List<EquipementDTO> equipementDTOs = new ArrayList<>();

	    for (Equipement equipement : equipements) {
	    	EquipementDTO dto = new EquipementDTO();
	        dto.setCode(equipement.getEqptCode());
	        dto.setEqptDesignation(equipement.getEqptDesignation());
	        equipementDTOs.add(dto);
	    }

	    return equipementDTOs;
	}
	@PreAuthorize("hasAuthority('CLIENT')")
	@RequestMapping(value="/eqptClient/{codeClient}",method = RequestMethod.GET)
	public List<Equipement> getEquipementsByClientCodeClient(@PathVariable("codeClient") Long codeClient) {
			return equipementService.findByClientCodeClient(codeClient);
	}
	
	
	
	
	
	
	
	
	
}
