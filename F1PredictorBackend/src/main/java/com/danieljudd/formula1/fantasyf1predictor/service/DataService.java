package com.danieljudd.formula1.fantasyf1predictor.service;

import com.danieljudd.formula1.fantasyf1predictor.model.Constructor;
import com.danieljudd.formula1.fantasyf1predictor.model.Driver;
import com.danieljudd.formula1.fantasyf1predictor.repository.CircuitRepository;
import com.danieljudd.formula1.fantasyf1predictor.repository.ConstructorRepository;
import com.danieljudd.formula1.fantasyf1predictor.repository.DriverRepository;
import com.danieljudd.formula1.fantasyf1predictor.repository.GrandPrixRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataService {

  private final boolean LOAD_DATA = false;

  @Autowired
  private ConstructorRepository constructorRepository;

  @Autowired
  private DriverRepository driverRepository;

  @Autowired
  private CircuitRepository circuitRepository;

  @Autowired
  private GrandPrixRepository grandPrixRepository;

  @PostConstruct
  public void addData() {
    if (LOAD_DATA) {
      createConstructorAndDrivers();
    }

  }

  private void createConstructorAndDrivers() {
    try {
//      Create a Constructor
      Constructor RBR = new Constructor("RBR", "Red Bull Racing", "Austria");
      Constructor MCL = new Constructor("MCL", "Mclaren", "United Kingdom");
      Constructor FER = new Constructor("FER", "Ferrari", "Italy");
      Constructor MEC = new Constructor("MEC", "Mercedes", "Germany");
      Constructor AST = new Constructor("AST", "Aston Martin", "United Kingdom");
      Constructor VRB = new Constructor("VRB", "RB", "Austria");
      Constructor HAA = new Constructor("HAA", "Haas", "United States");
      Constructor ALP = new Constructor("ALP", "Alpine", "France");
      Constructor WIL = new Constructor("WIL", "Williams", "United Kingdom");
      Constructor KIC = new Constructor("KIC", "Kick Sauber", "Switzerland");

      constructorRepository.save(RBR);
      constructorRepository.save(MCL);
      constructorRepository.save(FER);
      constructorRepository.save(MEC);
      constructorRepository.save(AST);
      constructorRepository.save(VRB);
      constructorRepository.save(HAA);
      constructorRepository.save(ALP);
      constructorRepository.save(WIL);
      constructorRepository.save(KIC);

      // Create drivers
      Driver driver1 = new Driver("VER", "Max Verstappen", "Netherlands", (byte) 1, RBR);
      Driver driver2 = new Driver("PER", "Sergio Perez", "Mexico", (byte) 11, RBR);

      Driver driver3 = new Driver("LEC", "Charles Leclerc", "Monaco", (byte) 16, FER);
      Driver driver4 = new Driver("SAI", "Carlos Sainz", "Spain", (byte) 55, FER);

      Driver driver5 = new Driver("HAM", "Lewis Hamilton", "United Kingdom", (byte) 44, MEC);
      Driver driver6 = new Driver("RUS", "George Russel", "United Kingdom", (byte) 63, MEC);

      Driver driver7 = new Driver("NOR", "Lando Norris", "United Kingdom", (byte) 4, MCL);
      Driver driver8 = new Driver("PIA", "Oscar Piastri", "Australia", (byte) 81, MCL);

      Driver driver9 = new Driver("ALO", "Fernando Alonso", "Spain", (byte) 14, AST);
      Driver driver10 = new Driver("STR", "Lance Stroll", "Canada", (byte) 18, AST);

      Driver driver11 = new Driver("OCO", "Esteban Ocon", "France", (byte) 31, ALP);
      Driver driver12 = new Driver("GAS", "Pierre Gasly", "France", (byte) 10, ALP);

      Driver driver13 = new Driver("MAG", "Kevin Magnussen", "Denmark", (byte) 20, HAA);
      Driver driver14 = new Driver("HUL", "Nico Hulkenberg", "Germany", (byte) 27, HAA);

      Driver driver15 = new Driver("TSU", "Yuki Tsunoda", "Japan", (byte) 22, VRB);
      Driver driver16 = new Driver("LAW", "Liam Lawson", "New Zealand", (byte) 30, VRB);

      Driver driver17 = new Driver("ALB", "Alexander Albon", "Thailand", (byte) 23, WIL);
      Driver driver18 = new Driver("SAR", "Logan Sargeant", "United States", (byte) 2, WIL);

      Driver driver19 = new Driver("ZHO", "Zhou Guanyu", "China", (byte) 24, KIC);
      Driver driver20 = new Driver("BOT", "Valtteri Bottas", "Finland", (byte) 77, KIC);

      // Save drivers
      driverRepository.save(driver1);
      driverRepository.save(driver2);
      driverRepository.save(driver3);
      driverRepository.save(driver4);
      driverRepository.save(driver5);
      driverRepository.save(driver6);
      driverRepository.save(driver7);
      driverRepository.save(driver8);
      driverRepository.save(driver9);
      driverRepository.save(driver10);
      driverRepository.save(driver11);
      driverRepository.save(driver12);
      driverRepository.save(driver13);
      driverRepository.save(driver14);
      driverRepository.save(driver15);
      driverRepository.save(driver16);
      driverRepository.save(driver17);
      driverRepository.save(driver18);
      driverRepository.save(driver19);
      driverRepository.save(driver20);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}

