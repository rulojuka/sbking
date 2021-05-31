package br.com.sbk.sbking.core.boardrules;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.sbk.sbking.core.Board;
import br.com.sbk.sbking.core.BridgeContract;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.core.Hand;
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
import br.com.sbk.sbking.core.exceptions.HandHasNoDefinedOpeningException;

public class BridgeOpener {

  private List<BoardRule> boardRules;

  public BridgeOpener(List<BoardRule> boardRules) {
    this.boardRules = boardRules;
  }

  public BridgeContract getOpening(Hand hand) {
    Map<Direction, Hand> hands = new HashMap<Direction, Hand>();
    hands.put(Direction.NORTH, hand);
    hands.put(Direction.EAST, new Hand());
    hands.put(Direction.SOUTH, new Hand());
    hands.put(Direction.WEST, new Hand());
    Board board = new Board(hands, Direction.NORTH);
    for (BoardRule boardRule : boardRules) {
      if (boardRule.isValid(board)) {
        BridgeContract contract = null;
        try {
          contract = this.getContract(boardRule, hand);
        } catch (HandHasNoDefinedOpeningException e) {
          contract = new BridgeContract(7, Strain.NOTRUMPS, false, false);
        }
        // System.out.println("The contract is: " + contract.getLevel() + " " +
        // contract.getStrain().getName());
        if (contract == null) {
          contract = new BridgeContract(7, Strain.NOTRUMPS, false, false);
        }
        return contract;
      }
    }
    // HandEvaluations handEvaluations = new HandEvaluations(hand);
    // System.out.println("The PASS hand is:");
    // System.out.println(hand.toString());
    // System.out.println("HCP: " + handEvaluations.getHCP());
    return new BridgeContract(7, Strain.NOTRUMPS, false, false);
  }

  private BridgeContract getContract(BoardRule boardRule, Hand hand) {
    HandEvaluations handEvaluations = new HandEvaluations(hand);
    // System.out.println("The hand is:");
    // System.out.println(hand.toString());
    // System.out.println("HCP: " + handEvaluations.getHCP());
    // System.out.println("The rule is:");
    // System.out.println(boardRule.getClass());

    if (boardRule instanceof DealerHasFourWeakOpeningBoardRule) {
      Suit longestSuit = handEvaluations.getLongestSuit();
      Strain strain = this.strainToSuitMapper(longestSuit);
      return new BridgeContract(4, strain, false, false);
    }

    if (boardRule instanceof DealerHasOneMajorOpeningBoardRule) {
      int numberOfSpades = handEvaluations.getSizeOf(Suit.SPADES);
      int numberOfHearts = handEvaluations.getSizeOf(Suit.HEARTS);
      Suit correctSuit;
      if (numberOfSpades >= numberOfHearts) {
        correctSuit = Suit.SPADES;
      } else {
        correctSuit = Suit.HEARTS;
      }
      Strain strain = this.strainToSuitMapper(correctSuit);
      return new BridgeContract(1, strain, false, false);
    }

    if (boardRule instanceof DealerHasOneMinorOpeningBoardRule) {
      Map<Suit, Integer> numberOfCardsOf = handEvaluations.getNumberOfCardsPerSuit();
      int clubs = numberOfCardsOf.get(Suit.CLUBS);
      int diamonds = numberOfCardsOf.get(Suit.DIAMONDS);
      Suit correctSuit;
      if (diamonds > clubs) {
        correctSuit = Suit.DIAMONDS;
      } else if (diamonds < clubs) {
        correctSuit = Suit.CLUBS;
      } else if (handEvaluations.isBalanced()) { // Equal number of cards
        correctSuit = Suit.CLUBS;
      } else {
        correctSuit = Suit.DIAMONDS;
      }
      Strain strain = this.strainToSuitMapper(correctSuit);
      return new BridgeContract(1, strain, false, false);
    }

    if (boardRule instanceof DealerHasOneNoTrumpOpeningBoardRule) {
      return new BridgeContract(1, Strain.NOTRUMPS, false, false);
    }

    if (boardRule instanceof DealerHasThreeWeakOpeningBoardRule) {
      Suit longestSuit = handEvaluations.getLongestSuit();
      Strain strain = this.strainToSuitMapper(longestSuit);
      return new BridgeContract(3, strain, false, false);
    }

    if (boardRule instanceof DealerHasTwoClubsOpeningBoardRule) {
      return new BridgeContract(2, Strain.CLUBS, false, false);
    }

    if (boardRule instanceof DealerHasTwoNoTrumpOpeningBoardRule) {
      return new BridgeContract(2, Strain.NOTRUMPS, false, false);
    }

    if (boardRule instanceof DealerHasTwoWeakOpeningBoardRule) {
      Suit longestSuit = handEvaluations.getLongestSuit();
      if (Suit.CLUBS.equals(longestSuit)) {
        return new BridgeContract(3, Strain.CLUBS, false, false);
      } else {
        Strain strain = this.strainToSuitMapper(longestSuit);
        return new BridgeContract(2, strain, false, false);
      }
    }

    throw new HandHasNoDefinedOpeningException(hand);

  }

  private Strain strainToSuitMapper(Suit suit) {
    if (Suit.CLUBS.equals(suit)) {
      return Strain.CLUBS;
    }
    if (Suit.DIAMONDS.equals(suit)) {
      return Strain.DIAMONDS;
    }
    if (Suit.HEARTS.equals(suit)) {
      return Strain.HEARTS;
    }
    if (Suit.SPADES.equals(suit)) {
      return Strain.SPADES;
    }
    return Strain.NOTRUMPS;
  }

}
