package com.danieljudd.formula1.fantasyf1predictor.service;

import com.danieljudd.formula1.fantasyf1predictor.DTO.TeamInputDTO;
import com.danieljudd.formula1.fantasyf1predictor.DTO.TeamOutputDTO;
import com.danieljudd.formula1.fantasyf1predictor.model.Constructor;
import com.danieljudd.formula1.fantasyf1predictor.model.Driver;
import com.danieljudd.formula1.fantasyf1predictor.model.Team;
import com.danieljudd.formula1.fantasyf1predictor.repository.ConstructorRepository;
import com.danieljudd.formula1.fantasyf1predictor.repository.DriverRepository;
import com.danieljudd.formula1.fantasyf1predictor.repository.TeamRepository;
import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
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

  @Autowired
  private GrandPrixService grandPrixService;

  private int numberOfRaces;

  @PostConstruct
  public void init() {
    // Calendar.getInstance().get(Calendar.YEAR) is not used currently to avoid issues with the current year
    numberOfRaces = Math.max(1,
        grandPrixService.getNumberOfRacesFromSeason(2023));
  }

  // Input:

  public Team addTeam(TeamInputDTO teamInputDTO) {
    if (teamRepository.findByTeamOwnerAndTeamName(teamInputDTO.getTeamOwner(),
        teamInputDTO.getTeamName())
        != null) {
      throw new IllegalArgumentException(
          "Team " + teamInputDTO.getTeamName() + " already exists for owner "
              + teamInputDTO.getTeamOwner());
    }

    Team team = convertTeamDTOToTeam(teamInputDTO);

    return teamRepository.save(team);
  }

  public Team updateTeam(TeamInputDTO teamInputDTO) {
    if (teamRepository.findByTeamOwnerAndTeamName(teamInputDTO.getTeamOwner(),
        teamInputDTO.getTeamName())
        == null) {
      System.out.println(
          "Team " + teamInputDTO.getTeamName() + " does not exist for owner "
              + teamInputDTO.getTeamOwner()
              + ". Adding new team instead");
      return addTeam(teamInputDTO);
    } else {
      Team team = convertTeamDTOToTeam(teamInputDTO);
      return teamRepository.save(team);
    }
  }

  private BigDecimal calculateCostCap(TeamInputDTO teamInputDTO) {
    BigDecimal costCap = teamInputDTO.getRemainingBudget();
    for (int driverId : teamInputDTO.getDriverIds()) {
      costCap = costCap.add(driverRepository.findByDriverId(driverId).getFantasyPrice());
    }
    for (int constructorId : teamInputDTO.getConstructorIds()) {
      costCap = costCap.add(
          constructorRepository.findByConstructorId(constructorId).getFantasyPrice());
    }
    return costCap;
  }

  private Team convertTeamDTOToTeam(TeamInputDTO teamInputDTO) {
    if (teamInputDTO.getDriverIds().size() != 5) {
      throw new IllegalArgumentException("Team must have 5 drivers");
    } else if (teamInputDTO.getConstructorIds().size() != 2) {
      throw new IllegalArgumentException("Team must have 2 constructors");
    }

    Team team = new Team();

    team.setTeamOwner(teamInputDTO.getTeamOwner());
    team.setTeamName(teamInputDTO.getTeamName());

    // Calculate cc from remaining budget and selected drivers/constructors
    team.setCostCap(calculateCostCap(teamInputDTO));
    team.setFreeTransfers(teamInputDTO.getFreeTransfers());
    team.setActiveChip(teamInputDTO.getActiveChip());

    Set<Driver> drivers = new HashSet<>();
    for (int driverId : teamInputDTO.getDriverIds()) {
      drivers.add(driverRepository.findByDriverId(driverId));
    }
    team.setDrivers(drivers);

    Set<Constructor> constructors = new HashSet<>();
    for (int constructorId : teamInputDTO.getConstructorIds()) {
      constructors.add(constructorRepository.findByConstructorId(constructorId));
    }
    team.setConstructors(constructors);

    return team;
  }

  // Output:

  public List<Team> getRecommendedTeams(int limit) {
    List<Team> teams = new ArrayList<>();

    for (int i = 0; i < limit; i++) {
      Team team = new Team();
      team.setTeamOwner("Recommended");
      team.setTeamName("Team " + i);

      Set<Driver> drivers = new HashSet<>();
      while (drivers.size() < 5) {
        drivers.add(driverRepository.findByDriverId((int) (Math.random() * 20) + 1));
      }
      team.setDrivers(drivers);

      Set<Constructor> constructors = new HashSet<>();
      while (constructors.size() < 2) {
        constructors.add(constructorRepository.findByConstructorId((int) (Math.random() * 10) + 1));
      }
      team.setConstructors(constructors);

      team.setCostCap(calculateValue(team));
      team.setFreeTransfers((byte) 0);
      team.setActiveChip("None");

      teams.add(team);
    }

    return teams;
  }

  public List<TeamOutputDTO> convertTeamsToDTOs(List<Team> teams) {
    return teams.stream().map(this::convertTeamToDTO).collect(Collectors.toList());
  }

  public TeamOutputDTO convertTeamToDTO(Team team) {
    TeamOutputDTO teamOutputDTO = new TeamOutputDTO();

    teamOutputDTO.setTeamOwner(team.getTeamOwner());
    teamOutputDTO.setTeamName(team.getTeamName());

    teamOutputDTO.setValue(calculateValue(team));
    teamOutputDTO.setFreeTransfers(team.getFreeTransfers());
    teamOutputDTO.setActiveChip(team.getActiveChip());

    teamOutputDTO.setDrivers(team.getDrivers());
    teamOutputDTO.setConstructors(team.getConstructors());

    teamOutputDTO.setAverageFantasyPoints(calculateAverageFantasyPoints(team));
    teamOutputDTO.setAverageRecentPoints(calculateAverageRecentPoints(team));
    teamOutputDTO.setAveragePoints(calculateAveragePoints(team));
    teamOutputDTO.setConsistency(calculateConsistency(team));

    return teamOutputDTO;
  }

  private BigDecimal calculateValue(Team team) {
    BigDecimal value = new BigDecimal("0.0");
    for (Driver driver : team.getDrivers()) {
      value = value.add(driver.getFantasyPrice());
    }
    for (Constructor constructor : team.getConstructors()) {
      value = value.add(constructor.getFantasyPrice());
    }
    return value;
  }

  private float calculateAverageFantasyPoints(Team team) {
    float totalFantasyPoints = 0;

    for (Driver driver : team.getDrivers()) {
      totalFantasyPoints += driver.getFantasyPoints();
    }
    for (Constructor constructor : team.getConstructors()) {
      totalFantasyPoints += constructor.getFantasyPoints();
    }
    System.out.println(
        "Total fantasy points: " + totalFantasyPoints + " - Average Fantasy Points; " + (
            totalFantasyPoints / numberOfRaces));
    return totalFantasyPoints / numberOfRaces;
  }

  private float calculateAverageRecentPoints(Team team) {
    float totalRecentPoints = 0;

    for (int i = 0; i < team.getDrivers().size(); i++) {
      totalRecentPoints +=
          (i + 1) * team.getDrivers().toArray(new Driver[0])[i].getPoints().floatValue();
    }
    for (Constructor constructor : team.getConstructors()) {
      for (int i = 0; i < constructor.getDrivers().size(); i++) {
        totalRecentPoints +=
            (i + 1) * constructor.getDrivers().toArray(new Driver[0])[i].getPoints().floatValue();
      }
    }
    System.out.println(
        "Total recent points: " + totalRecentPoints + " - Average Recent Points; " + (
            totalRecentPoints / numberOfRaces));
    return totalRecentPoints / numberOfRaces;
  }

  private float calculateAveragePoints(Team team) {
    float totalPoints = 0;

    for (Driver driver : team.getDrivers()) {
      totalPoints += driver.getPoints().floatValue();
    }
    for (Constructor constructor : team.getConstructors()) {
      for (Driver driver : constructor.getDrivers()) {
        totalPoints += driver.getPoints().floatValue();
      }
    }
    System.out.println(
        "Total points: " + totalPoints + " - Average Points; " + (totalPoints / numberOfRaces));
    return totalPoints / numberOfRaces;
  }

  private float calculateConsistency(Team team) {
    float averagePoints = calculateAveragePoints(team);
    double totalVariance = 0;

    for (Driver driver : team.getDrivers()) {
      totalVariance += Math.abs(driver.getPoints().floatValue() - averagePoints);
    }
    for (Constructor constructor : team.getConstructors()) {
      for (Driver driver : constructor.getDrivers()) {
        totalVariance += Math.abs(driver.getPoints().floatValue() - averagePoints);
      }
    }
    float maxVariance = Math.max(1, numberOfRaces * ((25 + 18 + 1) * 2));
    float variance = (float) (1 - (totalVariance / maxVariance));
    System.out.println(
        "Variance: " + variance + " - Max Variance: " + maxVariance + " - Total Variance: "
            + totalVariance);
    return variance * 1000;
  }
}
