package br.com.sbk.sbking.core.boarddealer;

import br.com.sbk.sbking.core.Board;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.core.boardrules.BoardRule;
import br.com.sbk.sbking.core.boardrules.PositiveOrNegativeChooserHasFourteenHCPPlusAndDoubletonAtLeastInAllSuits;

public class FourteenHCPPlusDoubletonRuledBoardDealer implements BoardDealer {

    @Override
    public Board dealBoard(Direction dealer) {
        BoardRule boardRule = new PositiveOrNegativeChooserHasFourteenHCPPlusAndDoubletonAtLeastInAllSuits();
        RuledBoardDealer ruledBoardDealer = new RuledBoardDealer(boardRule);
        return ruledBoardDealer.dealBoard(dealer);
    }

}
