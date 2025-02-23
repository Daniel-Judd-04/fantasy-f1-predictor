package com.danieljudd.formula1.fantasyf1predictor.DTO;

import java.math.BigDecimal;
import lombok.Getter;

@Getter
public class DriverDTO {

  private int driverId;
  private String shortName;
  private String fullName;
  private String country;
  private byte carNumber;
  private int constructor;
  private BigDecimal points;
  private int fantasyPoints;
  private BigDecimal fantasyPrice;
  private boolean active;

}
