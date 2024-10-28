package com.danieljudd.formula1.fantasyf1predictor.controller;

import com.danieljudd.formula1.fantasyf1predictor.model.Driver;
import com.danieljudd.formula1.fantasyf1predictor.repository.DriverRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/drivers")
public class DriverController {

  @Autowired
  private DriverRepository driverRepository;

  @GetMapping
  public List<Driver> getAllDrivers() {
    return driverRepository.findAll();
  }

  @GetMapping("/{id}")
  public Driver getDriverById(@PathVariable Long id) {
    return driverRepository.findById(id).orElse(null);
  }

  @PostMapping
  public Driver createDriver(@RequestBody Driver driver) {
    return driverRepository.save(driver);
  }

  @PutMapping("/{id}")
  public Driver updateDriver(@PathVariable Long id, @RequestBody Driver driverDetails) {
    Driver driver = driverRepository.findById(id).orElse(null);
    if (driver != null) {
      driver.setName(driverDetails.getName());
      driver.setNationality(driverDetails.getNationality());
      driver.setNumber(driverDetails.getNumber());
      driver.setTeam(driverDetails.getTeam());
      return driverRepository.save(driver);
    } else {
      return null;
    }
  }

  @DeleteMapping("/{id}")
  public void deleteDriver(@PathVariable Long id) {
    driverRepository.deleteById(id);
  }
}
