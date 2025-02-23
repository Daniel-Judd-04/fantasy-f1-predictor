package com.danieljudd.formula1.fantasyf1predictor.model.result;

import com.danieljudd.formula1.fantasyf1predictor.model.Constructor;
import com.danieljudd.formula1.fantasyf1predictor.model.Driver;
import com.danieljudd.formula1.fantasyf1predictor.model.GrandPrix;
import java.io.Serializable;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ResultId implements Serializable {

  private Driver driver;
  private Constructor constructor;
  private GrandPrix grandPrix;
  private char sessionType;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ResultId teamId = (ResultId) o;
    return Objects.equals(driver, teamId.driver) && Objects.equals(constructor, teamId.constructor)
        && Objects.equals(grandPrix, teamId.grandPrix);
  }

  @Override
  public int hashCode() {
    return Objects.hash(driver, constructor, grandPrix);
  }
}
