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
		final int prime = 31;
		int result = 1;
		result = prime * result + ((heartsProhibitable == null) ? 0 : heartsProhibitable.hashCode());
		result = prime * result + ((suitFollowable == null) ? 0 : suitFollowable.hashCode());
		result = prime * result + ((winnable == null) ? 0 : winnable.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ruleset other = (Ruleset) obj;
		if (heartsProhibitable == null) {
			if (other.heartsProhibitable != null)
				return false;
		} else if (!(heartsProhibitable.getClass()==other.heartsProhibitable.getClass()))
			return false;
		if (suitFollowable == null) {
			if (other.suitFollowable != null)
				return false;
		} else if (!(suitFollowable.getClass()==other.suitFollowable.getClass()))
			return false;
		if (winnable == null) {
			if (other.winnable != null)
				return false;
		} else if (!(winnable.getClass()==other.winnable.getClass()))
			return false;
		return true;
	}
	
	

}
