package br.com.sbk.sbking.core.boarddealer;

import java.util.Deque;

import br.com.sbk.sbking.core.Board;
import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Direction;

public interface BoardDealer {

    Board dealBoard(Direction dealer, Deque<Card> deck);

}
