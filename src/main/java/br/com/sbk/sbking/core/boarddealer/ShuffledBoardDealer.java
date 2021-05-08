package br.com.sbk.sbking.core.boarddealer;

import java.util.HashMap;
import java.util.Map;

import br.com.sbk.sbking.core.Board;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.core.Hand;
import br.com.sbk.sbking.core.ShuffledDeck;

public class ShuffledBoardDealer implements BoardDealer {

  private Map<Direction, Hand> hands;

  @Override
  public Board dealBoard(Direction dealer) {
    Direction currentDirection;
    Hand currentHand;
    ShuffledDeck currentDeck = new ShuffledDeck();
    hands = new HashMap<Direction, Hand>();
    for (Direction direction : Direction.values()) {
      hands.put(direction, new Hand());
    }
    for (currentDirection = dealer; currentDeck.hasCard(); currentDirection = currentDirection.next()) {
      currentHand = this.hands.get(currentDirection);
      currentHand.addCard(currentDeck.dealCard());
    }
    return new Board(hands, dealer);
  }

}
