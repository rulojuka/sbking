package br.com.sbk.sbking.core.rulesets.abstractClasses;

import java.io.Serializable;

import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.core.Hand;
import br.com.sbk.sbking.core.Trick;
import br.com.sbk.sbking.core.rulesets.interfaces.Descriptionable;
import br.com.sbk.sbking.core.rulesets.interfaces.HeartsProhibitable;
import br.com.sbk.sbking.core.rulesets.interfaces.Scoreable;
import br.com.sbk.sbking.core.rulesets.interfaces.SuitFollowable;
import br.com.sbk.sbking.core.rulesets.interfaces.Winnable;

@SuppressWarnings("serial")
public abstract class Ruleset
		implements SuitFollowable, Scoreable, Descriptionable, HeartsProhibitable, Winnable, Serializable {

	protected SuitFollowable suitFollowable;
	protected HeartsProhibitable heartsProhibitable;
	protected Winnable winnable;

	@Override
	public boolean followsSuit(Trick trick, Hand hand, Card card) {
		return suitFollowable.followsSuit(trick, hand, card);
	}

	@Override
	public boolean prohibitsHeartsUntilOnlySuitLeft() {
		return heartsProhibitable.prohibitsHeartsUntilOnlySuitLeft();
	}

	@Override
	public Direction getWinner(Trick trick) {
		return winnable.getWinner(trick);
	}

	@Override
	public int hashCode() {
		return this.getClass().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		Class<? extends Ruleset> myClass = this.getClass();
		return myClass.equals(obj.getClass());
	}

}
