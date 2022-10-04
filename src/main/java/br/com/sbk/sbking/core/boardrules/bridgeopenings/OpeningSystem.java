package br.com.sbk.sbking.core.boardrules.bridgeopenings;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.com.sbk.sbking.core.Board;
import br.com.sbk.sbking.core.BridgeContract;
import br.com.sbk.sbking.core.Strain;
import br.com.sbk.sbking.core.Suit;
import br.com.sbk.sbking.core.boardrules.BoardRule;

public class OpeningSystem {
    
    private List<BoardRule> rulePriority = new ArrayList<>();

    public OpeningSystem() {
        //22+
        rulePriority.add(new DealerHasTwoClubsOpeningBoardRule());
        
        //12-21
        rulePriority.add(new DealerHasOneMajorOpeningBoardRule());
        rulePriority.add(new DealerHasOneNoTrumpOpeningBoardRule());
        rulePriority.add(new DealerHasTwoNoTrumpOpeningBoardRule());
        rulePriority.add(new DealerHasOneMinorOpeningBoardRule());

        //6-10
        rulePriority.add(new DealerHasFourWeakOpeningBoardRule());
        rulePriority.add(new DealerHasThreeWeakOpeningBoardRule());
        rulePriority.add(new DealerHasTwoWeakOpeningBoardRule());
    }

    private BoardRule getOpeningRule(Board board){
        for (BoardRule boardRule : rulePriority) {
            if(boardRule.isValid(board)){
                return boardRule;
            }
        }
        return null;
    }

    public BridgeContract getOpeningBridgeContract(Board board){
        BoardRule boardRule = this.getOpeningRule(board);
        if(boardRule==null){
            return new BridgeContract(0, Strain.NOTRUMPS, false, false);
        } else if(boardRule.getClass().equals(new DealerHasTwoClubsOpeningBoardRule().getClass())){
            return new BridgeContract(2, Strain.CLUBS, false, false);
        } else if(boardRule.getClass().equals(new DealerHasOneMajorOpeningBoardRule().getClass())){
            Map<Suit, Integer> numberOfCardsPerSuit = board.getHandOf(board.getDealer()).getHandEvaluations().getNumberOfCardsPerSuit();
            if(numberOfCardsPerSuit.get(Suit.SPADES) >= numberOfCardsPerSuit.get(Suit.HEARTS)){
                return new BridgeContract(1, Strain.SPADES, false, false);
            }else{
                return new BridgeContract(1, Strain.HEARTS, false, false);
            }
        } else if(boardRule.getClass().equals(new DealerHasOneNoTrumpOpeningBoardRule().getClass())){
            return new BridgeContract(1, Strain.NOTRUMPS, false, false);
        } else if(boardRule.getClass().equals(new DealerHasTwoNoTrumpOpeningBoardRule().getClass())){
            return new BridgeContract(2, Strain.NOTRUMPS, false, false);
        } else if(boardRule.getClass().equals(new DealerHasOneMinorOpeningBoardRule().getClass())){
            Map<Suit, Integer> numberOfCardsPerSuit = board.getHandOf(board.getDealer()).getHandEvaluations().getNumberOfCardsPerSuit();
            if(numberOfCardsPerSuit.get(Suit.DIAMONDS) >= numberOfCardsPerSuit.get(Suit.CLUBS)){
                return new BridgeContract(1, Strain.DIAMONDS, false, false);
            }else{
                return new BridgeContract(1, Strain.CLUBS, false, false);
            }
        } else if(boardRule.getClass().equals(new DealerHasFourWeakOpeningBoardRule().getClass())){
            Suit longestSuit = board.getHandOf(board.getDealer()).getHandEvaluations().getLongestSuit();
            return new BridgeContract(4, Strain.getFromSuit(longestSuit), false, false);
        } else if(boardRule.getClass().equals(new DealerHasThreeWeakOpeningBoardRule().getClass())){
            Suit longestSuit = board.getHandOf(board.getDealer()).getHandEvaluations().getLongestSuit();
            return new BridgeContract(3, Strain.getFromSuit(longestSuit), false, false);
        } else if(boardRule.getClass().equals(new DealerHasTwoWeakOpeningBoardRule().getClass())){
            Suit longestSuit = board.getHandOf(board.getDealer()).getHandEvaluations().getLongestSuit();
            return new BridgeContract(2, Strain.getFromSuit(longestSuit), false, false);
        } else {
            return new BridgeContract(0, Strain.NOTRUMPS, false, false);
        }
    }
}
