package com.danieljudd.formula1.fantasyf1predictor.model.team;

public enum Chip {
  NONE,
  WILDCARD,
  NO_NEGATIVE,
  FINAL_FIX,
  EXTRA_DRS_BOOST,
  AUTOPILOT,
  LIMITLESS;

  @Override
  public String toString() {
    return name().replace("_", " ");
  }
}
