package br.com.sbk.sbking.core;

import java.util.HashMap;
import java.util.Map;

public class MinibridgeBoardDealer implements BoardDealer {

  private Board board;

  /**
   * Deals a Board where the partnership of the dealer has strictly more points
   * than the other partnership and the dealer has more or equal HCP than its
   * partner.
   */
  @Override
  public Board dealBoard(Direction dealer) {
    ShuffledBoardDealer shuffledBoardDealer = new ShuffledBoardDealer();
    int dealerPartnershipHCP;
    int nonDealerPartnershipHCP;

    do {
      dealerPartnershipHCP = 0;
      nonDealerPartnershipHCP = 0;
      this.board = shuffledBoardDealer.dealBoard(dealer);
      for (Direction direction : Direction.values()) {
        HandEvaluations handEvaluations = this.board.getHandOf(direction).getHandEvaluations();
        int hcp = handEvaluations.getHCP();
        if (direction.isNorthSouth() == dealer.isNorthSouth()) {
          dealerPartnershipHCP += hcp;
        } else {
          nonDealerPartnershipHCP += hcp;
        }
      }
    } while (dealerPartnershipHCP == nonDealerPartnershipHCP);

    if (dealerPartnershipHCP < nonDealerPartnershipHCP) {
      this.board = this.rotateHands(this.board, 1);
    }

    HandEvaluations dealerHandEvaluations = this.board.getHandOf(dealer).getHandEvaluations();
    HandEvaluations dealerPartnerHandEvaluations = this.board.getHandOf(dealer.next(2)).getHandEvaluations();
    if (dealerHandEvaluations.getHCP() < dealerPartnerHandEvaluations.getHCP()) {
      this.board = this.rotateHands(this.board, 2);
    }

    return this.board;
  }

  private Board rotateHands(Board board, int i) {
    Map<Direction, Hand> hands = new HashMap<Direction, Hand>();
    for (Direction direction : Direction.values()) {
      hands.put(direction.next(i), board.getHandOf(direction));
    }
    return new Board(hands, board.getDealer());
  }

}
