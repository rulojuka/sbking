package br.com.sbk.sbking.core.rulesets.concrete;

import br.com.sbk.sbking.core.Suit;
import br.com.sbk.sbking.core.rulesets.abstractClasses.PositiveRuleset;
import br.com.sbk.sbking.core.rulesets.implementations.DefaultSuitFollowable;
import br.com.sbk.sbking.core.rulesets.implementations.DontProhibitsHearts;
import br.com.sbk.sbking.core.rulesets.implementations.TrumpSuitWinnable;

@SuppressWarnings("serial")
public class PositiveWithTrumpsRuleset extends PositiveRuleset {

    private Suit trumpSuit;

    public PositiveWithTrumpsRuleset(Suit trumpSuit) {
        this.suitFollowable = new DefaultSuitFollowable();
        this.heartsProhibitable = new DontProhibitsHearts();
        this.winnable = new TrumpSuitWinnable(trumpSuit);
        this.trumpSuit = trumpSuit;
    }

    @Override
    public String getShortDescription() {
        return "Positive " + trumpSuit.getName().toLowerCase();
    }

    @Override
    public String getCompleteDescription() {
        return "Make the most tricks with " + trumpSuit.getName().toLowerCase() + " as trump suit";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((trumpSuit == null) ? 0 : trumpSuit.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        PositiveWithTrumpsRuleset other = (PositiveWithTrumpsRuleset) obj;
        if (trumpSuit != other.trumpSuit) {
            return false;
        }
        return true;
    }

}
