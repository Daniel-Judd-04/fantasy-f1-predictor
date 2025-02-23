package com.danieljudd.formula1.fantasyf1predictor.model.team;

import com.danieljudd.formula1.fantasyf1predictor.model.Constructor;
import com.danieljudd.formula1.fantasyf1predictor.model.Driver;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
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
public class UserTeam extends Team {

  @NotNull
  private String owner; // The owner of the team

  @NotNull
  private String name; // The name of the team

  @NotNull
  private BigDecimal remainingBudget; // The remaining budget of the team
  @NotNull
  private byte freeTransfers; // The number of free transfers the team has left

  public BigDecimal calculateTotalBudget() {
    BigDecimal totalCost = remainingBudget;
    for (Driver driver : getDrivers()) {
      totalCost = totalCost.add(driver.getFantasyPrice());
    }
    for (Constructor constructor : getConstructors()) {
      totalCost = totalCost.add(constructor.getFantasyPrice());
    }
    return totalCost;
  }

}
