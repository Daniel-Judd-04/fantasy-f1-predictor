package com.danieljudd.formula1.fantasyf1predictor.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
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
    property = "constructorId")
public class Constructor {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int constructorId;

  @NotNull
  @Size(min = 3, max = 3)
  @Column(
      name = "short_name",
      nullable = false,
      unique = true,
      columnDefinition = "CHAR(3)"
  )
  private String shortName;

  private String fullName;
  private String country;

  @JsonIdentityReference(alwaysAsId = true)
  @OneToMany(mappedBy = "constructor", orphanRemoval = true)
  private List<Driver> drivers = new ArrayList<>();

  @OneToMany(mappedBy = "constructor", orphanRemoval = true)
  @JsonIdentityReference(alwaysAsId = true)
  private List<RaceResult> raceResults = new ArrayList<>();

  private int fantasyPoints = 0;
  private BigDecimal fantasyPrice = new BigDecimal("0.0");

  private boolean active = true;

  /**
   * Constructor for Constructor
   *
   * @param shortName Unique 3 letter identifier
   * @param fullName  Full name of the constructor
   * @param country   Country of origin
   */
  public Constructor(String shortName, String fullName, String country) {
    this.shortName = shortName;
    this.fullName = fullName;
    this.country = country;
  }

  @Override
  public String toString() {
    return fullName + " (" + shortName + ")";
  }
}
