package br.com.sbk.sbking.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * This is not a unit test suite as it depends on ShuffledBoardDealer. It is
 * also non-deterministic. I am ok with that for now.
 */
public class MinibridgeBoardDealerTest {

  private static MinibridgeBoardDealer minibridgeBoardDealer;
  private static Direction anyDirection;

  @BeforeClass
  public static void setup() {
    minibridgeBoardDealer = new MinibridgeBoardDealer();
    anyDirection = Direction.SOUTH;
  }

  @Test
  public void dealBoardShouldDealABoardWithTheCorrectDealer() {
    Board minibridgeBoard = minibridgeBoardDealer.dealBoard(anyDirection);

    assertEquals(anyDirection, minibridgeBoard.getDealer());
  }

  @Test
  public void dealBoardShouldDealABoardWithStrictlyMoreHCPForDealerPartnership() {
    Board minibridgeBoard = minibridgeBoardDealer.dealBoard(anyDirection);

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
    Board minibridgeBoard = minibridgeBoardDealer.dealBoard(anyDirection);

    int dealerHCP = minibridgeBoard.getHandOf(anyDirection).getHandEvaluations().getHCP();
    int dealerPartnerHCP = minibridgeBoard.getHandOf(anyDirection.next(2)).getHandEvaluations().getHCP();
    assertTrue(dealerHCP >= dealerPartnerHCP);
  }

}
