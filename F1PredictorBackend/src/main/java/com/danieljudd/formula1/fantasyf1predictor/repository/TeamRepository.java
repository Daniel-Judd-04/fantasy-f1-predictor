package com.danieljudd.formula1.fantasyf1predictor.repository;

import com.danieljudd.formula1.fantasyf1predictor.model.Team;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

  // Find teams by nationality
  List<Team> findByCountry(String country);

  // Find team by short name
  Team findByShortName(String shortName);
}

