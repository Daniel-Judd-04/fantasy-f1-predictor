package com.danieljudd.formula1.fantasyf1predictor.DTO;

import com.danieljudd.formula1.fantasyf1predictor.model.Constructor;
import com.danieljudd.formula1.fantasyf1predictor.model.Driver;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import java.math.BigDecimal;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserTeamOutputDTO {

  private int teamId;

  private String owner;
  private String name;
  private BigDecimal remainingBudget;
  private byte freeTransfers;

  @JsonIdentityReference(alwaysAsId = true)
  private Set<Driver> drivers;
  @JsonIdentityReference(alwaysAsId = true)
  private Set<Constructor> constructors;

}
