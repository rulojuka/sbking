package br.com.sbk.sbking.core.rulesets.implementations;

import java.io.Serializable;

import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.core.Suit;
import br.com.sbk.sbking.core.Trick;
import br.com.sbk.sbking.core.rulesets.interfaces.Winnable;

@SuppressWarnings("serial")
public class TrumpSuitWinnable implements Winnable, Serializable {

    private Suit trumpSuit;

    public TrumpSuitWinnable(Suit trumpSuit) {
        this.trumpSuit = trumpSuit;
    }

    @Override
    public Direction getWinner(Trick trick) {
        return trick.getWinnerWithTrumpSuit(this.trumpSuit);
    }

}
