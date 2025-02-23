package com.danieljudd.formula1.fantasyf1predictor.model.team;

import com.danieljudd.formula1.fantasyf1predictor.model.Constructor;
import com.danieljudd.formula1.fantasyf1predictor.model.Driver;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class RecommendedTeam extends Team {

  @NotNull
  private Chip activeChip; // The active chip of the RecommendTeam

  @Transient
  private BigDecimal totalCost;

  public void calculateTotalCost() {
    BigDecimal driversTotal = getDrivers().stream()
        .map(Driver::getFantasyPrice)
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal constructorsTotal = getConstructors().stream()
        .map(Constructor::getFantasyPrice)
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    totalCost = driversTotal.add(constructorsTotal);
  }

  public byte calculateRequiredTransfers(UserTeam comparableTeam) {
    Set<Driver> teamDrivers = new HashSet<>(getDrivers());
    Set<Constructor> teamConstructors = new HashSet<>(getConstructors());

    Set<Driver> comparableDrivers = new HashSet<>(comparableTeam.getDrivers());
    Set<Constructor> comparableConstructors = new HashSet<>(comparableTeam.getConstructors());

    teamDrivers.removeAll(comparableDrivers);
    teamConstructors.removeAll(comparableConstructors);

    return (byte) (teamDrivers.size() + teamConstructors.size());
  }

  public float calculateAverageFantasyPoints(int numberOfRaces) {
    float totalFantasyPoints = 0;

    for (Driver driver : getDrivers()) {
      totalFantasyPoints += driver.getFantasyPoints();
    }
    for (Constructor constructor : getConstructors()) {
      totalFantasyPoints += constructor.getFantasyPoints();
    }
    return numberOfRaces > 0 ? totalFantasyPoints / numberOfRaces : 0;
  }

  public float calculateAverageRecentPoints() {
    float totalRecentPoints = 0;

    for (Driver driver : getDrivers()) {
      totalRecentPoints += driver.getAvgRecPoints();
    }
    for (Constructor constructor : getConstructors()) {
      totalRecentPoints += constructor.getAvgRecPoints();
    }

    return totalRecentPoints;
  }

  public float calculateAveragePoints() {
    float avgPoints = 0;

    for (Driver driver : getDrivers()) {
      avgPoints += driver.getAvgPoints();
    }
    for (Constructor constructor : getConstructors()) {
      avgPoints += constructor.getAvgPoints();
    }
    return avgPoints;
  }

  public float calculateConsistency() {
    float avgVarience = 0;

    for (Driver driver : getDrivers()) {
      avgVarience += driver.getAvgVariance();
    }
    for (Constructor constructor : getConstructors()) {
      avgVarience += constructor.getAvgVariance();
    }
    return avgVarience;
  }

}
