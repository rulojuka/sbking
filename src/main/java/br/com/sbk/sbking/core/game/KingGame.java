package br.com.sbk.sbking.core.game;

import static br.com.sbk.sbking.core.GameConstants.MAXIMUM_NEGATIVES_PERMITTED_BY_DIRECTION;
import static br.com.sbk.sbking.core.GameConstants.MAXIMUM_POSITIVES_PERMITTED_BY_DIRECTION;
import static br.com.sbk.sbking.core.GameConstants.TOTAL_GAMES;

import java.util.HashSet;
import java.util.Set;

import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.core.boarddealer.BoardDealer;
import br.com.sbk.sbking.core.boarddealer.ShuffledBoardDealer;
import br.com.sbk.sbking.core.rulesets.abstractrulesets.NegativeRuleset;
import br.com.sbk.sbking.core.rulesets.abstractrulesets.PositiveRuleset;
import br.com.sbk.sbking.core.rulesets.abstractrulesets.Ruleset;
import br.com.sbk.sbking.core.rulesets.concrete.NoRuleset;
import br.com.sbk.sbking.gui.models.KingGameScoreboard;

public class KingGame extends TrickGame {

    private KingGameScoreboard gameScoreboard;
    private Set<NegativeRuleset> chosenNegativeRulesets = new HashSet<NegativeRuleset>();
    BoardDealer boardDealer;
    private int northSouthNegatives = 0;
    private int eastWestNegatives = 0;
    private int northSouthPositives = 0;
    private int eastWestPositives = 0;
    private int playedHands = 0;

    public KingGame() {
        this.gameScoreboard = new KingGameScoreboard();
        this.boardDealer = new ShuffledBoardDealer();
        this.dealNewBoard();
    }

    @Override
    public void dealNewBoard() {
        this.currentBoard = this.boardDealer.dealBoard(this.dealer);
        this.currentDeal = new Deal(currentBoard, new NoRuleset(), this.getLeader());
    }

    public void addRuleset(Ruleset currentGameModeOrStrain) {
        this.currentDeal = new Deal(this.currentBoard, currentGameModeOrStrain, this.getLeader());
    }

    @Override
    public boolean isFinished() {
        return playedHands == TOTAL_GAMES;
    }

    @Override
    public void finishDeal() {
        Direction currentChooser = this.getCurrentDeal().getDealer().getPositiveOrNegativeChooserWhenDealer();
        boolean isNorthSouth = currentChooser.isNorthSouth();
        Ruleset currentRuleset = this.getCurrentDeal().getRuleset();
        boolean isPositive = currentRuleset instanceof PositiveRuleset;

        this.getGameScoreboard().addFinishedDeal(this.getCurrentDeal());
        if (isPositive) {
            if (isNorthSouth) {
                northSouthPositives++;
            } else {
                eastWestPositives++;
            }
        } else {
            NegativeRuleset negativeRuleset = (NegativeRuleset) currentRuleset;
            this.chosenNegativeRulesets.add(negativeRuleset);
            if (isNorthSouth) {
                northSouthNegatives++;
            } else {
                eastWestNegatives++;
            }
        }
        this.dealer = this.dealer.next();
        this.playedHands++;
    }

    public KingGameScoreboard getGameScoreboard() {
        return gameScoreboard;
    }

    public boolean isGameModePermitted(Ruleset ruleset, Direction chooser) {
        boolean positiveRuleset = (ruleset instanceof PositiveRuleset);
        boolean chooserNorthSouth = chooser.isNorthSouth();

        if (!positiveRuleset && chosenNegativeRulesets.contains(ruleset)) {
            return false;
        }

        if (chooserNorthSouth) {
            if (positiveRuleset) {
                return northSouthPositives < MAXIMUM_POSITIVES_PERMITTED_BY_DIRECTION;
            } else {
                return northSouthNegatives < MAXIMUM_NEGATIVES_PERMITTED_BY_DIRECTION;
            }
        } else {
            if (positiveRuleset) {
                return eastWestPositives < MAXIMUM_POSITIVES_PERMITTED_BY_DIRECTION;
            } else {
                return eastWestNegatives < MAXIMUM_NEGATIVES_PERMITTED_BY_DIRECTION;
            }
        }
    }

    @Override
    public Direction getLeader() {
        return this.dealer.next(2);
    }

}
