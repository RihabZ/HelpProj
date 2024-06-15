package com.rihab.interventions.restControllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rihab.interventions.dto.PieceRequestDTO;
import com.rihab.interventions.entities.PieceRechangeRequest;

import com.rihab.interventions.service.PieceRequestService;

import jakarta.persistence.EntityNotFoundException;


@RestController
@RequestMapping("/api")
@CrossOrigin
public class PieceRequestRestController {

	

	@Autowired
	PieceRequestService pieceReqService;
	

	@PreAuthorize("hasAuthority('MANAGER')")
@RequestMapping(path = "allRequests", method = RequestMethod.GET)
public List<PieceRechangeRequest> getAllRequests() {
    return pieceReqService.getAllPiecesRequests();
}


	@PreAuthorize("hasAuthority('MANAGER')")
@RequestMapping(value="/getByCodeDemande/{codeDemande}",method = RequestMethod.GET)
public PieceRequestDTO getPieceRequestById(@PathVariable("codeDemande") long codeDemande) {
	return pieceReqService.getPieceRequest(codeDemande);
 }


@PreAuthorize("hasAuthority('TECHNICIEN')")
@RequestMapping(path = "/addPieceRequest", method = RequestMethod.POST)
public List<PieceRequestDTO> createPieceRequests(@RequestBody List<PieceRequestDTO> pieceReqDTOs) {
    return pieceReqService.savePieceRequests(pieceReqDTOs);
}
@PreAuthorize("hasAuthority('TECHNICIEN')")
@RequestMapping(value="/deletePieceRequest/{codeDemande}",method = RequestMethod.DELETE)

public void deletePieceRequest(@PathVariable("codeDemande") long codeDemande)
{
	pieceReqService.deletePieceRequestByCodeDemande(codeDemande);
}
/*
@PreAuthorize("hasAuthority('TECHNICIEN')")
@PutMapping("/updateEtatPieceRequest/{codeDemande}")
public ResponseEntity<PieceRequestDTO> updateEtatPieceRequest(@PathVariable Long codeDemande) {
    PieceRequestDTO updatedPieceRequest = pieceReqService.updateEtatPieceRequest(codeDemande);
    return ResponseEntity.ok(updatedPieceRequest);
}/
*/
@PreAuthorize("hasAuthority('TECHNICIEN')")
@PutMapping("/updateEtatPieceRequest/{codeDemande}/{codeArticle}")
public ResponseEntity<PieceRequestDTO> updateEtatPieceRequest(@PathVariable Long codeDemande, @RequestParam Long codeArticle) {
    PieceRequestDTO updatedPieceRequest = pieceReqService.updateEtatPieceRequest(codeDemande, codeArticle);
    return ResponseEntity.ok(updatedPieceRequest);
}




@PreAuthorize("hasAuthority('MANAGER')")
@RequestMapping(path = "/updateStatutDemande/{interCode}", method = RequestMethod.PUT)
public PieceRequestDTO updateStatutDemande(@PathVariable String interCode, @RequestParam String newStatutDemande) {
    return pieceReqService.updateStatutDemandePieceRequest(interCode, newStatutDemande);
}
@PreAuthorize("hasAuthority('MANAGER')")
@PutMapping("/updateQuantiteStock/{interCode}")
public ResponseEntity<String> updateQuantiteStock(@PathVariable String interCode) {
    String message = pieceReqService.updateQuantiteStock(interCode);
    return ResponseEntity.ok(message);
}


@PreAuthorize("hasAuthority('MANAGER')")
@PutMapping("/updateQuantitePieceRechange/{interCode}")
public ResponseEntity<Void> updateQuantitePieceRechange(@PathVariable String interCode, @RequestBody Map<Long, Double> quantites) {
	pieceReqService.updateQuantitePieceRechange(interCode, quantites);
    return ResponseEntity.ok().build();
}

@PreAuthorize("hasAuthority('MAGASINIER')")
@PutMapping("/updateChampDone/{interCode}")
public void updateChampDone(@PathVariable String interCode) {
	pieceReqService.updateChapDone(interCode);
}


@PreAuthorize("hasAuthority('MAGASINIER')")
@PutMapping("/updateChapNonDone/{interCode}")
public ResponseEntity<String> updateChapNonDone(@PathVariable("interCode") String interCode) {
    try {
    	pieceReqService.updateChapNonDone(interCode);
        return ResponseEntity.ok("Les demandes de pièces ont été mises à jour avec succès.");
    } catch (EntityNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aucune demande de pièce trouvée pour le ticket avec le code interne " + interCode);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Une erreur est survenue lors de la mise à jour des demandes de pièces.");
    }

}
@PreAuthorize("hasAuthority('MANAGER')")
@PutMapping("/updateArticleForPieceRequests/{codeDemande}")
public ResponseEntity<Void> updateArticleForPieceRequests(
        @PathVariable long codeDemande, 
        @RequestParam long newArticleId) {
    pieceReqService.updateArticleForPieceRequests(codeDemande, newArticleId);
    return ResponseEntity.ok().build();
}






}
