package com.danieljudd.formula1.fantasyf1predictor.controller;

import com.danieljudd.formula1.fantasyf1predictor.model.GrandPrix;
import com.danieljudd.formula1.fantasyf1predictor.repository.GrandPrixRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/grands-prix")
public class GrandPrixController {

  @Autowired
  private GrandPrixRepository grandPrixRepository;

  @GetMapping
  public List<GrandPrix> getAllGrandPrix() {
    return grandPrixRepository.findAll();
  }

  @GetMapping("/id={id}")
  public GrandPrix getGrandPrixById(@PathVariable long id) {
    return grandPrixRepository.findById(id).orElse(null);
  }
}
