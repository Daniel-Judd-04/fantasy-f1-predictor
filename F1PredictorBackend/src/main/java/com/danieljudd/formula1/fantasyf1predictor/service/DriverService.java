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

  public Driver updateDriver(Driver driver) {
    // Assuming driverId is part of the Driver object, find the existing driver by ID
    Driver existingDriver = driverRepository.findByDriverId(driver.getDriverId());

    if (existingDriver == null) {
      throw new IllegalArgumentException(
          "Driver " + driver.getFullName() + " not found with id "
              + driver.getDriverId());
    }

    // Update fields
    existingDriver.setFullName(driver.getFullName());
    existingDriver.setCountry(driver.getCountry());
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
        driverDTO.getConstructorId());
    if (constructor == null) {
      throw new IllegalArgumentException(
          "Constructor with id " + driverDTO.getConstructorId() + " not found");
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
}
