package com.danieljudd.formula1.fantasyf1predictor.model;

import com.danieljudd.formula1.fantasyf1predictor.model.result.Result;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator.class,
    property = "grandPrixId")
public class GrandPrix implements Comparable<GrandPrix> {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int grandPrixId;

  @ManyToOne
  @JoinColumn(name = "circuit_id", nullable = false)
  @JsonIdentityReference(alwaysAsId = true)
  private Circuit circuit;

  @OneToMany(mappedBy = "grandPrix", orphanRemoval = true)
  @JsonIdentityReference(alwaysAsId = true)
  private List<Result> results = new ArrayList<>();

  @Column(unique = true)
  private Instant startTime;
  private int season;
  private byte round;

  private String fullName;

  private boolean sprint;

  private byte yellowFlags;
  private byte redFlags;

  private BigDecimal airTemperature;
  private BigDecimal trackTemperature;
  private BigDecimal humidity;
  private boolean rainfall;
  private BigDecimal windSpeed;

  public GrandPrix(Circuit circuit, Instant startTime, String fullName,
      boolean sprint,
      byte yellowFlags, byte redFlags, BigDecimal airTemperature, BigDecimal trackTemperature,
      BigDecimal humidity, boolean rainfall, BigDecimal windSpeed) {
    this.circuit = circuit;
    this.startTime = startTime;
    this.fullName = fullName;
    this.sprint = sprint;
    this.yellowFlags = yellowFlags;
    this.redFlags = redFlags;
    this.airTemperature = airTemperature;
    this.trackTemperature = trackTemperature;
    this.humidity = humidity;
    this.rainfall = rainfall;
    this.windSpeed = windSpeed;
  }

  public int getSeason() {
    return startTime.atZone(ZoneId.systemDefault()).getYear();
  }

  @Override
  public int compareTo(GrandPrix o) {
    return startTime.compareTo(o.startTime);
  }
}
