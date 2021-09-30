package br.com.sbk.sbking.core.game;

import java.util.Map;

import br.com.sbk.sbking.core.Board;
import br.com.sbk.sbking.core.HandEvaluations;
import br.com.sbk.sbking.core.Suit;
import br.com.sbk.sbking.core.boardrules.bridgeopenings.DealerHasFourWeakOpeningBoardRule;
import br.com.sbk.sbking.core.boardrules.bridgeopenings.DealerHasOneMajorOpeningBoardRule;
import br.com.sbk.sbking.core.boardrules.bridgeopenings.DealerHasOneMinorOpeningBoardRule;
import br.com.sbk.sbking.core.boardrules.bridgeopenings.DealerHasOneNoTrumpOpeningBoardRule;
import br.com.sbk.sbking.core.boardrules.bridgeopenings.DealerHasThreeWeakOpeningBoardRule;
import br.com.sbk.sbking.core.boardrules.bridgeopenings.DealerHasTwoClubsOpeningBoardRule;
import br.com.sbk.sbking.core.boardrules.bridgeopenings.DealerHasTwoNoTrumpOpeningBoardRule;
import br.com.sbk.sbking.core.boardrules.bridgeopenings.DealerHasTwoWeakOpeningBoardRule;

public class OpeningTrainerGame {

    private final String twoClubs = "2c";
    private final String twoNoTrump = "2nt";
    private final String oneNoTrump = "1nt";
    private final String one = "1";
    private final String pass = "PASS";

    private Board board;
    private HandEvaluations handEvaluations;

    private DealerHasTwoClubsOpeningBoardRule dealerHas2C;
    private DealerHasTwoNoTrumpOpeningBoardRule dealerHas2NT;
    private DealerHasOneNoTrumpOpeningBoardRule dealerHas1NT;
    private DealerHasOneMajorOpeningBoardRule dealerHas1M;
    private DealerHasOneMinorOpeningBoardRule dealerHas1m;
    private DealerHasFourWeakOpeningBoardRule dealerHas4W;
    private DealerHasThreeWeakOpeningBoardRule dealerHas3W;
    private DealerHasTwoWeakOpeningBoardRule dealerHas2W;

    public OpeningTrainerGame(Board board) {
        this.board = board;
        this.handEvaluations = board.getHandOf(board.getDealer()).getHandEvaluations();
        this.createBoardRules();
    }

    private void createBoardRules() {
        this.dealerHas2C = new DealerHasTwoClubsOpeningBoardRule();
        this.dealerHas2NT = new DealerHasTwoNoTrumpOpeningBoardRule();
        this.dealerHas1NT = new DealerHasOneNoTrumpOpeningBoardRule();
        this.dealerHas1M = new DealerHasOneMajorOpeningBoardRule();
        this.dealerHas1m = new DealerHasOneMinorOpeningBoardRule();
        this.dealerHas4W = new DealerHasFourWeakOpeningBoardRule();
        this.dealerHas3W = new DealerHasThreeWeakOpeningBoardRule();
        this.dealerHas2W = new DealerHasTwoWeakOpeningBoardRule();
    }

    public String getOpening() {
        if (this.dealerHas2C.isValid(this.board)) {
            return twoClubs;
        }
        if (this.dealerHas2NT.isValid(this.board)) {
            return twoNoTrump;
        }
        if (this.dealerHas1NT.isValid(this.board)) {
            return oneNoTrump;
        }
        if (this.dealerHas1M.isValid(this.board)) {
            return "1".concat(this.handEvaluations.getLongestSuit().getSymbol());
        }
        if (this.dealerHas1m.isValid(this.board)) {
            return this.oneMinorCriteria();
        }
        if (this.dealerHas4W.isValid(this.board)) {
            return "4".concat(this.handEvaluations.getLongestSuit().getSymbol());
        }
        if (this.dealerHas3W.isValid(this.board)) {
            return "3".concat(this.handEvaluations.getLongestSuit().getSymbol());
        }
        if (this.dealerHas2W.isValid(this.board)) {
            return "2".concat(this.handEvaluations.getLongestSuit().getSymbol());
        }
        return pass;
    }

    private String oneMinorCriteria() {
        Map<Suit, Integer> numberOfCardsPerSuit = handEvaluations.getNumberOfCardsPerSuit();

        if (numberOfCardsPerSuit.get(Suit.DIAMONDS) == numberOfCardsPerSuit.get(Suit.CLUBS)) {
            if (numberOfCardsPerSuit.get(Suit.DIAMONDS) == 3) {
                return one.concat(Suit.CLUBS.getSymbol());
            } else if (numberOfCardsPerSuit.get(Suit.DIAMONDS) == 4) {
                if ((numberOfCardsPerSuit.get(Suit.DIAMONDS) == 1 && numberOfCardsPerSuit.get(Suit.CLUBS) == 4)
                        || (numberOfCardsPerSuit.get(Suit.DIAMONDS) == 4
                                && numberOfCardsPerSuit.get(Suit.CLUBS) == 1)) {
                    return one.concat(Suit.DIAMONDS.getSymbol());
                }
                return one.concat(Suit.CLUBS.getSymbol());
            } else if (numberOfCardsPerSuit.get(Suit.DIAMONDS) == 5 || numberOfCardsPerSuit.get(Suit.DIAMONDS) == 6) {
                return one.concat(Suit.DIAMONDS.getSymbol());
            }
        }
        return "1".concat(handEvaluations.getLongestSuit().getSymbol());
    }

}
