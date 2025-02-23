package com.danieljudd.formula1.fantasyf1predictor.service;

import com.danieljudd.formula1.fantasyf1predictor.DTO.DriverDTO;
import com.danieljudd.formula1.fantasyf1predictor.model.Constructor;
import com.danieljudd.formula1.fantasyf1predictor.model.Driver;
import com.danieljudd.formula1.fantasyf1predictor.repository.ConstructorRepository;
import com.danieljudd.formula1.fantasyf1predictor.repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DriverService {

  @Autowired
  private DriverRepository driverRepository;

  @Autowired
  private ConstructorRepository constructorRepository;

  public Driver updateDriver(DriverDTO driver) {
    // Assuming driverId is part of the Driver object, find the existing driver by ID
    Driver existingDriver = driverRepository.findByDriverId(driver.getDriverId());

    Constructor constructor = constructorRepository.findByConstructorId(driver.getConstructor());

    if (existingDriver == null) {
      throw new IllegalArgumentException(
          "Driver " + driver.getFullName() + " not found with id "
              + driver.getDriverId());
    } else if (constructor == null) {
      throw new IllegalArgumentException(
          "Constructor with id " + driver.getConstructor() + " not found");
    }

    // Update fields
    existingDriver.setFullName(driver.getFullName());
    existingDriver.setCountry(driver.getCountry());
    existingDriver.setConstructor(constructor);
    existingDriver.setPoints(driver.getPoints());
    existingDriver.setFantasyPoints(driver.getFantasyPoints());
    existingDriver.setFantasyPrice(driver.getFantasyPrice());
    existingDriver.setActive(driver.isActive());

    // Save and return the updated driver
    return driverRepository.save(existingDriver);
  }

  public Driver addDriver(DriverDTO driverDTO) {
    // Check if a driver with the same ID already exists
    if (driverRepository.findByShortName(driverDTO.getShortName()) != null) {
      throw new IllegalArgumentException(
          "Driver with short name " + driverDTO.getShortName() + " already exists");
    }

    Constructor constructor = constructorRepository.findByConstructorId(
        driverDTO.getConstructor());
    if (constructor == null) {
      throw new IllegalArgumentException(
          "Constructor with id " + driverDTO.getConstructor() + " not found");
    }

    Driver driver = new Driver();
    driver.setShortName(driverDTO.getShortName());
    driver.setFullName(driverDTO.getFullName());
    driver.setCountry(driverDTO.getCountry());
    driver.setCarNumber(driverDTO.getCarNumber());
    driver.setConstructor(constructor);
    driver.setFantasyPrice(driverDTO.getFantasyPrice());
    driver.setActive(driverDTO.isActive());

    System.out.println("Adding driver: " + driver + " with constructor " + constructor);
    return driverRepository.save(driver);
  }

  public boolean deleteDriver(int id) {
    if (driverRepository.findByDriverId(id) == null) {
      throw new IllegalArgumentException("Driver with id=" + id + " not found");
    }

    driverRepository.deleteById(id);
    return true;
  }
}
