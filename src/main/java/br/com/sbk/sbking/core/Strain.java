package br.com.sbk.sbking.core;

import br.com.sbk.sbking.core.rulesets.abstractrulesets.PositiveRuleset;
import br.com.sbk.sbking.core.rulesets.concrete.PositiveNoTrumpsRuleset;
import br.com.sbk.sbking.core.rulesets.concrete.PositiveWithTrumpsRuleset;

/**
 * The Laws of Bridge use "Denomination" but we will use Strain as it is more common.
 */
public enum Strain {
    CLUBS("Clubs", new PositiveWithTrumpsRuleset(Suit.CLUBS)),
    DIAMONDS("Diamonds", new PositiveWithTrumpsRuleset(Suit.DIAMONDS)),
    HEARTS("Hearts", new PositiveWithTrumpsRuleset(Suit.HEARTS)),
    SPADES("Spades", new PositiveWithTrumpsRuleset(Suit.SPADES)),
    NOTRUMPS("No Trumps", new PositiveNoTrumpsRuleset());

    private final String name;
    private final PositiveRuleset positiveRuleset;

    Strain(String name, PositiveRuleset positiveRuleset) {
        this.name = name;
        this.positiveRuleset = positiveRuleset;
    }

    public String getName() {
        return this.name;
    }

    public PositiveRuleset getPositiveRuleset() {
        return positiveRuleset;
    }

    public String getSymbol() {
        return String.valueOf(this.name.charAt(0));
    }

}
