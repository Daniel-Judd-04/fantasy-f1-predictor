package com.danieljudd.formula1.fantasyf1predictor.DTO;

import com.danieljudd.formula1.fantasyf1predictor.model.Constructor;
import com.danieljudd.formula1.fantasyf1predictor.model.Driver;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import java.math.BigDecimal;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TeamOutputDTO {

  private String teamOwner;
  private String teamName;

  private BigDecimal value;
  private byte freeTransfers;
  private String activeChip;

  @JsonIdentityReference(alwaysAsId = true)
  private Set<Driver> drivers;
  @JsonIdentityReference(alwaysAsId = true)
  private Set<Constructor> constructors;

  private float averageFantasyPoints;
  private float averageRecentPoints;
  private float averagePoints;
  private float consistency;
}
