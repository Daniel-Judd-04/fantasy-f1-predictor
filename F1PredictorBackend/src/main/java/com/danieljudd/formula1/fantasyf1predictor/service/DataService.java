package com.danieljudd.formula1.fantasyf1predictor.service;

import com.danieljudd.formula1.fantasyf1predictor.repository.DriverRepository;
import com.danieljudd.formula1.fantasyf1predictor.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataService {

  @Autowired
  private TeamRepository teamRepository;

  @Autowired
  private DriverRepository driverRepository;

  //  @PostConstruct
  public void createTeamAndDrivers() {
//    try {
//      // Create a team
//      Team RBR = new Team("RBR", "Red Bull Racing", "Austria");
//      Team MCL = new Team("MCL", "Mclaren", "United Kingdom");
//      Team FER = new Team("FER", "Ferrari", "Italy");
//      Team MEC = new Team("MEC", "Mercedes", "Germany");
//      Team AST = new Team("AST", "Aston Martin", "United Kingdom");
//      Team VRB = new Team("VRB", "RB", "Austria");
//      Team HAA = new Team("HAA", "Haas", "United States");
//      Team ALP = new Team("ALP", "Alpine", "France");
//      Team WIL = new Team("WIL", "Williams", "United Kingdom");
//      Team KIC = new Team("KIC", "Kick Sauber", "Switzerland");
//
//      teamRepository.save(RBR);
//      teamRepository.save(MCL);
//      teamRepository.save(FER);
//      teamRepository.save(MEC);
//      teamRepository.save(AST);
//      teamRepository.save(VRB);
//      teamRepository.save(HAA);
//      teamRepository.save(ALP);
//      teamRepository.save(WIL);
//      teamRepository.save(KIC);
//
//      // Create drivers
//      Driver driver1 = new Driver("VER", "Max Verstappen", "Netherlands", 1, RBR);
//      Driver driver2 = new Driver("PER", "Sergio Perez", "Mexico", 11, RBR);
//
//      Driver driver3 = new Driver("LEC", "Charles Leclerc", "Monaco", 16, FER);
//      Driver driver4 = new Driver("SAI", "Carlos Sainz", "Spain", 55, FER);
//
//      Driver driver5 = new Driver("HAM", "Lewis Hamilton", "United Kingdom", 44, MEC);
//      Driver driver6 = new Driver("RUS", "George Russel", "United Kingdom", 63, MEC);
//
//      Driver driver7 = new Driver("NOR", "Lando Norris", "United Kingdom", 4, MCL);
//      Driver driver8 = new Driver("PIA", "Oscar Piastri", "Australia", 81, MCL);
//
//      Driver driver9 = new Driver("ALO", "Fernando Alonso", "Spain", 14, AST);
//      Driver driver10 = new Driver("STR", "Lance Stroll", "Canada", 18, AST);
//
//      Driver driver11 = new Driver("OCO", "Esteban Ocon", "France", 31, ALP);
//      Driver driver12 = new Driver("GAS", "Pierre Gasly", "France", 10, ALP);
//
//      Driver driver13 = new Driver("MAG", "Kevin Magnussen", "Denmark", 20, HAA);
//      Driver driver14 = new Driver("HUL", "Nico Hulkenberg", "Germany", 27, HAA);
//
//      Driver driver15 = new Driver("TSU", "Yuki Tsunoda", "Japan", 22, VRB);
//      Driver driver16 = new Driver("LAW", "Liam Lawson", "New Zealand", 30, VRB);
//
//      Driver driver17 = new Driver("ALB", "Alexander Albon", "Thailand", 23, WIL);
//      Driver driver18 = new Driver("SAR", "Logan Sargeant", "United States", 2, WIL);
//
//      Driver driver19 = new Driver("ZHO", "Zhou Guanyu", "China", 24, KIC);
//      Driver driver20 = new Driver("BOT", "Valtteri Bottas", "Finland", 77, KIC);
//
//      // Save drivers
//      driverRepository.save(driver1);
//      driverRepository.save(driver2);
//      driverRepository.save(driver3);
//      driverRepository.save(driver4);
//      driverRepository.save(driver5);
//      driverRepository.save(driver6);
//      driverRepository.save(driver7);
//      driverRepository.save(driver8);
//      driverRepository.save(driver9);
//      driverRepository.save(driver10);
//      driverRepository.save(driver11);
//      driverRepository.save(driver12);
//      driverRepository.save(driver13);
//      driverRepository.save(driver14);
//      driverRepository.save(driver15);
//      driverRepository.save(driver16);
//      driverRepository.save(driver17);
//      driverRepository.save(driver18);
//      driverRepository.save(driver19);
//      driverRepository.save(driver20);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
  }
}

