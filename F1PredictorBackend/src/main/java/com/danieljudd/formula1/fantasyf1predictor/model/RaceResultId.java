package com.danieljudd.formula1.fantasyf1predictor.model;

import java.io.Serializable;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class RaceResultId implements Serializable {

  private Driver driver;
  private Constructor constructor;
  private GrandPrix grandPrix;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RaceResultId teamId = (RaceResultId) o;
    return Objects.equals(driver, teamId.driver) && Objects.equals(constructor, teamId.constructor)
        && Objects.equals(grandPrix, teamId.grandPrix);
  }

  @Override
  public int hashCode() {
    return Objects.hash(driver, constructor, grandPrix);
  }
}
