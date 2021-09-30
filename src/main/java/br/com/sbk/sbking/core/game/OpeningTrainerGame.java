package br.com.sbk.sbking.core.game;

import java.util.Map;

import br.com.sbk.sbking.core.Board;
import br.com.sbk.sbking.core.BridgeContract;
import br.com.sbk.sbking.core.HandEvaluations;
import br.com.sbk.sbking.core.Strain;
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

    public BridgeContract getOpening() {
        if (this.dealerHas2C.isValid(this.board)) {
            return new BridgeContract(2, Strain.CLUBS, false, false, false, board.getDealer());
        }
        if (this.dealerHas2NT.isValid(this.board)) {
            return new BridgeContract(2, Strain.NOTRUMPS, false, false, false, board.getDealer());
        }
        if (this.dealerHas1NT.isValid(this.board)) {
            return new BridgeContract(1, Strain.NOTRUMPS, false, false, false, board.getDealer());
        }
        if (this.dealerHas1M.isValid(this.board)) {
            Suit longestSuit = this.handEvaluations.getLongestSuit();
            return new BridgeContract(1, Strain.fromSuit(longestSuit), false, false, false, board.getDealer());
        }
        if (this.dealerHas1m.isValid(this.board)) {
            return this.oneMinorCriteria();
        }
        if (this.dealerHas4W.isValid(this.board)) {
            Suit longestSuit = this.handEvaluations.getLongestSuit();
            return new BridgeContract(4, Strain.fromSuit(longestSuit), false, false, false, board.getDealer());
        }
        if (this.dealerHas3W.isValid(this.board)) {
            Suit longestSuit = this.handEvaluations.getLongestSuit();
            return new BridgeContract(3, Strain.fromSuit(longestSuit), false, false, false, board.getDealer());
        }
        if (this.dealerHas2W.isValid(this.board)) {
            Suit longestSuit = this.handEvaluations.getLongestSuit();
            return new BridgeContract(2, Strain.fromSuit(longestSuit), false, false, false, board.getDealer());
        }
        return new BridgeContract(1, null, false, false, true, board.getDealer());
    }

    private BridgeContract oneMinorCriteria() {
        Map<Suit, Integer> numberOfCardsPerSuit = handEvaluations.getNumberOfCardsPerSuit();
        Integer numberOfDiamondCards = numberOfCardsPerSuit.get(Suit.DIAMONDS);
        Integer numberOfClubCards = numberOfCardsPerSuit.get(Suit.CLUBS);
        BridgeContract oneClubs = new BridgeContract(1, Strain.CLUBS, false, false, false, board.getDealer());
        BridgeContract oneDiamonds = new BridgeContract(1, Strain.DIAMONDS, false, false, false, board.getDealer());

        if (numberOfDiamondCards > numberOfClubCards) {
            return oneDiamonds;
        }

        if (numberOfDiamondCards < numberOfClubCards) {
            return oneClubs;
        }

        if (handEvaluations.isBalanced()) {
            return oneClubs;
        } else {
            return oneDiamonds;
        }
    }

}
