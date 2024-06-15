package com.rihab.interventions.service;

import java.util.List;
import java.util.Optional;

import com.rihab.interventions.entities.Demandeur;
import com.rihab.interventions.entities.Manager;
import com.rihab.interventions.entities.Ticket;

public interface ManagerService {


	Manager saveManager(Manager manager);
	Manager updateManager(Manager manager);
void deleteManager(Manager manager);
 void deleteManagerByCode(long code);
 Manager getManager(long code);
List<Manager> getAllManagers();
Manager findByUsername(String name);

}
