package br.com.sbk.sbking.gui.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.core.GameModeSummary;
import br.com.sbk.sbking.core.Score;
import br.com.sbk.sbking.core.exceptions.DealNotFinishedException;
import br.com.sbk.sbking.core.rulesets.abstractClasses.PositiveRuleset;
import br.com.sbk.sbking.core.rulesets.abstractClasses.Ruleset;
import br.com.sbk.sbking.core.rulesets.concrete.NegativeHeartsRuleset;
import br.com.sbk.sbking.core.rulesets.concrete.NegativeKingRuleset;
import br.com.sbk.sbking.core.rulesets.concrete.NegativeLastTwoRuleset;
import br.com.sbk.sbking.core.rulesets.concrete.NegativeMenRuleset;
import br.com.sbk.sbking.core.rulesets.concrete.NegativeTricksRuleset;
import br.com.sbk.sbking.core.rulesets.concrete.NegativeWomenRuleset;

@SuppressWarnings("serial")
public class KingGameScoreboard implements Serializable {

    private GameModeSummary[] games = new GameModeSummary[10];
    private int gamesPlayed = 0;
    int positivesPlayed = 0;

    public void addFinishedDeal(Deal deal) {
        if (!deal.isFinished()) {
            throw new DealNotFinishedException();
        } else {
            Direction chosenBy = deal.getDealer().getPositiveOrNegativeChooserWhenDealer();
            Ruleset ruleset = deal.getRuleset();
            int orderOfPlay = ++gamesPlayed;
            Score score = deal.getScore();
            GameModeSummary current = new GameModeSummary(chosenBy, ruleset, orderOfPlay, score);
            games[getPositionOfRuleset(ruleset)] = current;
            if (ruleset instanceof PositiveRuleset) {
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

        String name;
        String score;
        String chosenBy;
        String orderOfPlay;

        if (currentGameModeSummary != null) {
            name = currentGameModeSummary.getName();
            score = String.valueOf(currentGameModeSummary.getScore());
            if (score.charAt(0) != '-') {
                score = "+" + score;
            }
            chosenBy = currentGameModeSummary.getChosenBy();
            orderOfPlay = currentGameModeSummary.getOrderOfPlay();
        } else {
            name = getNameOfGameNumber(line);
            score = "----";
            chosenBy = "---";
            orderOfPlay = "--";
        }
        response = String.format("%-20s %-4s %-3s %-2s", name, score, chosenBy, orderOfPlay);
        return response;

    }

    public String getSummary() {
        String response;

        String name = "Total score:";
        String score = "----";
        String chosenBy = "";
        String orderOfPlay = "";

        int totalScore = 0;
        for (GameModeSummary gameModeSummary : games) {
            if (gameModeSummary != null) {
                totalScore += gameModeSummary.getScore();
            }
        }
        score = String.valueOf(totalScore);
        response = String.format("%-20s %-4s %-3s %-2s", name, score, chosenBy, orderOfPlay);
        return response;
    }

    private static final Map<Integer, String> NEGATIVE_GAME_NAMES = new HashMap<Integer, String>() {
        {
            put(1, new NegativeTricksRuleset().getShortDescription());
            put(2, new NegativeHeartsRuleset().getShortDescription());
            put(3, new NegativeMenRuleset().getShortDescription());
            put(4, new NegativeWomenRuleset().getShortDescription());
            put(5, new NegativeLastTwoRuleset().getShortDescription());
            put(6, new NegativeKingRuleset().getShortDescription());
        }
    };

    private String getNameOfGameNumber(int number) {
        String name = NEGATIVE_GAME_NAMES.get(number);
        if (name == null) {
            return "Positive";
        } else {
            return name;
        }
    }

    private static final Map<Ruleset, Integer> NEGATIVE_POSITIONS = new HashMap<Ruleset, Integer>() {
        {
            put(new NegativeTricksRuleset(), 1);
            put(new NegativeHeartsRuleset(), 2);
            put(new NegativeMenRuleset(), 3);
            put(new NegativeWomenRuleset(), 4);
            put(new NegativeLastTwoRuleset(), 5);
            put(new NegativeKingRuleset(), 6);
        }
    };

    private int getPositionOfRuleset(Ruleset ruleset) {
        Integer position = NEGATIVE_POSITIONS.get(ruleset);
        if (position == null) {
            return 6 + this.positivesPlayed;
        } else {
            return position;
        }
    }
}
