package com.danieljudd.formula1.fantasyf1predictor.service;

import com.danieljudd.formula1.fantasyf1predictor.DTO.GrandPrixInputDTO;
import com.danieljudd.formula1.fantasyf1predictor.model.Circuit;
import com.danieljudd.formula1.fantasyf1predictor.model.GrandPrix;
import com.danieljudd.formula1.fantasyf1predictor.repository.CircuitRepository;
import com.danieljudd.formula1.fantasyf1predictor.repository.GrandPrixRepository;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GrandPrixService {

  @Autowired
  private GrandPrixRepository grandPrixRepository;

  @Autowired
  private CircuitService circuitService;

  @Autowired
  private CircuitRepository circuitRepository;

  public GrandPrix addGrandPrix(GrandPrixInputDTO grandPrixDTO) {
    if (grandPrixRepository.findByStartTime(grandPrixDTO.getStartTime()) != null) {
      throw new IllegalArgumentException(
          "Grand Prix with start time '" + grandPrixDTO.getStartTime() + "' already exists");
    }

    Circuit circuit;
    if (grandPrixDTO.getCircuit().getFullName() != null) { // New Circuit
      System.out.println("Adding new circuit: " + grandPrixDTO.getCircuit().getFullName());
      circuit = circuitService.addCircuit(grandPrixDTO.getCircuit());
    } else { // Existing Circuit
      circuit = circuitRepository.findByCircuitId(grandPrixDTO.getCircuit().getCircuitId());
    }

    GrandPrix newGrandPrix = new GrandPrix();
    newGrandPrix.setCircuit(circuit);
    newGrandPrix.setStartTime(grandPrixDTO.getStartTime());
    newGrandPrix.setFullName(grandPrixDTO.getFullName());
    newGrandPrix.setSprint(grandPrixDTO.isSprint());

    return grandPrixRepository.save(newGrandPrix);
  }

  public List<GrandPrix> getGrandPrixFromSeason(int season) {
    List<GrandPrix> grandsPrix = grandPrixRepository.findAll();
    List<GrandPrix> grandsPrixFromSameSeason = new ArrayList<>();
    for (GrandPrix gp : grandsPrix) {
      if (gp.getSeason() == season) {
        grandsPrixFromSameSeason.add(gp);
      }
    }
    Collections.sort(grandsPrixFromSameSeason);
    return grandsPrixFromSameSeason;
  }

  public int getNumberOfRaces(GrandPrix grandPrix) {
    // Find the numberOfRaces which have already happened in the current season
    int numberOfRaces;
    int year = grandPrix.getSeason();
    do {
      List<GrandPrix> grandPrixFromSeason = this.getGrandPrixFromSeason(year);
      numberOfRaces = 0;
      for (GrandPrix gp : grandPrixFromSeason) {
        if (gp.getStartTime().isAfter(Instant.now())) {
          break;
        } else {
          numberOfRaces++;
        }
      }
      year--;
    } while (numberOfRaces == 0 && year > 1950);

    return numberOfRaces;
  }
}
