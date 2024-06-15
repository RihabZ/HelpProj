package com.rihab.interventions.service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.rihab.interventions.dto.EquipementDTO;
import com.rihab.interventions.entities.*;
import com.rihab.interventions.repos.*;

@Service
public class EquipementServiceImpl implements EquipementService {
	
				@Autowired
				EquipementRepository equipementRepository;
				@Autowired
				ImageService imageService;
				
				
				
				@Override
				public Equipement save(Equipement equipement)
				{
				return equipementRepository.save(equipement);

				}

				// Méthode pour convertir un DTO en entité Equipement
				public Equipement convertToEntity(EquipementDTO equipementDTO) {
				    return Equipement.builder()
				            .eqptCode(equipementDTO.getCode())
				            .eqptDesignation(equipementDTO.getEqptDesignation())
				            .eqptId(equipementDTO.getEqptId())
				            .eqptPrix(equipementDTO.getEqptPrix())
				            .eqptDtAchat(equipementDTO.getEqptDtAchat())
				            .eqptGarantie(equipementDTO.getEqptGarantie())
				            .eqptEnService(equipementDTO.getEqptEnService())
				            .eqptGarTypeDtRef(equipementDTO.getEqptGarTypeDtRef())
				            .eqptMachine(equipementDTO.getEqptMachine())
				            .eqptDtCreation(equipementDTO.getEqptDtCreation())
				            .eqptDureeGarantie(equipementDTO.getEqptDureeGarantie())
				            .dateFinGarantie(equipementDTO.getDateFinGarantie())
				            
				            .eqptLotNumero(equipementDTO.getEqptLotNumero())
				            .eqptNumeroSerie(equipementDTO.getEqptNumeroSerie())
				            .dateFabrication(equipementDTO.getDateFabrication())
				            .dateInstallation(equipementDTO.getDateInstallation())
				            .dateMiseEnService(equipementDTO.getDateMiseEnService())
				            
				            .dateDemontage(equipementDTO.getDateDemontage())
				            .dateRemplacement(equipementDTO.getDateRemplacement())
				            .dateLivraison(equipementDTO.getDateLivraison())
				            .type(equipementDTO.getType())
				            .client(equipementDTO.getClient())
				            .famille(equipementDTO.getFamille())
				            .build();
				}

				// Méthode pour convertir une entité Equipement en DTO
				public EquipementDTO convertToDTO(Equipement equipement) {
				    return EquipementDTO.builder()
				            .code(equipement.getEqptCode())
				            .eqptDesignation(equipement.getEqptDesignation())
				            .eqptId(equipement.getEqptId())
				            .eqptPrix(equipement.getEqptPrix())
				            .eqptDtAchat(equipement.getEqptDtAchat())
				            .eqptGarantie(equipement.getEqptGarantie())
				            .eqptEnService(equipement.getEqptEnService())
				            .eqptGarTypeDtRef(equipement.getEqptGarTypeDtRef())
				            .eqptMachine(equipement.getEqptMachine())
				            .eqptDtCreation(equipement.getEqptDtCreation())
				            .eqptDureeGarantie(equipement.getEqptDureeGarantie())
				            .dateFinGarantie(equipement.getDateFinGarantie())
				           
				            .eqptLotNumero(equipement.getEqptLotNumero())
				            .eqptNumeroSerie(equipement.getEqptNumeroSerie())
				            .dateFabrication(equipement.getDateFabrication())
				            .dateInstallation(equipement.getDateInstallation())
				            .dateMiseEnService(equipement.getDateMiseEnService())
				           
				            .dateDemontage(equipement.getDateDemontage())
				            .dateRemplacement(equipement.getDateRemplacement())
				            .dateLivraison(equipement.getDateLivraison())
				            .type(equipement.getType())
				            .client(equipement.getClient())
				            .famille(equipement.getFamille())
				           
				           
				            .build();
				}
				
					@Override
					public Equipement updateEquipement(Equipement Equipement) {
					return equipementRepository.save(Equipement);
					}


	
	@Override
	public void deleteEquipement(Equipement eqpt) {
		equipementRepository.delete(eqpt);
	}
	
	
	 @Override
	public void deleteEquipementByCode(String code) {
		 equipementRepository.deleteById(code);
	}
	 
	 
	@Override
	public Equipement getEquipement(String code) {
			return equipementRepository.findById(code).get();
	}
	
	
	@Override
	public List<Equipement> getAllEquipements() {
			return equipementRepository.findAll();
	}


	
	@Override
	public List<Equipement> findByEqptDesignation(String desing)
	{
		return equipementRepository.findByEqptDesignation(desing);
	}
	@Override
	public List<Equipement> findByEqptDesignationContains(String desing)
	{
		return equipementRepository.findByEqptDesignationContains(desing);
	}

	@Override
	public List<Equipement> findByDesingPrix ( String desing,Double prix)
	{
		return equipementRepository.findByDesingPrix(desing,prix);
	}


	
	@Override
	public List<Equipement> findByTypeEqtyCode(String eqtyCode)
	{
		return equipementRepository.findByTypeEqtyCode( eqtyCode);
				
	}

	


	@Override
	public List<Equipement> findByClientCodeClient(Long codeClient)
	{
		return equipementRepository.findByClientCodeClient( codeClient);
				
	}

	@Override
	public List<EquipementDTO> getAllEquipementsDTOs() {
		// TODO Auto-generated method stub
		return null;
	}

	
	
	
	
	
	
	
}
