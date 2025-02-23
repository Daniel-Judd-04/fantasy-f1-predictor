package com.danieljudd.formula1.fantasyf1predictor.repository;

import com.danieljudd.formula1.fantasyf1predictor.model.GrandPrix;
import java.time.Instant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GrandPrixRepository extends JpaRepository<GrandPrix, Long> {

  GrandPrix findByGrandPrixId(int circuitId);
  
  GrandPrix findByStartTime(Instant startTime);

}
