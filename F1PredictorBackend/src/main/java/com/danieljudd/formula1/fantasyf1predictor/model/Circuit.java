package com.danieljudd.formula1.fantasyf1predictor.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.math.BigDecimal;
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
    property = "circuitId")
public class Circuit {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int circuitId;


  @Column(unique = true)
  private String fullName;
  private String locationName;
  private String country;

  private String timezone;

  private BigDecimal length;
  private byte corners;
  private byte laps;
  private byte drsZones;

  /**
   * Constructor for Circuit
   *
   * @param fullName     Full name of the circuit
   * @param locationName Location name of the circuit
   * @param country      Country of the circuit
   * @param timezone     Timezone of the circuit
   * @param length       Length of a lap of the circuit
   * @param corners      Number of corners
   * @param laps         Number of laps
   * @param drsZones     Number of DRS zones
   */
  public Circuit(String fullName, String locationName, String country, String timezone,
      BigDecimal length, byte corners, byte laps, byte drsZones) {
    this.fullName = fullName;
    this.locationName = locationName;
    this.country = country;
    this.timezone = timezone;
    this.length = length;
    this.corners = corners;
    this.laps = laps;
    this.drsZones = drsZones;
  }
}
