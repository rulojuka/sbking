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
    Map<Direction, Integer> hcps = new HashMap<Direction, Integer>();
    ShuffledBoardDealer shuffledBoardDealer = new ShuffledBoardDealer();
    int dealerPartnershipHCP;
    int nonDealerPartnershipHCP;

    do {
      dealerPartnershipHCP = 0;
      nonDealerPartnershipHCP = 0;
      this.board = shuffledBoardDealer.dealBoard(dealer);
      for (Direction direction : Direction.values()) {
        int hcp = this.board.getHandOf(direction).getHCP();
        hcps.put(direction, hcp);
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

    if (this.board.getHandOf(dealer).getHCP() < this.board.getHandOf(dealer.next(2)).getHCP()) {
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
