package br.com.sbk.sbking.core.game;

import static br.com.sbk.sbking.core.GameConstants.TOTAL_GAMES;

import java.util.HashSet;
import java.util.Set;

import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.core.boarddealer.BoardDealer;
import br.com.sbk.sbking.core.boarddealer.Complete52CardDeck;
import br.com.sbk.sbking.core.boarddealer.FourteenHCPPlusDoubletonRuledBoardDealer;
import br.com.sbk.sbking.core.rulesets.abstractrulesets.NegativeRuleset;
import br.com.sbk.sbking.core.rulesets.abstractrulesets.PositiveRuleset;
import br.com.sbk.sbking.core.rulesets.abstractrulesets.Ruleset;
import br.com.sbk.sbking.core.rulesets.concrete.NoRuleset;
import br.com.sbk.sbking.gui.models.KingGameScoreboard;

public class PositiveKingGame extends TrickGame {

    private KingGameScoreboard gameScoreboard;
    private Set<NegativeRuleset> chosenNegativeRulesets = new HashSet<NegativeRuleset>();
    private int northSouthNegatives = 0;
    private int eastWestNegatives = 0;
    private int northSouthPositives = 0;
    private int eastWestPositives = 0;
    private int playedHands = 0;
    private BoardDealer boardDealer;

    public PositiveKingGame() {
        super();
        this.gameScoreboard = new KingGameScoreboard();
        this.gameDeck = new Complete52CardDeck().getDeck();
        this.boardDealer = new FourteenHCPPlusDoubletonRuledBoardDealer();
        this.dealNewBoard();
    }

    @Override
    public void dealNewBoard() {
        this.currentBoard = boardDealer.dealBoard(this.dealer, this.gameDeck);
        this.currentDeal = new Deal(currentBoard, new NoRuleset(), this.getLeader(), true);
    }

    public void addRuleset(Ruleset currentGameModeOrStrain) {
        this.currentDeal = new Deal(this.currentBoard, currentGameModeOrStrain, this.getLeader(), true);
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
                this.northSouthPositives++;
            } else {
                this.eastWestPositives++;
            }
        } else {
            NegativeRuleset negativeRuleset = (NegativeRuleset) currentRuleset;
            this.chosenNegativeRulesets.add(negativeRuleset);
            if (isNorthSouth) {
                this.northSouthNegatives++;
            } else {
                this.eastWestNegatives++;
            }
        }
        this.dealer = this.dealer.next();
        this.playedHands++;
    }

    public KingGameScoreboard getGameScoreboard() {
        return gameScoreboard;
    }

    public boolean isGameModePermitted(Ruleset ruleset, Direction chooser) {
        return (ruleset instanceof PositiveRuleset);
    }

    @Override
    public Direction getLeader() {
        return this.dealer.next(2);
    }

}
