package com.danieljudd.formula1.fantasyf1predictor.controller;

import com.danieljudd.formula1.fantasyf1predictor.DTO.UserTeamInputDTO;
import com.danieljudd.formula1.fantasyf1predictor.DTO.UserTeamOutputDTO;
import com.danieljudd.formula1.fantasyf1predictor.model.team.UserTeam;
import com.danieljudd.formula1.fantasyf1predictor.repository.UserTeamRepository;
import com.danieljudd.formula1.fantasyf1predictor.service.UserTeamService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user-teams")
public class UserTeamController {

  private final UserTeamRepository userTeamRepository;

  private final UserTeamService userTeamService;

  public UserTeamController(UserTeamRepository userTeamRepository,
      UserTeamService userTeamService) {
    this.userTeamRepository = userTeamRepository;
    this.userTeamService = userTeamService;
  }

  @GetMapping()
  public List<UserTeamOutputDTO> getUserTeams() {
    return userTeamService.convertToOutputDTOs(userTeamRepository.findAll());
  }

  @PostMapping
  public ResponseEntity<?> addTeam(@RequestBody UserTeamInputDTO userTeamInputDTO) {
    System.out.println("Adding new team: " + userTeamInputDTO.getName());
    UserTeam newUserTeam = userTeamService.addUserTeam(userTeamInputDTO);
    if (newUserTeam != null) {
      return ResponseEntity.ok(newUserTeam);
    }
    return ResponseEntity.badRequest().body("Invalid user team creation request");
  }

  @PutMapping
  public ResponseEntity<?> updateTeam(@RequestBody UserTeamInputDTO userTeamInputDTO) {
    UserTeam updatedUserTeam = userTeamService.updateUserTeam(userTeamInputDTO);
    if (updatedUserTeam != null) {
      return ResponseEntity.ok(updatedUserTeam);
    }
    return ResponseEntity.badRequest().body("Invalid user team update request");
  }

}
