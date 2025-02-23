package com.danieljudd.formula1.fantasyf1predictor.service;

import com.danieljudd.formula1.fantasyf1predictor.DTO.UserTeamInputDTO;
import com.danieljudd.formula1.fantasyf1predictor.DTO.UserTeamOutputDTO;
import com.danieljudd.formula1.fantasyf1predictor.model.Constructor;
import com.danieljudd.formula1.fantasyf1predictor.model.Driver;
import com.danieljudd.formula1.fantasyf1predictor.model.team.UserTeam;
import com.danieljudd.formula1.fantasyf1predictor.repository.ConstructorRepository;
import com.danieljudd.formula1.fantasyf1predictor.repository.DriverRepository;
import com.danieljudd.formula1.fantasyf1predictor.repository.UserTeamRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class UserTeamService {

  private final UserTeamRepository userTeamRepository;
  private final DriverRepository driverRepository;
  private final ConstructorRepository constructorRepository;

  public UserTeamService(
      UserTeamRepository userTeamRepository,
      DriverRepository driverRepository,
      ConstructorRepository constructorRepository) {
    this.userTeamRepository = userTeamRepository;
    this.driverRepository = driverRepository;
    this.constructorRepository = constructorRepository;
  }

  public UserTeam addUserTeam(UserTeamInputDTO userTeamInputDTO) {
    if (userTeamRepository.findById(userTeamInputDTO.getTeamId()).isPresent()) {
      throw new IllegalArgumentException(
          "User Team with ID " + userTeamInputDTO.getTeamId() + " already exists"
      );
    }

    UserTeam userTeam = convertToUserTeam(userTeamInputDTO);

    return userTeamRepository.save(userTeam);
  }

  public UserTeam updateUserTeam(UserTeamInputDTO userTeamInputDTO) {
    if (userTeamRepository.findById(userTeamInputDTO.getTeamId()).isEmpty()) {
      throw new IllegalArgumentException(
          "User Team with ID " + userTeamInputDTO.getTeamId() + " does not exist"
      );
    }

    UserTeam userTeam = convertToUserTeam(userTeamInputDTO);
    return userTeamRepository.save(userTeam);
  }

  private UserTeam convertToUserTeam(UserTeamInputDTO userTeamInputDTO) {
    if (userTeamInputDTO.getDriverIds().size() != 5) {
      throw new IllegalArgumentException("Team must have 5 drivers");
    } else if (userTeamInputDTO.getConstructorIds().size() != 2) {
      throw new IllegalArgumentException("Team must have 2 constructors");
    }

    UserTeam userTeam = new UserTeam();

    userTeam.setTeamId(userTeamInputDTO.getTeamId());

    userTeam.setOwner(userTeamInputDTO.getOwner());
    userTeam.setName(userTeamInputDTO.getName());

    userTeam.setRemainingBudget(userTeamInputDTO.getRemainingBudget());
    userTeam.setFreeTransfers(userTeamInputDTO.getFreeTransfers());

    Set<Driver> drivers = new HashSet<>();
    for (int driverId : userTeamInputDTO.getDriverIds()) {
      drivers.add(driverRepository.findByDriverId(driverId));
    }
    userTeam.setDrivers(drivers);

    Set<Constructor> constructors = new HashSet<>();
    for (int constructorId : userTeamInputDTO.getConstructorIds()) {
      constructors.add(constructorRepository.findByConstructorId(constructorId));
    }
    userTeam.setConstructors(constructors);

    return userTeam;
  }

  public List<UserTeamOutputDTO> convertToOutputDTOs(List<UserTeam> teams) {
    return teams.stream().map(this::convertToOutputDTO).collect(Collectors.toList());
  }

  public UserTeamOutputDTO convertToOutputDTO(UserTeam team) {
    UserTeamOutputDTO userTeamOutputDTO = new UserTeamOutputDTO();

    userTeamOutputDTO.setTeamId(team.getTeamId());

    userTeamOutputDTO.setName(team.getName());
    userTeamOutputDTO.setOwner(team.getOwner());

    userTeamOutputDTO.setDrivers(team.getDrivers());
    userTeamOutputDTO.setConstructors(team.getConstructors());

    userTeamOutputDTO.setRemainingBudget(team.getRemainingBudget());
    userTeamOutputDTO.setFreeTransfers(team.getFreeTransfers());

    return userTeamOutputDTO;
  }
}
