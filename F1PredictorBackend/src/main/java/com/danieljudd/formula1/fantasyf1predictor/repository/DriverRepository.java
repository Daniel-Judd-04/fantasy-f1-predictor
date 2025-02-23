package com.danieljudd.formula1.fantasyf1predictor.repository;

import com.danieljudd.formula1.fantasyf1predictor.model.Driver;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepository extends JpaRepository<Driver, Integer> {

  @EntityGraph(attributePaths = {"constructor"})
  Driver findByDriverId(int driverId);

  List<Driver> findByCountry(String country);

  Driver findByShortName(String shortName);

  Driver findByCarNumber(byte carNumber);

  List<Driver> findAllByActive(boolean active);
}


