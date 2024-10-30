package com.danieljudd.formula1.fantasyf1predictor.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator.class,
    property = "id")
public class Circuit {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private int round;

  @Column(unique = true)
  private Instant date;
  private String timezone;

  private String fullName;
  private String locationName;
  private String country;

  private boolean isSprint;

  /**
   * Constructor for Circuit
   *
   * @param round        The nth race of the season
   * @param date         The date of the race (UTC)
   * @param timezone     The timezone offset of the race
   * @param fullName     The official name of the race
   * @param locationName The given name for the location of the race
   * @param country      The country for the race
   * @param isSprint     Whether the race is a sprint
   */
  public Circuit(int round, Instant date, String timezone, String fullName, String locationName,
      String country,
      boolean isSprint) {
    this.round = round;
    this.date = date;
    this.timezone = timezone;
    this.fullName = fullName;
    this.locationName = locationName;
    this.country = country;
    this.isSprint = isSprint;
  }
}
