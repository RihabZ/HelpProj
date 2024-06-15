package com.rihab.interventions.service;

import java.util.List;

import com.rihab.interventions.entities.Magasinier;
import com.rihab.interventions.entities.User;

public interface MagasinierService {


	Magasinier saveMagasinier(Magasinier magasinier);
	Magasinier updateMagasinier(Magasinier magasinier);
			void deleteMagasinier(Magasinier magasinier);
			 void deleteMagasinierByCode(long code);
			 Magasinier getMagasinier(long code);
			List<User> getAllMagas();
			List<Magasinier> getAllMagasiniers();

}