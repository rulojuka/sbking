package br.com.sbk.sbking.core;

import br.com.sbk.sbking.core.boardrules.PositiveOrNegativeChooserHasFourteenHCPPlusAndDoubletonAtLeastInAllSuits;

public class FourteenHCPPlusDoubletonRuledBoardDealer implements BoardDealer {

    @Override
    public Board dealBoard(Direction dealer) {
        BoardRule boardRule = new PositiveOrNegativeChooserHasFourteenHCPPlusAndDoubletonAtLeastInAllSuits();
        RuledBoardDealer ruledBoardDealer = new RuledBoardDealer(boardRule);
        return ruledBoardDealer.dealBoard(dealer);
    }

}
