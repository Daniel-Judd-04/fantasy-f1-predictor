package com.danieljudd.formula1.fantasyf1predictor.controller;

import com.danieljudd.formula1.fantasyf1predictor.model.Constructor;
import com.danieljudd.formula1.fantasyf1predictor.repository.TeamRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

  @Autowired
  private TeamRepository teamRepository;

  @GetMapping
  public List<Constructor> getAllTeams() {
    return teamRepository.findAll();
  }

  @GetMapping("/size")
  public long getTeamCount() {
    return teamRepository.count();
  }

  @GetMapping("/id={id}")
  public Constructor getTeamById(@PathVariable Long id) {
    return teamRepository.findById(id).orElse(null);
  }

  @GetMapping("/shortName={shortName}")
  public Constructor getTeamByShortName(@PathVariable String shortName) {
    return teamRepository.findByShortName(shortName.toUpperCase());
  }
}