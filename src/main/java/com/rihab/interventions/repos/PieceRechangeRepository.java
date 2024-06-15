package com.rihab.interventions.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rihab.interventions.entities.Manager;
import com.rihab.interventions.entities.PieceRechange;
import com.rihab.interventions.entities.PieceRechangeId;

public interface PieceRechangeRepository extends JpaRepository<PieceRechange, PieceRechangeId> {

	void deletePieceRechangeById(PieceRechangeId id);

}
