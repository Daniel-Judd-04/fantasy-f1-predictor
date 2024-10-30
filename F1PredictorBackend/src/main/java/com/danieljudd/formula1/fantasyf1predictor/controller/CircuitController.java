package com.danieljudd.formula1.fantasyf1predictor.controller;

import com.danieljudd.formula1.fantasyf1predictor.model.Circuit;
import com.danieljudd.formula1.fantasyf1predictor.repository.CircuitRepository;
import java.time.Instant;
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

  @GetMapping("/round={round}")
  public Circuit getCircuitByRound(@PathVariable int round) {
    return circuitRepository.findByRound(round);
  }

  @GetMapping("/date={givenDate}")
  public Circuit getCircuitByDate(@PathVariable String givenDate) {
    // Given format     "yyyy,MM,dd,HH,mm"
    // Example format   "2024,01,30,12,30"
    // Required format  "yyyy-MM-ddTHH:mm:00Z"
    String[] parts = givenDate.split(",");
    String formattedDate =
        parts[0] + "-" + parts[1] + "-" + parts[2] + "T" + parts[3] + ":" + parts[4] + ":00Z";
    return circuitRepository.findByDate(Instant.parse(formattedDate));
  }

  @GetMapping("/country={country}")
  public List<Circuit> getCircuitByCountry(@PathVariable String country) {
    return circuitRepository.findByCountry(country);
  }

  @GetMapping("/sprint={isSprint}")
  public List<Circuit> getCircuitBySprint(@PathVariable boolean isSprint) {
    return circuitRepository.findByIsSprint(isSprint);
  }

}
