package com.danieljudd.formula1.fantasyf1predictor.DTO;

import java.math.BigDecimal;
import lombok.Getter;

@Getter
public class DriverDTO {

  private String shortName;
  private String fullName;
  private String country;
  private byte carNumber;
  private int constructorId;
  private BigDecimal fantasyPrice;
  private boolean active;

}
