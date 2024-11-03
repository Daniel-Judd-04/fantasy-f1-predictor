package com.danieljudd.formula1.fantasyf1predictor.repository;

import com.danieljudd.formula1.fantasyf1predictor.model.GrandPrix;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GrandPrixRepository extends JpaRepository<GrandPrix, Long> {

  GrandPrix findByGrandPrixId(int circuitId);

  List<GrandPrix> findBySeason(int season);

}
