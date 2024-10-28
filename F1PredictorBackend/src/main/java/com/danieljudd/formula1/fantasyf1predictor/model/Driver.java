package com.danieljudd.formula1.fantasyf1predictor.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Driver {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @Size(min = 3, max = 3)
  @Column(unique = true)
  private String shortName;

  private String name;
  private String nationality;
  @Column(unique = true)
  private int number;

  @ManyToOne
  @JoinColumn(name = "team_id", nullable = false)
  @JsonBackReference
  private Team team;

  /**
   * Constructor for Driver
   *
   * @param shortName   Unique 3 letter identifier
   * @param name        Full name of the driver
   * @param nationality Country of origin
   * @param number      Car number
   * @param team        Team of the driver
   */
  public Driver(String shortName, String name, String nationality, int number, Team team) {
    this.shortName = shortName;
    this.name = name;
    this.nationality = nationality;
    this.number = number;
    this.team = team;
  }
}
