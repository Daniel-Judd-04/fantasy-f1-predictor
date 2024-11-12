package com.danieljudd.formula1.fantasyf1predictor.model;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@IdClass(TeamId.class)
public class Team {

  @Id
  @NotNull
  @Column(name = "team_owner")
  private String teamOwner;
  @Id
  @NotNull
  @Column(name = "team_name")
  private String teamName;

  @NotNull
  private BigDecimal costCap;
  @NotNull
  private byte freeTransfers;
  @NotNull
  private String activeChip;

  @ManyToMany
  @JsonIdentityReference(alwaysAsId = true)
  @JoinTable(
      name = "team_drivers",
      joinColumns = {
          @JoinColumn(name = "team_owner", referencedColumnName = "team_owner"),
          @JoinColumn(name = "team_name", referencedColumnName = "team_name")
      },
      inverseJoinColumns = @JoinColumn(name = "driver_id", referencedColumnName = "driverId")
  )
  private Set<Driver> drivers = new HashSet<>();

  @ManyToMany
  @JsonIdentityReference(alwaysAsId = true)
  @JoinTable(
      name = "team_constructors",
      joinColumns = {
          @JoinColumn(name = "team_owner", referencedColumnName = "team_owner"),
          @JoinColumn(name = "team_name", referencedColumnName = "team_name")
      },
      inverseJoinColumns = @JoinColumn(name = "constructor_id", referencedColumnName = "constructorId")
  )
  private Set<Constructor> constructors = new HashSet<>();
}
