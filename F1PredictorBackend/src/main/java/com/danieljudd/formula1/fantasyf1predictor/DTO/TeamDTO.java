package com.danieljudd.formula1.fantasyf1predictor.DTO;

import java.math.BigDecimal;
import java.util.List;
import lombok.Getter;

@Getter
public class TeamDTO {

  private String teamOwner;
  private String teamName;

  private BigDecimal remainingBudget;
  private byte freeTransfers;
  private String activeChip;

  private List<Integer> driverIds;
  private List<Integer> constructorIds;

}
