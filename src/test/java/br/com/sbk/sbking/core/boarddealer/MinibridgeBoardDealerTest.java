package br.com.sbk.sbking.core.boarddealer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Deque;

import org.junit.BeforeClass;
import org.junit.Test;

import br.com.sbk.sbking.core.Board;
import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Direction;

/**
 * This is not a unit test suite as it depends on ShuffledBoardDealer. It is
 * also non-deterministic. I am ok with that for now.
 */
public class MinibridgeBoardDealerTest {

  private static MinibridgeBoardDealer minibridgeBoardDealer;
  private static Direction anyDirection;
  private static Deque<Card> gameDeck;

  @BeforeClass
  public static void setup() {
    minibridgeBoardDealer = new MinibridgeBoardDealer();
    anyDirection = Direction.SOUTH;
    Complete52CardDeck complete52CardDeck = new Complete52CardDeck();
    gameDeck = complete52CardDeck.getDeck();
  }

  @Test
  public void dealBoardShouldDealABoardWithTheCorrectDealer() {
    Board minibridgeBoard = minibridgeBoardDealer.dealBoard(anyDirection, gameDeck);

    assertEquals(anyDirection, minibridgeBoard.getDealer());
  }

  @Test
  public void dealBoardShouldDealABoardWithStrictlyMoreHCPForDealerPartnership() {
    Board minibridgeBoard = minibridgeBoardDealer.dealBoard(anyDirection, gameDeck);

    int dealerPartnershipHCP = 0;
    int nonDealerPartnershipHCP = 0;
    for (Direction direction : Direction.values()) {
      int currentDirectionHCP = minibridgeBoard.getHandOf(direction).getHandEvaluations().getHCP();
      if (direction.isNorthSouth() == anyDirection.isNorthSouth()) {
        dealerPartnershipHCP += currentDirectionHCP;
      } else {
        nonDealerPartnershipHCP += currentDirectionHCP;
      }
    }
    assertTrue(dealerPartnershipHCP > nonDealerPartnershipHCP);
  }

  @Test
  public void dealBoardShouldDealABoardWithEqualOrMoreHCPForDealerThanTheirPartner() {
    Board minibridgeBoard = minibridgeBoardDealer.dealBoard(anyDirection, gameDeck);

    int dealerHCP = minibridgeBoard.getHandOf(anyDirection).getHandEvaluations().getHCP();
    int dealerPartnerHCP = minibridgeBoard.getHandOf(anyDirection.next(2)).getHandEvaluations().getHCP();
    assertTrue(dealerHCP >= dealerPartnerHCP);
  }

}
