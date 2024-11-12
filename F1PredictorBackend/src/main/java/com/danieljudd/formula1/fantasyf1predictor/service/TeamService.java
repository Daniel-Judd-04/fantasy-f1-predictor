package com.danieljudd.formula1.fantasyf1predictor.service;

import com.danieljudd.formula1.fantasyf1predictor.DTO.TeamDTO;
import com.danieljudd.formula1.fantasyf1predictor.model.Constructor;
import com.danieljudd.formula1.fantasyf1predictor.model.Driver;
import com.danieljudd.formula1.fantasyf1predictor.model.Team;
import com.danieljudd.formula1.fantasyf1predictor.repository.ConstructorRepository;
import com.danieljudd.formula1.fantasyf1predictor.repository.DriverRepository;
import com.danieljudd.formula1.fantasyf1predictor.repository.TeamRepository;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeamService {

  @Autowired
  private TeamRepository teamRepository;

  @Autowired
  private DriverRepository driverRepository;

  @Autowired
  private ConstructorRepository constructorRepository;

  public Team addTeam(TeamDTO teamDTO) {
    if (teamRepository.findByTeamOwnerAndTeamName(teamDTO.getTeamOwner(), teamDTO.getTeamName())
        != null) {
      throw new IllegalArgumentException(
          "Team " + teamDTO.getTeamName() + " already exists for owner " + teamDTO.getTeamOwner());
    }

    Team team = convertTeamDTOToTeam(teamDTO);

    return teamRepository.save(team);
  }

  public Team updateTeam(TeamDTO teamDTO) {
    if (teamRepository.findByTeamOwnerAndTeamName(teamDTO.getTeamOwner(), teamDTO.getTeamName())
        == null) {
      System.out.println(
          "Team " + teamDTO.getTeamName() + " does not exist for owner " + teamDTO.getTeamOwner()
              + ". Adding new team instead");
      return addTeam(teamDTO);
    } else {
      Team team = convertTeamDTOToTeam(teamDTO);
      return teamRepository.save(team);
    }
  }

  private BigDecimal calculateCostCap(TeamDTO teamDTO) {
    BigDecimal costCap = teamDTO.getRemainingBudget();
    for (int driverId : teamDTO.getDriverIds()) {
      costCap = costCap.add(driverRepository.findByDriverId(driverId).getFantasyPrice());
    }
    for (int constructorId : teamDTO.getConstructorIds()) {
      costCap = costCap.add(
          constructorRepository.findByConstructorId(constructorId).getFantasyPrice());
    }
    return costCap;
  }

  private Team convertTeamDTOToTeam(TeamDTO teamDTO) {
    if (teamDTO.getDriverIds().size() != 5) {
      throw new IllegalArgumentException("Team must have 5 drivers");
    } else if (teamDTO.getConstructorIds().size() != 2) {
      throw new IllegalArgumentException("Team must have 2 constructors");
    }

    Team team = new Team();

    team.setTeamOwner(teamDTO.getTeamOwner());
    team.setTeamName(teamDTO.getTeamName());

    // Calculate cc from remaining budget and selected drivers/constructors
    team.setCostCap(calculateCostCap(teamDTO));
    team.setFreeTransfers(teamDTO.getFreeTransfers());
    team.setActiveChip(teamDTO.getActiveChip());

    Set<Driver> drivers = new HashSet<>();
    for (int driverId : teamDTO.getDriverIds()) {
      drivers.add(driverRepository.findByDriverId(driverId));
    }
    team.setDrivers(drivers);

    Set<Constructor> constructors = new HashSet<>();
    for (int constructorId : teamDTO.getConstructorIds()) {
      constructors.add(constructorRepository.findByConstructorId(constructorId));
    }
    team.setConstructors(constructors);

    return team;
  }

}
