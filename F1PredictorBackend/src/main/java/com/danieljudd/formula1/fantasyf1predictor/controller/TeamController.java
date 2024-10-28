package com.danieljudd.formula1.fantasyf1predictor.controller;

import com.danieljudd.formula1.fantasyf1predictor.model.Team;
import com.danieljudd.formula1.fantasyf1predictor.repository.TeamRepository;
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
@RequestMapping("/api/teams")
public class TeamController {

  @Autowired
  private TeamRepository teamRepository;

  @GetMapping
  public List<Team> getAllTeams() {
    return teamRepository.findAll();
  }

  @GetMapping("/{id}")
  public Team getTeamById(@PathVariable Long id) {
    return teamRepository.findById(id).orElse(null);
  }

  @PostMapping
  public Team createTeam(@RequestBody Team team) {
    return teamRepository.save(team);
  }

  @PutMapping("/{id}")
  public Team updateTeam(@PathVariable Long id, @RequestBody Team teamDetails) {
    Team team = teamRepository.findById(id).orElse(null);
    if (team != null) {
      team.setName(teamDetails.getName());
      team.setCountry(teamDetails.getCountry());
      // Update other fields as necessary
      return teamRepository.save(team);
    } else {
      return null;
    }
  }

  @DeleteMapping("/{id}")
  public void deleteTeam(@PathVariable Long id) {
    teamRepository.deleteById(id);
  }
}
