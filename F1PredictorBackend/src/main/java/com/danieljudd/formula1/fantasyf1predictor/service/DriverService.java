package com.danieljudd.formula1.fantasyf1predictor.service;

import com.danieljudd.formula1.fantasyf1predictor.model.Driver;
import com.danieljudd.formula1.fantasyf1predictor.repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DriverService {

  @Autowired
  private DriverRepository driverRepository;

  public Driver updateDriver(Driver driver) {
    // Assuming driverId is part of the Driver object, find the existing driver by ID
    Driver existingDriver = driverRepository.findByDriverId(driver.getDriverId());

    if (existingDriver == null) {
      // If the driver does not exist, throw an exception
      throw new IllegalArgumentException("Driver not found");
    }

    // Update fields
    existingDriver.setFullName(driver.getFullName());
    existingDriver.setPoints(driver.getPoints());
    existingDriver.setFantasyPoints(driver.getFantasyPoints());
    existingDriver.setFantasyPrice(driver.getFantasyPrice());

    // Save and return the updated driver
    return driverRepository.save(existingDriver);
  }
}
