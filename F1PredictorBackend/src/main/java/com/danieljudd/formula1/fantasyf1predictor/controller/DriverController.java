package com.danieljudd.formula1.fantasyf1predictor.controller;

import com.danieljudd.formula1.fantasyf1predictor.model.Driver;
import com.danieljudd.formula1.fantasyf1predictor.repository.DriverRepository;
import com.danieljudd.formula1.fantasyf1predictor.service.DriverService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/drivers")
public class DriverController {

  @Autowired
  private DriverRepository driverRepository;

  @Autowired
  private DriverService driverService;

  @GetMapping
  public List<Driver> getAllDrivers() {
    return driverRepository.findAll();
  }

  @GetMapping("/size")
  public long getDriverCount() {
    return driverRepository.count();
  }

  @GetMapping("/id={id}")
  public Driver getDriverById(@PathVariable Long id) {
    return driverRepository.findById(id).orElse(null);
  }

  @GetMapping("/shortName={shortName}")
  public Driver getDriverByShortName(@PathVariable String shortName) {
    return driverRepository.findByShortName(shortName.toUpperCase());
  }

  @PutMapping
  public ResponseEntity<?> updateDriver(@RequestBody Driver driver) {
    Driver updatedDriver = driverService.updateDriver(driver);
    return ResponseEntity.ok(updatedDriver);
  }
}
