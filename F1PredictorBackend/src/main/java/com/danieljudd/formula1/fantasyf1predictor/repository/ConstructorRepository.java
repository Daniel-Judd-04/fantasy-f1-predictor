package com.danieljudd.formula1.fantasyf1predictor.repository;

import com.danieljudd.formula1.fantasyf1predictor.model.Constructor;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Constructor, Long> {

  // Find teams by nationality
  List<Constructor> findByCountry(String country);

  // Find team by short name
  Constructor findByShortName(String shortName);
}

