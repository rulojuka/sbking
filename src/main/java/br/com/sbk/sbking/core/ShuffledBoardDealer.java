package br.com.sbk.sbking.core;

import java.util.HashMap;
import java.util.Map;

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
