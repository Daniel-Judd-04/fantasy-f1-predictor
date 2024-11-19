package com.danieljudd.formula1.fantasyf1predictor.service;

import com.danieljudd.formula1.fantasyf1predictor.repository.GrandPrixRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GrandPrixService {

  @Autowired
  private GrandPrixRepository grandPrixRepository;

  public int getNumberOfRacesFromSeason(int season) {
    return grandPrixRepository.findBySeason(season).size();
  }

}
