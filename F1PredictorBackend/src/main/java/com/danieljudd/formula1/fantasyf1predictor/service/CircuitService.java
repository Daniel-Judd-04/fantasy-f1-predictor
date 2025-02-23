package com.danieljudd.formula1.fantasyf1predictor.service;

import com.danieljudd.formula1.fantasyf1predictor.DTO.CircuitInputDTO;
import com.danieljudd.formula1.fantasyf1predictor.model.Circuit;
import com.danieljudd.formula1.fantasyf1predictor.repository.CircuitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CircuitService {

  @Autowired
  private CircuitRepository circuitRepository;

  public Circuit addCircuit(CircuitInputDTO circuitDTO) {
    if (circuitRepository.findByCircuitId(circuitDTO.getCircuitId()) != null) {
      throw new IllegalArgumentException(
          "Circuit with ID '" + circuitDTO.getCircuitId() + "' already exists");
    }

    Circuit newCircuit = new Circuit();
    newCircuit.setFullName(circuitDTO.getFullName());
    newCircuit.setLocationName(circuitDTO.getLocationName());
    newCircuit.setCountry(circuitDTO.getCountry());
    newCircuit.setTimezone(circuitDTO.getTimezone());

    return circuitRepository.save(newCircuit);
  }

}
