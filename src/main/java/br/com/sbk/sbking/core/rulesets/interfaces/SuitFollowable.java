package br.com.sbk.sbking.core.rulesets.interfaces;

import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Hand;
import br.com.sbk.sbking.core.Trick;

public interface SuitFollowable {

    public boolean followsSuit(Trick trick, Hand hand, Card card);

}
