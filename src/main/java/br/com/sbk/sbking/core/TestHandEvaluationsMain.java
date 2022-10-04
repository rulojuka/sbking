package br.com.sbk.sbking.core;

import java.util.ArrayList;
import java.util.List;

import br.com.sbk.sbking.core.boarddealer.Complete52CardDeck;
import br.com.sbk.sbking.core.boarddealer.ShuffledBoardDealer;
import br.com.sbk.sbking.core.boardrules.BoardRule;
import br.com.sbk.sbking.core.boardrules.bridgeopenings.OpeningSystem;

public class TestHandEvaluationsMain {
    public static void main(String[] args) {
        Direction north = Direction.NORTH;
        ShuffledBoardDealer shuffledBoardDealer = new ShuffledBoardDealer();
        Complete52CardDeck complete52CardDeck = new Complete52CardDeck();
        OpeningSystem openingSystem = new OpeningSystem();
        List<BridgeContract> bridgeContracts = new ArrayList<BridgeContract>();
        bridgeContracts.add(new BridgeContract(0, null, false, false));
        bridgeContracts.add(new BridgeContract(1, Strain.CLUBS, false, false));
        bridgeContracts.add(new BridgeContract(1, Strain.DIAMONDS, false, false));
        bridgeContracts.add(new BridgeContract(1, Strain.HEARTS, false, false));
        bridgeContracts.add(new BridgeContract(1, Strain.SPADES, false, false));
        bridgeContracts.add(new BridgeContract(1, Strain.NOTRUMPS, false, false));
        bridgeContracts.add(new BridgeContract(2, Strain.CLUBS, false, false));
        bridgeContracts.add(new BridgeContract(2, Strain.DIAMONDS, false, false));
        bridgeContracts.add(new BridgeContract(2, Strain.HEARTS, false, false));
        bridgeContracts.add(new BridgeContract(2, Strain.SPADES, false, false));
        bridgeContracts.add(new BridgeContract(2, Strain.NOTRUMPS, false, false));
        bridgeContracts.add(new BridgeContract(3, Strain.CLUBS, false, false));
        bridgeContracts.add(new BridgeContract(3, Strain.DIAMONDS, false, false));
        bridgeContracts.add(new BridgeContract(3, Strain.HEARTS, false, false));
        bridgeContracts.add(new BridgeContract(3, Strain.SPADES, false, false));
        bridgeContracts.add(new BridgeContract(4, Strain.CLUBS, false, false));
        bridgeContracts.add(new BridgeContract(4, Strain.DIAMONDS, false, false));
        bridgeContracts.add(new BridgeContract(4, Strain.HEARTS, false, false));
        bridgeContracts.add(new BridgeContract(4, Strain.SPADES, false, false));
        
        for (BridgeContract bridgeContract : bridgeContracts) {
            
        for (int i = 0; i < 10;) {
            Board dealBoard = shuffledBoardDealer.dealBoard(north, complete52CardDeck.getDeck());
            Hand hand = dealBoard.getHandOf(north);
            BridgeContract contract = openingSystem.getOpeningBridgeContract(dealBoard);
            if(bridgeContract.equals(contract)){
                System.out.println(hand + ": " + hand.getHandEvaluations().getHCP() + " HCP "+ bridgeContract);
                i++;
            }            
        }
    }
    }
    
}
