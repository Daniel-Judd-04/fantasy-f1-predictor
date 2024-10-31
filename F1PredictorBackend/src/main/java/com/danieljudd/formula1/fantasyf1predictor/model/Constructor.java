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

  @OneToMany(mappedBy = "constructor")
  @JsonIdentityReference(alwaysAsId = true)
  private List<Driver> drivers;

  private int fantasyPoints;
  private BigDecimal fantasyPrice;

  /**
   * Constructor for Constructor
   *
   * @param shortName     Unique 3 letter identifier
   * @param fullName      Full name of the constructor
   * @param country       Country of origin
   * @param drivers       List of drivers in the constructor
   * @param fantasyPoints Fantasy points
   * @param fantasyPrice  Fantasy price
   */
  public Constructor(String shortName, String fullName, String country, List<Driver> drivers,
      int fantasyPoints, BigDecimal fantasyPrice) {
    this.shortName = shortName;
    this.fullName = fullName;
    this.country = country;
    this.drivers = drivers;
    this.fantasyPoints = fantasyPoints;
    this.fantasyPrice = fantasyPrice;
  }
}
