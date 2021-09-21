package br.com.sbk.sbking.core.game;

import br.com.sbk.sbking.core.Board;
import br.com.sbk.sbking.core.HandEvaluations;
import br.com.sbk.sbking.core.Suit;
import br.com.sbk.sbking.core.boardrules.bridgeopenings.DealerHasOneMajorOpeningBoardRule;
import br.com.sbk.sbking.core.boardrules.bridgeopenings.DealerHasOneMinorOpeningBoardRule;
import br.com.sbk.sbking.core.boardrules.bridgeopenings.DealerHasOneNoTrumpOpeningBoardRule;
import br.com.sbk.sbking.core.boardrules.bridgeopenings.DealerHasTwoClubsOpeningBoardRule;
import br.com.sbk.sbking.core.boardrules.bridgeopenings.DealerHasTwoNoTrumpOpeningBoardRule;

public class OpeningTrainerGame {

    private final String twoClubs = "2C";
    private final String twoNoTrump = "2NT";
    private final String oneNoTrump = "1NT";
    private final String oneMajor = "1M";
    private final String oneMinor = "1m";

    public String getOpening(Board board) {
        HandEvaluations handEvaluations = board.getHandOf(board.getDealer()).getHandEvaluations();
        if (DealerHasTwoClubsOpeningBoardRule.isValid(board)) {
            return twoClubs;
        } else if (DealerHasTwoNoTrumpOpeningBoardRule.isValid(board)) {
            return twoNoTrump;
        } else if (DealerHasOneNoTrumpOpeningBoardRule.isValid(board)) {
            return oneNoTrump;
        } else if (DealerHasOneMajorOpeningBoardRule.isValid(board)) {
            if (handEvaluations.getLongestSuit() == Suit.HEARTS) {
                return "1h";
            } else {
                return "1s";
            }
        } else if (DealerHasOneMinorOpeningBoardRule.isValid(board)) {

        } else if (handEvaluations.getHCP() == 11) {
            // barragem
        }
    }

}
