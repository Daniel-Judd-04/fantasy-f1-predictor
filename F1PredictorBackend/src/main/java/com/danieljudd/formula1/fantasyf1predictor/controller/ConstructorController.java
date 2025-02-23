package com.danieljudd.formula1.fantasyf1predictor.controller;

import com.danieljudd.formula1.fantasyf1predictor.DTO.ConstructorDTO;
import com.danieljudd.formula1.fantasyf1predictor.model.Constructor;
import com.danieljudd.formula1.fantasyf1predictor.repository.ConstructorRepository;
import com.danieljudd.formula1.fantasyf1predictor.service.ConstructorService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/constructors")
public class ConstructorController {

  @Autowired
  private ConstructorRepository constructorRepository;

  @Autowired
  private ConstructorService constructorService;

  @GetMapping
  public List<Constructor> getAllConstructors() {
    return constructorRepository.findAll();
  }

  @GetMapping("/size")
  public long getConstructorCount() {
    return constructorRepository.count();
  }

  @PutMapping
  public ResponseEntity<Constructor> updateConstructor(@RequestBody Constructor constructor) {
    Constructor updatedConstructor = constructorService.updateConstructor(constructor);
    return ResponseEntity.ok(updatedConstructor);
  }

  @PostMapping
  public ResponseEntity<Constructor> addConstructor(@RequestBody ConstructorDTO constructor) {
    Constructor newConstructor = constructorService.addConstructor(constructor);
    return new ResponseEntity<>(newConstructor, HttpStatus.CREATED);
  }

  @DeleteMapping("/id={id}")
  public ResponseEntity<String> deleteConstructor(@PathVariable int id) {
    if (constructorService.deleteConstructor(id)) {
      return new ResponseEntity<>("Constructor deleted", HttpStatus.OK);
    }
    return new ResponseEntity<>("Error deleting constructor with id: " + id,
        HttpStatus.BAD_REQUEST);
  }
}
