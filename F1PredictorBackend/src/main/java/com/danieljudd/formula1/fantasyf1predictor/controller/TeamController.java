package com.danieljudd.formula1.fantasyf1predictor.controller;

import com.danieljudd.formula1.fantasyf1predictor.DTO.TeamDTO;
import com.danieljudd.formula1.fantasyf1predictor.model.Team;
import com.danieljudd.formula1.fantasyf1predictor.repository.TeamRepository;
import com.danieljudd.formula1.fantasyf1predictor.service.TeamService;
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
@RequestMapping("/api/teams")
public class TeamController {

  @Autowired
  private TeamRepository teamRepository;

  @Autowired
  private TeamService teamService;

  @GetMapping
  public List<Team> getAllTeams() {
    return teamRepository.findAll();
  }

  @GetMapping("/user")
  public List<Team> getUserTeams() {
    List<Team> teams = teamRepository.findAll();
    teams.removeIf(team -> team.getTeamOwner().equals("Generated-by-F1-Predictor"));
    return teams;
  }

  @GetMapping("/teamOwner={teamOwner}&teamName={teamName}")
  public Team getTeam(@PathVariable String teamOwner, @PathVariable String teamName) {
    Team team = teamRepository.findByTeamOwnerAndTeamName(teamOwner, teamName);
    if (team == null) {
      throw new IllegalArgumentException(
          "Team not found for owner: " + teamOwner + " and name: " + teamName);
    }
    return team;
  }

  @PutMapping
  public ResponseEntity<?> updateTeam(@RequestBody TeamDTO teamDTO) {
    Team updatedTeam = teamService.updateTeam(teamDTO);
    if (updatedTeam != null) {
      return ResponseEntity.ok(updatedTeam);
    }
    return ResponseEntity.badRequest().body("Invalid team update request");
  }
}
