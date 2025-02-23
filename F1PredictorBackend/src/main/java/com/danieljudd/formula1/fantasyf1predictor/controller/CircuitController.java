package com.danieljudd.formula1.fantasyf1predictor.controller;

import com.danieljudd.formula1.fantasyf1predictor.model.Circuit;
import com.danieljudd.formula1.fantasyf1predictor.repository.CircuitRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/circuits")
public class CircuitController {

  @Autowired
  private CircuitRepository circuitRepository;

  @GetMapping
  public List<Circuit> getAllCircuits() {
    return circuitRepository.findAll();
  }

  @GetMapping("/size")
  public long getCircuitCount() {
    return circuitRepository.count();
  }

  @GetMapping("/id={id}")
  public Circuit getCircuitById(@PathVariable Long id) {
    return circuitRepository.findById(id).orElse(null);
  }

  @GetMapping("/country={country}")
  public List<Circuit> getCircuitByCountry(@PathVariable String country) {
    return circuitRepository.findByCountry(country);
  }
}
