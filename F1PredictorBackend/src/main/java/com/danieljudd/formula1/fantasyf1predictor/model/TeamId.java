package com.danieljudd.formula1.fantasyf1predictor.model;

import java.io.Serializable;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamId implements Serializable {

  private String teamOwner;
  private String teamName;
  
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TeamId teamId = (TeamId) o;
    return Objects.equals(teamOwner, teamId.teamOwner) && Objects.equals(teamName, teamId.teamName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(teamOwner, teamName);
  }
}
