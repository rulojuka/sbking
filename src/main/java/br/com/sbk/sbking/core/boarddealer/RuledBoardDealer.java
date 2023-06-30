package br.com.sbk.sbking.core.boarddealer;

import java.util.Deque;
import java.util.EnumMap;
import java.util.Map;

import br.com.sbk.sbking.core.Board;
import br.com.sbk.sbking.core.Card;
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
  public Board dealBoard(Direction dealer, Deque<Card> deck) {
    Board currentBoard;
    do {
      currentBoard = this.createShuffledBoard(dealer, deck);
    } while (!this.boardRule.isValid(currentBoard));
    return currentBoard;
  }

  private Board createShuffledBoard(Direction dealer, Deque<Card> deck) {
    Map<Direction, Hand> hands = new EnumMap<Direction, Hand>(Direction.class);
    Direction currentDirection;
    ShuffledDeck currentDeck;

    currentDeck = new ShuffledDeck(deck);
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
