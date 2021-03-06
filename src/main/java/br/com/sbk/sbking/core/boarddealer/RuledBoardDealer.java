package br.com.sbk.sbking.core.boarddealer;

import java.util.HashMap;
import java.util.Map;

import br.com.sbk.sbking.core.Board;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.core.Hand;
import br.com.sbk.sbking.core.ShuffledDeck;
import br.com.sbk.sbking.core.boardrules.BoardRule;

public class RuledBoardDealer implements BoardDealer {

  private BoardRule boardRule;

  public RuledBoardDealer(BoardRule boardRule) {
    this.boardRule = boardRule;
  }

  @Override
  public Board dealBoard(Direction dealer) {
    Board currentBoard;
    do {
      currentBoard = this.createShuffledBoard(dealer);
    } while (!this.boardRule.isValid(currentBoard));
    return currentBoard;
  }

  private Board createShuffledBoard(Direction dealer) {
    Map<Direction, Hand> hands = new HashMap<Direction, Hand>();
    Direction currentDirection;
    ShuffledDeck currentDeck;

    currentDeck = new ShuffledDeck();
    for (Direction direction : Direction.values()) {
      hands.put(direction, new Hand());
    }
    currentDirection = dealer;

    while (currentDeck.hasCard()) {
      hands.get(currentDirection).addCard(currentDeck.dealCard());
      currentDirection = currentDirection.next();
    }
    return new Board(hands, dealer);
  }

}
