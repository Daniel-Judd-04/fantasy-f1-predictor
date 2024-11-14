package com.danieljudd.formula1.fantasyf1predictor.model;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@IdClass(RaceResultId.class)
public class RaceResult {

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

  @NotNull
  byte position;
  @NotNull
  byte pitStops;
  boolean fastestLap = false;
}
