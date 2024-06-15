package com.rihab.interventions.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rihab.interventions.entities.PieceRechangeRequest;

public interface PieceRequestRepository extends JpaRepository<PieceRechangeRequest, Long> {

	List<PieceRechangeRequest> findByTicketInterCode(String interCode);

	List<PieceRechangeRequest> findByCodeDemande(long codeDemande);

}
