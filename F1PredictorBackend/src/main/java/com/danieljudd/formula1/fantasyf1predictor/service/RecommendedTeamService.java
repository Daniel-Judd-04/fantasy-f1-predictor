package com.danieljudd.formula1.fantasyf1predictor.service;

import com.danieljudd.formula1.fantasyf1predictor.DTO.RecommendedTeamOutputDTO;
import com.danieljudd.formula1.fantasyf1predictor.model.Constructor;
import com.danieljudd.formula1.fantasyf1predictor.model.Driver;
import com.danieljudd.formula1.fantasyf1predictor.model.GrandPrix;
import com.danieljudd.formula1.fantasyf1predictor.model.team.Chip;
import com.danieljudd.formula1.fantasyf1predictor.model.team.RecommendedTeam;
import com.danieljudd.formula1.fantasyf1predictor.model.team.UserTeam;
import com.danieljudd.formula1.fantasyf1predictor.repository.ConstructorRepository;
import com.danieljudd.formula1.fantasyf1predictor.repository.DriverRepository;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class RecommendedTeamService {

  private final DriverRepository driverRepository;

  private final ConstructorRepository constructorRepository;

  private final GrandPrixService grandPrixService;

  private List<Chip> activeChips;
  private UserTeam comparableTeam;
  private GrandPrix grandPrix;

  public RecommendedTeamService(DriverRepository driverRepository,
      ConstructorRepository constructorRepository, GrandPrixService grandPrixService) {
    this.driverRepository = driverRepository;
    this.constructorRepository = constructorRepository;
    this.grandPrixService = grandPrixService;
  }

  public List<RecommendedTeam> getRecommendedTeams(
      List<Chip> activeChips,
      UserTeam comparableTeam,
      GrandPrix grandPrix) {
    this.activeChips = activeChips;
    this.comparableTeam = comparableTeam;
    this.grandPrix = grandPrix;

    System.out.println(
        "Grand Prix: " + grandPrix.getFullName() +
            "\n - Season: " + grandPrix.getSeason() +
            "\n - Round: " + grandPrix.getRound() +
            "\nSelected Chips: " + activeChips.toString() +
            "\nComparable team: " + comparableTeam.getName() +
            "\n - Total Budget: " + comparableTeam.calculateTotalBudget() +
            "\n - Max Transfers: " + comparableTeam.getFreeTransfers() +
            "\n - Drivers: " + comparableTeam.getDrivers().toString() +
            "\n - Constructors: " + comparableTeam.getConstructors().toString()
    );

    if (comparableTeam.getFreeTransfers() < 1 || comparableTeam.getFreeTransfers() > 3) {
      throw new IllegalArgumentException(
          "Comparable Team must have between 1 and 3 free transfers");
    } else if (comparableTeam.getDrivers().size() != 5) {
      throw new IllegalArgumentException("Comparable Team must have 5 drivers");
    } else if (comparableTeam.getConstructors().size() != 2) {
      throw new IllegalArgumentException("Comparable Team must have 2 constructors");
    }

    List<Driver> drivers = driverRepository.findAllByActive(true);
    if (drivers.size() > 20) {
      throw new IllegalArgumentException("Too many active drivers!");
    }

    List<Constructor> constructors = constructorRepository.findAllByActive(true);
    if (constructors.size() > 10) {
      throw new IllegalArgumentException("Too many active constructors!");
    }

    calcDriverAndConstructorData(grandPrix);

    System.out.println("Generating all valid teams...");
    List<RecommendedTeam> allTeams = generateAllCombinations(drivers, constructors);
    System.out.println("Generated " + allTeams.size() + " valid teams.");

    return allTeams;
  }

  private List<RecommendedTeam> generateAllCombinations(
      List<Driver> drivers,
      List<Constructor> constructors) {
    List<RecommendedTeam> allTeams = new ArrayList<>();
    List<Driver> selectedDrivers = new ArrayList<>();
    List<Constructor> selectedConstructors = new ArrayList<>();

    generateDriverCombinations(
        drivers,
        selectedDrivers,
        allTeams,
        constructors,
        selectedConstructors,
        0);
    return allTeams;
  }

  private void generateDriverCombinations(
      List<Driver> drivers,
      List<Driver> currentDrivers,
      List<RecommendedTeam> allTeams,
      List<Constructor> constructors,
      List<Constructor> currentConstructors,
      int driverStart) {
    if (currentDrivers.size() == 5) {
      generateConstructorCombinations(
          constructors,
          currentConstructors,
          allTeams,
          currentDrivers,
          0);
      return;
    }

    for (int i = driverStart; i < drivers.size(); i++) {
      currentDrivers.add(drivers.get(i));
      generateDriverCombinations(
          drivers,
          currentDrivers,
          allTeams,
          constructors,
          currentConstructors,
          i + 1);
      currentDrivers.remove(currentDrivers.size() - 1);
    }
  }

  @Transactional
  public void generateConstructorCombinations(
      List<Constructor> constructors,
      List<Constructor> currentConstructors,
      List<RecommendedTeam> allTeams,
      List<Driver> currentDrivers,
      int constructorStart
  ) {
    if (currentConstructors.size() == 2) {
      for (Chip chip : activeChips) {
        RecommendedTeam newTeam = new RecommendedTeam();
        newTeam.setDrivers(new HashSet<>(currentDrivers));
        newTeam.setConstructors(new HashSet<>(currentConstructors));
        newTeam.setActiveChip(chip);
        newTeam.calculateTotalCost();

        if (
            (chip.equals(Chip.LIMITLESS) || !isTooExpensive(newTeam)) &&
                (chip.equals(Chip.WILDCARD) || !isTooManyTransfers(newTeam))
        ) {
          allTeams.add(newTeam);
        }
      }
      return;
    }

    for (int i = constructorStart; i < constructors.size(); i++) {
      currentConstructors.add(constructors.get(i));
      generateConstructorCombinations(constructors, currentConstructors, allTeams, currentDrivers,
          i + 1);
      currentConstructors.remove(currentConstructors.size() - 1);
    }
  }

  private boolean isTooExpensive(RecommendedTeam team) {
    if (team.getActiveChip().equals(Chip.LIMITLESS)) {
      return false;
    }
    return comparableTeam.calculateTotalBudget().compareTo(team.getTotalCost()) < 0;
  }

  private boolean isTooManyTransfers(RecommendedTeam team) {
    if (team.getActiveChip().equals(Chip.WILDCARD)) {
      return false;
    }
    return team.calculateRequiredTransfers(comparableTeam) > comparableTeam.getFreeTransfers();
  }

  public List<RecommendedTeamOutputDTO> convertToOutputDTOs(List<RecommendedTeam> teams,
      int numberOfRaces) {
    return teams
        .stream()
        .map(team -> convertToOutputDTO(team, numberOfRaces))
        .collect(Collectors.toList());
  }

  public RecommendedTeamOutputDTO convertToOutputDTO(RecommendedTeam team, int numberOfRaces) {
    RecommendedTeamOutputDTO recommendedTeamOutputDTO = new RecommendedTeamOutputDTO();

    recommendedTeamOutputDTO.setActiveChip(team.getActiveChip().name());

    recommendedTeamOutputDTO.setDrivers(team.getDrivers());
    recommendedTeamOutputDTO.setConstructors(team.getConstructors());

    recommendedTeamOutputDTO.setTotalCost(team.getTotalCost());
    recommendedTeamOutputDTO.setTransfers(team.calculateRequiredTransfers(comparableTeam));

    recommendedTeamOutputDTO.setAverageFantasyPoints(
        team.calculateAverageFantasyPoints(numberOfRaces));
    recommendedTeamOutputDTO.setAverageRecentPoints(team.calculateAverageRecentPoints());
    recommendedTeamOutputDTO.setAveragePoints(team.calculateAveragePoints());
    recommendedTeamOutputDTO.setConsistency(team.calculateConsistency());

    return recommendedTeamOutputDTO;
  }

  private void calcDriverAndConstructorData(GrandPrix gp) {
    for (Driver driver : driverRepository.findAll()) {
      driver.setAvgRecPoints(gp);
      driver.setAvgPoints(gp);
      driver.setAvgVariance(gp);
    }
    for (Constructor constructor : constructorRepository.findAll()) {
      constructor.setAvgRecPoints(gp);
      constructor.setAvgPoints(gp);
      constructor.setAvgVariance(gp);
    }
  }
}
