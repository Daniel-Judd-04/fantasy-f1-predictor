package com.danieljudd.formula1.fantasyf1predictor.service;

import com.danieljudd.formula1.fantasyf1predictor.DTO.ConstructorDTO;
import com.danieljudd.formula1.fantasyf1predictor.model.Constructor;
import com.danieljudd.formula1.fantasyf1predictor.repository.ConstructorRepository;
import java.util.ArrayList;
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
    existingConstructor.setCountry(constructor.getCountry());
    existingConstructor.setFantasyPoints(constructor.getFantasyPoints());
    existingConstructor.setFantasyPrice(constructor.getFantasyPrice());
    existingConstructor.setActive(constructor.isActive());
    System.out.println("Updating constructor " + existingConstructor.getFullName());
    System.out.println("Active status: " + existingConstructor.isActive());

    return constructorRepository.save(existingConstructor);
  }

  public Constructor addConstructor(ConstructorDTO constructorDTO) {
    if (constructorRepository.findByShortName(constructorDTO.getShortName()) != null) {
      throw new IllegalArgumentException(
          "Constructor with short name " + constructorDTO.getShortName() + " already exists");
    }

    Constructor constructor = new Constructor();
    constructor.setShortName(constructorDTO.getShortName());
    constructor.setFullName(constructorDTO.getFullName());
    constructor.setCountry(constructorDTO.getCountry());
    constructor.setFantasyPrice(constructorDTO.getFantasyPrice());
    constructor.setActive(constructorDTO.isActive());
    constructor.setDrivers(new ArrayList<>());

    System.out.println("Adding constructor: " + constructor);
    return constructorRepository.save(constructor);
  }
}
