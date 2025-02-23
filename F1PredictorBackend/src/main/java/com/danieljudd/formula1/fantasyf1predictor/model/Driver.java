package com.danieljudd.formula1.fantasyf1predictor.model;

import com.danieljudd.formula1.fantasyf1predictor.model.result.Result;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
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

  @OneToMany(mappedBy = "driver", orphanRemoval = true)
  @JsonIdentityReference(alwaysAsId = true)
  private List<Result> results = new ArrayList<>();

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

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    Driver driver = (Driver) obj;
    return driverId == driver.driverId;
  }

  @Override
  public int hashCode() {
    return Objects.hash(driverId);
  }

  // DB should ignore these
  @Transient
  private float avgRecPoints;
  @Transient
  private float avgPoints;
  @Transient
  private float avgVariance;

  public void setAvgRecPoints(GrandPrix gp) {
    avgRecPoints = calcAvgRecPoints(results, gp);
  }

  public void setAvgPoints(GrandPrix gp) {
    avgPoints = calcAvgPoints(results, gp);
  }

  public void setAvgVariance(GrandPrix gp) {
    avgVariance = calcAvgVariance(results, gp, avgPoints);
  }

  static float calcAvgRecPoints(List<Result> results, GrandPrix gp) {
    int sumOfWeightedInts = 0;
    int sumOfWeights = 0;

    results.sort(Comparator.comparing(r -> r.getGrandPrix().getStartTime()));

    for (int i = 0; i < results.size(); i++) {
      if (results.get(i).getGrandPrix().getStartTime().compareTo(gp.getStartTime()) > 0) {
        break;
      }
      sumOfWeightedInts += results.get(i).getPoints() * (i + 1);
      sumOfWeights += (i + 1);
    }
    return sumOfWeights > 0 ? (float) sumOfWeightedInts / sumOfWeights : 0;
  }

  static float calcAvgPoints(List<Result> results, GrandPrix gp) {
    int sumOfPoints = 0;
    int numOfRaces = 0;

    results.sort(Comparator.comparing(r -> r.getGrandPrix().getStartTime()));

    for (Result result : results) {
      if (result.getGrandPrix().getStartTime().compareTo(gp.getStartTime()) > 0) {
        break;
      }
      sumOfPoints += result.getPoints();
      numOfRaces++;
    }
    return numOfRaces > 0 ? (float) sumOfPoints / numOfRaces : 0;
  }

  static float calcAvgVariance(List<Result> results, GrandPrix gp, float avgPoints) {
    float totalVariance = 0;

    results.sort(Comparator.comparing(r -> r.getGrandPrix().getStartTime()));

    for (int i = 0; i < results.size(); i++) {
      if (results.get(0).getGrandPrix().getStartTime().compareTo(gp.getStartTime()) > 0) {
        return i > 0 ? totalVariance / i : 0;
      }
      totalVariance += Math.abs(results.get(i).getPoints() - avgPoints);
    }
    return !results.isEmpty() ? totalVariance / results.size() : 0;
  }
}
