package com.danieljudd.formula1.fantasyf1predictor.repository;

import com.danieljudd.formula1.fantasyf1predictor.model.Driver;
import com.danieljudd.formula1.fantasyf1predictor.model.Team;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepository extends JpaRepository<Driver, Long> {

  // Find drivers by nationality
  List<Driver> findByNationality(String nationality);

  // Find drivers by team
  List<Driver> findByTeam(Team team);

  // Find drivers by team name
  List<Driver> findByTeamName(String teamName);
}


