package com.rihab.interventions.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import com.rihab.interventions.dto.MagasinierDTO;
import com.rihab.interventions.entities.Magasinier;
import com.rihab.interventions.entities.User;
import com.rihab.interventions.repos.MagasinierRepository;

@Service
public class MagasinierServiceImpl implements MagasinierService {
	
	@Autowired
	MagasinierRepository magasinierRepository;



@Override
public Magasinier saveMagasinier(Magasinier demandeur)
{
return magasinierRepository.save(demandeur);

}

@Override
public Magasinier updateMagasinier(Magasinier demandeur) {
return magasinierRepository.save(demandeur);
}


@Override
public void deleteMagasinier(Magasinier demandeur) {
	magasinierRepository.delete(demandeur);
}


@Override
public void deleteMagasinierByCode(long code) {
	magasinierRepository.deleteById(code);
}


@Override
public Magasinier getMagasinier(long code) {
return magasinierRepository.findById(code).get();
}

@Override
public List<User> getAllMagas() {
	// TODO Auto-generated method stub
	return null;
}




@Override
public List<Magasinier> getAllMagasiniers() {
return magasinierRepository.findAll();
}



}
