package com.danieljudd.formula1.fantasyf1predictor.repository;

import com.danieljudd.formula1.fantasyf1predictor.model.Constructor;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConstructorRepository extends JpaRepository<Constructor, Integer> {

  @EntityGraph(attributePaths = {"drivers"})
  Constructor findByConstructorId(int constructorId);

  List<Constructor> findByCountry(String country);

  Constructor findByShortName(String shortName);

  List<Constructor> findAllByActive(boolean active);


}

