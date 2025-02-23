package com.danieljudd.formula1.fantasyf1predictor.DTO;

import java.time.Instant;
import java.time.ZoneId;
import lombok.Getter;

@Getter
public class GrandPrixInputDTO {

  private final String fullName;
  private final Instant startTime;
  private final int season;
  private final CircuitInputDTO circuit;
  private final boolean sprint;

  public GrandPrixInputDTO(String fullName, Instant startTime, CircuitInputDTO circuit,
      boolean sprint) {
    this.fullName = fullName;
    this.startTime = startTime;
    this.season = startTime.atZone(ZoneId.systemDefault()).getYear();
    this.circuit = circuit;
    this.sprint = sprint;
  }
}
