package com.danieljudd.formula1.fantasyf1predictor.model.team;

import com.danieljudd.formula1.fantasyf1predictor.model.Constructor;
import com.danieljudd.formula1.fantasyf1predictor.model.Driver;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.JOINED)
public class Team {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int teamId;

  @ManyToMany(fetch = FetchType.EAGER)
  @JsonIdentityReference(alwaysAsId = true)
  @JoinTable(
      name = "team_drivers",
      joinColumns = {
          @JoinColumn(name = "team_id", referencedColumnName = "teamId"),
      },
      inverseJoinColumns = @JoinColumn(name = "driver_id", referencedColumnName = "driverId")
  )
  private Set<Driver> drivers = new HashSet<>();

  @ManyToMany(fetch = FetchType.EAGER)
  @JsonIdentityReference(alwaysAsId = true)
  @JoinTable(
      name = "team_constructors",
      joinColumns = {
          @JoinColumn(name = "team_id", referencedColumnName = "teamId"),
      },
      inverseJoinColumns = @JoinColumn(name = "constructor_id", referencedColumnName = "constructorId")
  )
  private Set<Constructor> constructors = new HashSet<>();
}
