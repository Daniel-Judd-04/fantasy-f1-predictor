package com.danieljudd.formula1.fantasyf1predictor.controller;

import com.danieljudd.formula1.fantasyf1predictor.model.Constructor;
import com.danieljudd.formula1.fantasyf1predictor.repository.ConstructorRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/constructors")
public class ConstructorController {

  @Autowired
  private ConstructorRepository constructorRepository;

  @GetMapping
  public List<Constructor> getAllConstructors() {
    return constructorRepository.findAll();
  }

  @GetMapping("/size")
  public long getConstructorCount() {
    return constructorRepository.count();
  }

  @GetMapping("/id={id}")
  public Constructor getConstructorById(@PathVariable Long id) {
    return constructorRepository.findById(id).orElse(null);
  }

  @GetMapping("/shortName={shortName}")
  public Constructor getConstructorByShortName(@PathVariable String shortName) {
    return constructorRepository.findByShortName(shortName.toUpperCase());
  }
}
