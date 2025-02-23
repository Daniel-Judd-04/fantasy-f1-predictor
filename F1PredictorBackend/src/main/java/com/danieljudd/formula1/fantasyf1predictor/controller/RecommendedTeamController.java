package com.danieljudd.formula1.fantasyf1predictor.controller;

import com.danieljudd.formula1.fantasyf1predictor.DTO.RecommendedTeamOutputDTO;
import com.danieljudd.formula1.fantasyf1predictor.model.GrandPrix;
import com.danieljudd.formula1.fantasyf1predictor.model.team.Chip;
import com.danieljudd.formula1.fantasyf1predictor.model.team.RecommendedTeam;
import com.danieljudd.formula1.fantasyf1predictor.model.team.UserTeam;
import com.danieljudd.formula1.fantasyf1predictor.repository.GrandPrixRepository;
import com.danieljudd.formula1.fantasyf1predictor.repository.UserTeamRepository;
import com.danieljudd.formula1.fantasyf1predictor.service.GrandPrixService;
import com.danieljudd.formula1.fantasyf1predictor.service.RecommendedTeamService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recommended-teams")
public class RecommendedTeamController {

  @Autowired
  private UserTeamRepository userTeamRepository;

  @Autowired
  private GrandPrixRepository grandPrixRepository;

  @Autowired
  private RecommendedTeamService recommendedTeamService;

  @Autowired
  private GrandPrixService grandPrixService;

  @GetMapping(value = "/{limit}/{activeChips}/{userTeamId}/{grandPrixId}")
  public ResponseEntity<List<RecommendedTeamOutputDTO>> getRecommendedTeams(
      @PathVariable int limit,
      @PathVariable String activeChips,
      @PathVariable int userTeamId,
      @PathVariable int grandPrixId
  ) {

    List<Chip> selectedChips = new ArrayList<>();
    for (int chipIndex = 0; chipIndex < activeChips.length() && chipIndex < Chip.values().length;
        chipIndex++) {
      if (activeChips.charAt(chipIndex) == '1') {
        System.out.println("Adding chip: " + Chip.values()[chipIndex]);
        selectedChips.add(Chip.values()[chipIndex]);
      }
    }
    UserTeam userTeam = userTeamRepository.findById(userTeamId).orElseThrow();
    GrandPrix grandPrix = grandPrixRepository.findByGrandPrixId(grandPrixId);
    limit = limit == -1 ? Integer.MAX_VALUE : limit;

    List<RecommendedTeam> allTeams = recommendedTeamService.getRecommendedTeams(
        selectedChips, userTeam, grandPrix
    );

    System.out.println("Converting teams to output DTOs...");
    int numberOfRaces = grandPrixService.getNumberOfRaces(grandPrix);
    List<RecommendedTeamOutputDTO> convertedTeams = recommendedTeamService.convertToOutputDTOs(
        allTeams, numberOfRaces);

    System.out.println("Sorting all valid teams...");
    // TODO: Sort by by magic number
    convertedTeams.sort(
        (t1, t2) -> Float.compare(t2.getAverageRecentPoints(), t1.getAverageRecentPoints())
    );
    System.out.println("Reducing teams to " + Math.min(limit, allTeams.size()) + " teams...");
    List<RecommendedTeamOutputDTO> reducedTeams = convertedTeams.subList(0,
        Math.min(limit, allTeams.size()));

    System.out.println("Returning " + reducedTeams.size() + " team DTOs.");
    return ResponseEntity.ok(reducedTeams);
  }
}
