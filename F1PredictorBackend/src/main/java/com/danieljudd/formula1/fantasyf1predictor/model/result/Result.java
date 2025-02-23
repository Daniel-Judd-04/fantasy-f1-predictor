package com.danieljudd.formula1.fantasyf1predictor.model.result;

import com.danieljudd.formula1.fantasyf1predictor.model.Constructor;
import com.danieljudd.formula1.fantasyf1predictor.model.Driver;
import com.danieljudd.formula1.fantasyf1predictor.model.GrandPrix;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@IdClass(ResultId.class)
public class Result implements Comparable<Result> {

  @Id
  @NotNull
  @ManyToOne
  @JoinColumn(name = "driver_id", nullable = false)
  @JsonIdentityReference(alwaysAsId = true)
  Driver driver;

  @Id
  @NotNull
  @ManyToOne
  @JoinColumn(name = "constructor_id", nullable = false)
  @JsonIdentityReference(alwaysAsId = true)
  Constructor constructor;

  @Id
  @NotNull
  @ManyToOne
  @JoinColumn(name = "grand_prix_id", nullable = false)
  @JsonIdentityReference(alwaysAsId = true)
  GrandPrix grandPrix;

  @Id
  @NotNull
  char sessionType;

  @NotNull
  byte position;
  @NotNull
  byte points;

  @Override
  public int compareTo(Result o) {
    return grandPrix.getStartTime().compareTo(o.grandPrix.getStartTime());
  }

  public static Set<List<Result>> groupBySeason(List<Result> results) {
    Set<List<Result>> raceResultsBySeason = new HashSet<>();

    for (Result result : results) {
      int season = result.getGrandPrix().getSeason();
      boolean seasonExists = false;
      for (List<Result> seasonResults : raceResultsBySeason) {
        if (seasonResults.get(0).getGrandPrix().getSeason() == season) {
          for (Result seasonResult : seasonResults) {
            if (seasonResult.getGrandPrix().getStartTime()
                .compareTo(result.getGrandPrix().getStartTime()) > 0) {
              seasonResults.add(seasonResults.indexOf(seasonResult), result);
            }
            seasonExists = true;
            break;
          }
        }
      }
      if (!seasonExists) {
        List<Result> newSeasonResults = new ArrayList<>();
        newSeasonResults.add(result);
        raceResultsBySeason.add(newSeasonResults);
      }
    }

    return raceResultsBySeason;
  }
}
