package com.danieljudd.formula1.fantasyf1predictor.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    property = "driverId")
public class Driver {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int driverId;

  @NotNull
  @Size(min = 3, max = 3)
  @Column(
      name = "short_name",
      nullable = false,
      unique = true,
      columnDefinition = "CHAR(3)"
  )
  private String shortName;

  private String fullName;
  private String country;
  @Column(unique = true)
  private byte carNumber;

  @ManyToOne
  @JoinColumn(name = "constructor_id", nullable = false)
  @JsonIdentityReference(alwaysAsId = true)
  private Constructor constructor;

  private BigDecimal points = new BigDecimal("0.0");
  private int fantasyPoints = 0;
  private BigDecimal fantasyPrice = new BigDecimal("0.0");

  private boolean active = true;

  /**
   * Constructor for Driver.
   *
   * @param shortName   the short name of the driver
   * @param fullName    the full name of the driver
   * @param carNumber   the car number of the driver
   * @param country     the country of the driver
   * @param constructor the constructor of the driver
   */
  public Driver(String shortName, String fullName, String country, byte carNumber,
      Constructor constructor) {
    this.shortName = shortName;
    this.fullName = fullName;
    this.carNumber = carNumber;
    this.country = country;
    this.constructor = constructor;
  }

  @Override
  public String toString() {
    return fullName + " (" + shortName + ")";
  }
}
