package com.rihab.interventions.service;

import java.util.List;

import com.rihab.interventions.dto.PieceRequestDTO;
import com.rihab.interventions.entities.Article;
import com.rihab.interventions.entities.PieceRechange;
import com.rihab.interventions.entities.PieceRechangeId;
import com.rihab.interventions.entities.PieceRechangeRequest;

public interface PieceRechangeService {
		

	PieceRechange savePieceRechange(PieceRechange departement);
	PieceRechange updatePieceRechange(PieceRechange Departement);
void deletePieceRechange(PieceRechange departement);
 void deletePieceRechangeById(PieceRechangeId id);
 PieceRechange getPieceRechange(PieceRechangeId id);
List<PieceRechange> getAllPiecesRechange();
PieceRechange addPieceRechangeWithArticle(PieceRechange pieceRechange, Article article);


}
