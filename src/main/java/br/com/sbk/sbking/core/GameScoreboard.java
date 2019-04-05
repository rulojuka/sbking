package br.com.sbk.sbking.core;

import br.com.sbk.sbking.core.exceptions.DealNotFinishedException;
import br.com.sbk.sbking.core.rulesets.abstractClasses.PositiveRuleset;
import br.com.sbk.sbking.core.rulesets.abstractClasses.Ruleset;
import br.com.sbk.sbking.core.rulesets.concrete.NegativeHeartsRuleset;
import br.com.sbk.sbking.core.rulesets.concrete.NegativeKingRuleset;
import br.com.sbk.sbking.core.rulesets.concrete.NegativeLastTwoRuleset;
import br.com.sbk.sbking.core.rulesets.concrete.NegativeMenRuleset;
import br.com.sbk.sbking.core.rulesets.concrete.NegativeTricksRuleset;
import br.com.sbk.sbking.core.rulesets.concrete.NegativeWomenRuleset;

public class GameScoreboard {

	private GameModeSummary[] games = new GameModeSummary[10];
	private int gamesPlayed = 0;
	int positivesPlayed = 0;

	public void addGame(Deal deal) {
		if (!deal.isFinished()) {
			throw new DealNotFinishedException();
		} else {
			Direction chosenBy = deal.getLeader().getChosenByWhenLeader();
			Ruleset ruleset = deal.getRuleset();
			int orderOfPlay = ++gamesPlayed;
			Score score = deal.getScore();
			GameModeSummary current = new GameModeSummary(chosenBy, ruleset, orderOfPlay, score);
			games[getPositionOfRuleset(ruleset)] = current;
			if(ruleset instanceof PositiveRuleset) {
				positivesPlayed++;
			}
		}
	}

	public String getLine(int line) {

		if (line < 1 || line > 10) {
			throw new IllegalArgumentException("Invalid line number. Valid numbers are 1 to 10 (inclusive)");
		}
		GameModeSummary currentGameModeSummary = games[line - 1];
		String response;

		String name = "";
		String score = "";
		String chosenBy = "";
		String orderOfPlay = "";

		if (currentGameModeSummary != null) {
			name = currentGameModeSummary.getName();
			score = currentGameModeSummary.getScore();
			chosenBy = currentGameModeSummary.getChosenBy();
			orderOfPlay = currentGameModeSummary.getOrderOfPlay();
		} else {
			name = getNameOfGameNumber(line);
		}
		response = String.format("%20s %4s %4s %2s", name, score, chosenBy, orderOfPlay);
		return response;

	}

	private String getNameOfGameNumber(int number) {
		switch (number) {
		case 1:
			return new NegativeTricksRuleset().getShortDescription();
		case 2:
			return new NegativeHeartsRuleset().getShortDescription();
		case 3:
			return new NegativeMenRuleset().getShortDescription();
		case 4:
			return new NegativeWomenRuleset().getShortDescription();
		case 5:
			return new NegativeLastTwoRuleset().getShortDescription();
		default:
			return "Positive";
		}
	}

	private int getPositionOfRuleset(Ruleset ruleset) {
		if (new NegativeTricksRuleset().equals(ruleset))
			return 0;
		if (new NegativeHeartsRuleset().equals(ruleset))
			return 1;
		if (new NegativeMenRuleset().equals(ruleset))
			return 2;
		if (new NegativeWomenRuleset().equals(ruleset))
			return 3;
		if (new NegativeLastTwoRuleset().equals(ruleset))
			return 4;
		if (new NegativeKingRuleset().equals(ruleset))
			return 5;
		return 6 + this.positivesPlayed;
	}

}
