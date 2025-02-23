package com.danieljudd.formula1.fantasyf1predictor.response;

import com.danieljudd.formula1.fantasyf1predictor.DTO.RecommendedTeamOutputDTO;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProgressResponse {

  private int stage;
  private int maxStage;
  private String message;
  private List<RecommendedTeamOutputDTO> teams;

  public ProgressResponse(int stage, int maxStage, String message,
      List<RecommendedTeamOutputDTO> teams) {
    this.stage = stage;
    this.maxStage = maxStage;
    this.message = message;
    this.teams = teams;
  }
}
