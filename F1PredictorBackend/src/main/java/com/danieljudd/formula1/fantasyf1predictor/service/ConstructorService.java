package com.danieljudd.formula1.fantasyf1predictor.service;

import com.danieljudd.formula1.fantasyf1predictor.model.Constructor;
import com.danieljudd.formula1.fantasyf1predictor.repository.ConstructorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConstructorService {

  @Autowired
  private ConstructorRepository constructorRepository;

  public Constructor updateConstructor(Constructor constructor) {
    Constructor existingConstructor = constructorRepository.findByConstructorId(
        constructor.getConstructorId());

    if (existingConstructor == null) {
      throw new IllegalArgumentException(
          "Constructor " + constructor.getFullName() + " not found with id "
              + constructor.getConstructorId());
    }

    // Update fields
    existingConstructor.setFullName(constructor.getFullName());
    existingConstructor.setFantasyPoints(constructor.getFantasyPoints());
    existingConstructor.setFantasyPrice(constructor.getFantasyPrice());

    return constructorRepository.save(existingConstructor);
  }
}
