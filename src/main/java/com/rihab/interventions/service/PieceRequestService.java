package com.rihab.interventions.service;

import java.util.List;
import java.util.Map;

import com.rihab.interventions.dto.PieceRequestDTO;
import com.rihab.interventions.entities.PieceRechangeRequest;

public interface PieceRequestService {

	//PieceRequestDTO savePieceRequest(PieceRequestDTO inter) ;
	//PieceRequestDTO updatePieceRequest(PieceRequestDTO inter);
		//void deletePieceRequest(PieceRequestDTO inter);
		void deletePieceRequestByCodeDemande(long code);
		 PieceRequestDTO getPieceRequest(long code);
		
		 List<PieceRequestDTO> getAllPiecesRequests1();
			
		void deletePieceRequest(PieceRechangeRequest inter);
		List<PieceRechangeRequest> getAllPiecesRequests();
		List<PieceRequestDTO> savePieceRequests(List<PieceRequestDTO> pieceRequests) ;
		List<PieceRechangeRequest> toPieceRequests(List<PieceRequestDTO> requests);
		//PieceRequestDTO updateEtatPieceRequest(Long codeDemande);
		PieceRequestDTO updateStatutDemandePieceRequest(String interCode, String newStatutDemande);
		
		PieceRequestDTO updateEtatPieceRequest(Long codeDemande, Long codeArticle) ;
		
		String updateQuantiteStock(String interCode);

		void updateQuantitePieceRechange(String interCode, Map<Long, Double> articleQuantities);
		void updateChapDone(String interCode);
		void updateChapNonDone(String interCode);
		
		void updateArticleForPieceRequests(long codeDemande, long newArticleId) ;
}
