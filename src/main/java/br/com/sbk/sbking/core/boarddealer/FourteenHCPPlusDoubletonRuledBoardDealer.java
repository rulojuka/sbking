package br.com.sbk.sbking.core.boarddealer;

import java.util.Deque;

import br.com.sbk.sbking.core.Board;
import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.core.boardrules.BoardRule;
import br.com.sbk.sbking.core.boardrules.PositiveOrNegativeChooserHasFourteenHCPPlusAndDoubletonAtLeastInAllSuits;

public class FourteenHCPPlusDoubletonRuledBoardDealer implements BoardDealer {

    private RuledBoardDealer ruledBoardDealer;

    public FourteenHCPPlusDoubletonRuledBoardDealer() {
        BoardRule boardRule = new PositiveOrNegativeChooserHasFourteenHCPPlusAndDoubletonAtLeastInAllSuits();
        this.ruledBoardDealer = new RuledBoardDealer(boardRule);
    }

    @Override
    public Board dealBoard(Direction dealer, Deque<Card> deck) {
        return ruledBoardDealer.dealBoard(dealer, deck);
    }

}
