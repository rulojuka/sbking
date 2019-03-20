package br.com.sbk.sbking.core.rulesets.implementations;

import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Hand;
import br.com.sbk.sbking.core.Suit;
import br.com.sbk.sbking.core.Trick;
import br.com.sbk.sbking.core.rulesets.interfaces.SuitFollowable;

public class DefaultSuitFollowable implements SuitFollowable {

	@Override
	public boolean followsSuit(Trick trick, Hand hand, Card card) {
		if (trick == null) {
			throw new NullPointerException("Trick is null.");
		}
		if (trick.isEmpty()) {
			return true;
		}
		Suit leadSuitOfTrick = trick.getLeadSuit();
		if (!hand.hasSuit(leadSuitOfTrick) || card.getSuit() == leadSuitOfTrick) {
			return true;
		}
		return false;
	}

}
