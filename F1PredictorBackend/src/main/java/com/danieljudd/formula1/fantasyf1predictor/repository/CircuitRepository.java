package com.danieljudd.formula1.fantasyf1predictor.repository;

import com.danieljudd.formula1.fantasyf1predictor.model.Circuit;
import java.time.Instant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CircuitRepository extends JpaRepository<Circuit, Long> {

  Circuit findByRound(int round);

  Circuit findByDate(Instant date);

  List<Circuit> findByCountry(String country);

  List<Circuit> findByIsSprint(boolean isSprint);

}
