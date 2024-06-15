package com.rihab.interventions.restControllers;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rihab.interventions.dto.MagasinierDTO;
import com.rihab.interventions.entities.Departement;
import com.rihab.interventions.entities.Equipement;
import com.rihab.interventions.entities.Magasinier;
import com.rihab.interventions.entities.Technicien;
import com.rihab.interventions.entities.Ticket;
import com.rihab.interventions.service.TechnicienService;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class TechnicienRestController {



	@Autowired
	TechnicienService technicienService;

@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping(path="allTechniciens",method = RequestMethod.GET)
public List<Technicien> getAllTechniciens() {
	return technicienService.getAllTechniciens();
}


@RequestMapping(value="/getbycodetechnicien/{codeTechnicien}",method = RequestMethod.GET)
public Technicien getTechnicienById(@PathVariable("codeTechnicien") long codeTechnicien) {
	return technicienService.getTechnicien(codeTechnicien);
 }

//autorisation au admin seulement cette methode
@RequestMapping(path="/addTechnicien",method = RequestMethod.POST)

public Technicien createTechnicien(@RequestBody Technicien manager) {
	return technicienService.saveTechnicien(manager);
}


@RequestMapping(path="/updateTechnicien",method = RequestMethod.PUT)

public Technicien updateTechnicien(@RequestBody Technicien manager) {
		return technicienService.updateTechnicien(manager);
}

//autorisation au admin seulement cette methode
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping(value="/delTechnicien/{codeTechnicien}",method = RequestMethod.DELETE)

public void deleteTechnicien(@PathVariable("codeTechnicien") long codeTechnicien)
{
	technicienService.deleteTechnicienByCode(codeTechnicien);
}


@PreAuthorize("hasAuthority('MANAGER')")
@RequestMapping(value="/getTechDepart/{codeDepart}",method = RequestMethod.GET)
public List<Technicien> getTechniciensByDepartementCodeDepart(@PathVariable("codeDepart") long codeDepart) {
		return technicienService.findByDepartementCodeDepart(codeDepart);
}


@PreAuthorize("hasAuthority('ADMIN')")
@GetMapping("/totalTechDepart")
public ResponseEntity<List<Object[]>> countTechniciensByDepartement() {
    List<Object[]> counts = technicienService.countTechniciensByDepartement();
    return ResponseEntity.ok(counts);
}
  
//URL postman:
// PUT /responsable/{id}?responsable=O
//PUT /responsable/{id}?responsable=N

@PreAuthorize("hasAuthority('ADMIN')")
@PutMapping("/responsable/{id}")
public Technicien updateResponsable(@PathVariable Long id, @RequestParam String responsable) {
    return technicienService.updateResponsable(id, responsable);
}


}


