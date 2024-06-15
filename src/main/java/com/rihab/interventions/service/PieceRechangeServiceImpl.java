package com.rihab.interventions.service;

import java.util.List;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rihab.interventions.dto.PieceRequestDTO;
import com.rihab.interventions.entities.Article;
import com.rihab.interventions.entities.PieceRechange;
import com.rihab.interventions.entities.PieceRechangeId;
import com.rihab.interventions.entities.PieceRechangeRequest;
import com.rihab.interventions.repos.ArticleRepository;
import com.rihab.interventions.repos.PieceRechangeRepository;

import jakarta.transaction.Transactional;

@Service
public class PieceRechangeServiceImpl  implements PieceRechangeService {
	
	@Autowired
	PieceRechangeRepository pieceRechangeRepository;
	@Autowired
	ArticleRepository articleRepository;


	@Override
	public PieceRechange savePieceRechange(PieceRechange departement) {
	    // Obtenez les valeurs des clés primaires de l'article et de l'équipement depuis departement
	    long articleId = departement.getArticle().getCodeArticle();
	    String equipementId = departement.getEquipement().getEqptCode();
	    
	    // Créez un nouvel objet PieceRechangeId avec les clés primaires obtenues
	    PieceRechangeId pieceRechangeId = new PieceRechangeId(equipementId, articleId);
	    
	    // Créez un nouvel objet PieceRechange avec l'objet PieceRechangeId
	    PieceRechange pieceRechange = new PieceRechange();
	    pieceRechange.setId(pieceRechangeId);
	    pieceRechange.setArticle(departement.getArticle());
	    pieceRechange.setEquipement(departement.getEquipement());
	    pieceRechange.setEqprValeurFrequence(departement.getEqprValeurFrequence());
	    pieceRechange.setEqprUnitFrequence(departement.getEqprUnitFrequence());
	    pieceRechange.setEqprQte(departement.getEqprQte());
	    
	    // Enregistrez l'objet PieceRechange dans la base de données
	    return pieceRechangeRepository.save(pieceRechange);
	}

	@Override
	public PieceRechange updatePieceRechange(PieceRechange updatedPiece) {
	    // Récupérez l'identifiant de la pièce de rechange mise à jour
	    PieceRechangeId updatedPieceId = updatedPiece.getId();
	    
	    // Vérifiez si la pièce de rechange existe déjà dans la base de données
	   Optional<PieceRechange> existingPieceOptional = pieceRechangeRepository.findById(updatedPieceId);
	    
	    if(existingPieceOptional.isPresent()) {
	        // Si la pièce de rechange existe, mettez à jour ses champs avec les nouvelles valeurs
	        PieceRechange existingPiece = existingPieceOptional.get();
	        existingPiece.setEqprValeurFrequence(updatedPiece.getEqprValeurFrequence());
	        existingPiece.setEqprUnitFrequence(updatedPiece.getEqprUnitFrequence());
	        existingPiece.setEqprQte(updatedPiece.getEqprQte());
	        
	        // Enregistrez les modifications dans la base de données
	        return pieceRechangeRepository.save(existingPiece);
	    } else {
	        // Si la pièce de rechange n'existe pas, vous pouvez choisir de lever une exception ou de créer une nouvelle entrée
	        // Ici, nous levons une exception pour indiquer que la pièce de rechange n'a pas été trouvée
	        throw new RuntimeException("Pièce de rechange non trouvée avec l'identifiant : " + updatedPieceId);
	    }
	}

@Override
public void deletePieceRechange(PieceRechange departement) {
	pieceRechangeRepository.delete(departement);
}

@Override
public void deletePieceRechangeById(PieceRechangeId id) {
    // Implementer la logique pour trouver la pièce de rechange par son id si nécessaire
    // puis appeler la méthode deletePieceRechange de la repository
    PieceRechange pieceRechange = pieceRechangeRepository.findById(id).orElse(null);
    if (pieceRechange != null) {
        deletePieceRechange(pieceRechange);
    }
}
@Override
public PieceRechange getPieceRechange(PieceRechangeId id) {
return pieceRechangeRepository.findById(id).get();
}


@Override
public List<PieceRechange> getAllPiecesRechange() {
return pieceRechangeRepository.findAll();
}

/*
@Override
@Transactional
public PieceRechange ajoutNvPieceRequest(PieceRechangeRequest pieceRequest) {
    // Votre code existant pour récupérer le technicien et enregistrer la demande de pièce de rechange

    // Vérifiez si le statut de la demande est "nouveau"
    if ("nouveau".equalsIgnoreCase(pieceRequest.getEtat())) {
        // Créez une nouvelle pièce de rechange
        PieceRechange pieceRechange = new PieceRechange();
        
        // Créez l'ID de la pièce de rechange en utilisant les informations disponibles dans la demande
        PieceRechangeId pieceRechangeId = new PieceRechangeId();
        pieceRechangeId.setEqptCode(pieceRequest.getTicket().getEquipement().getEqptCode()); // Utilisez le code de l'équipement de la demande
        // Vous pouvez ignorer la clé de l'article car elle sera remplie une fois que le nouvel article est créé
        pieceRechange.setId(pieceRechangeId);

        // Remplissez les autres propriétés de la pièce de rechange en fonction des informations disponibles dans la demande
        pieceRechange.setEqprQte(pieceRequest.getQuantiteDemande());
        
        // Assurez-vous d'initialiser ou de définir toutes les autres propriétés de la pièce de rechange

        // Créez un nouvel article
        Article article = new Article();
        article.setNomArticle("nv"); // Vous pouvez utiliser n'importe quelle valeur par défaut pour le nom de l'article
        article.setMarqueArticle("Marque par défaut"); // Vous pouvez utiliser n'importe quelle valeur par défaut pour la marque de l'article
        article.setQteArticle(0.0); // Initialisez la quantité d'article à 0

        // Enregistrez le nouvel article dans la base de données
        articleRepository.save(article);

        // Associez le nouvel article à la pièce de rechange
        pieceRechange.setArticle(article);
        
        // Enregistrez la pièce de rechange dans la base de données
        pieceRechangeRepository.save(pieceRechange);

        return pieceRechange;
    }
    return null;
}
*/
@Transactional
public PieceRechange addPieceRechangeWithArticle(PieceRechange pieceRechange, Article article) {
    // Initialiser les champs de l'article
    article.setMarqueArticle("Marque par défaut");
    article.setNomArticle("Nom par défaut");
    article.setQteArticle(0.0); // Initialiser la quantité d'article à 0.0

    // Enregistrer l'article pour obtenir son ID généré
    Article savedArticle = articleRepository.save(article);

    // Créer l'ID de la pièce de rechange en utilisant les informations disponibles dans la demande
    PieceRechangeId pieceRechangeId = new PieceRechangeId();
    pieceRechangeId.setCodeArticle(savedArticle.getCodeArticle());
    pieceRechangeId.setEqptCode("codeEqp"); // Utiliser le code de l'équipement de la demande

    // Assigner l'ID de la pièce de rechange
    pieceRechange.setId(pieceRechangeId);

    // Associer l'article à la pièce de rechange
    pieceRechange.setArticle(savedArticle);

    // Enregistrer la pièce de rechange
    return pieceRechangeRepository.save(pieceRechange);
}







}
