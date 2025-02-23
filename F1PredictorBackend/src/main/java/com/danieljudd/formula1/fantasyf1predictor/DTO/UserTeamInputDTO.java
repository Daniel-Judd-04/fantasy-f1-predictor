package com.danieljudd.formula1.fantasyf1predictor.DTO;

import java.math.BigDecimal;
import java.util.List;
import lombok.Getter;

@Getter
public class UserTeamInputDTO {

  private int teamId;

  private String owner;
  private String name;

  private BigDecimal remainingBudget;
  private byte freeTransfers;

  private List<Integer> driverIds;
  private List<Integer> constructorIds;

}
