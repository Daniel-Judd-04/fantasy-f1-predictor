package com.danieljudd.formula1.fantasyf1predictor.repository;

import com.danieljudd.formula1.fantasyf1predictor.model.Circuit;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CircuitRepository extends JpaRepository<Circuit, Long> {

  Circuit findByCircuitId(int circuitId);

  Circuit findByFullName(String fullName);

  List<Circuit> findByCountry(String country);

}
