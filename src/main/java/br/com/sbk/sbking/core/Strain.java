package br.com.sbk.sbking.core;

import java.util.HashMap;

import br.com.sbk.sbking.core.rulesets.abstractrulesets.PositiveRuleset;
import br.com.sbk.sbking.core.rulesets.concrete.PositiveNoTrumpsRuleset;
import br.com.sbk.sbking.core.rulesets.concrete.PositiveWithTrumpsRuleset;

public enum Strain {
    DIAMONDS("Diamonds", "D", new PositiveWithTrumpsRuleset(Suit.DIAMONDS)),
    CLUBS("Clubs", "C", new PositiveWithTrumpsRuleset(Suit.CLUBS)),
    HEARTS("Hearts", "H", new PositiveWithTrumpsRuleset(Suit.HEARTS)),
    SPADES("Spades", "S", new PositiveWithTrumpsRuleset(Suit.SPADES)),
    NOTRUMPS("No Trumps", "NT", new PositiveNoTrumpsRuleset());

    private final String name;
    private final String shortName;
    private final PositiveRuleset positiveRuleset;

    private static HashMap<Suit, Strain> strainFromSuit = new HashMap<Suit, Strain>();
    static {
        strainFromSuit.put(Suit.CLUBS, Strain.CLUBS);
        strainFromSuit.put(Suit.DIAMONDS, Strain.DIAMONDS);
        strainFromSuit.put(Suit.HEARTS, Strain.HEARTS);
        strainFromSuit.put(Suit.SPADES, Strain.SPADES);
    }

    Strain(String name, String shortName, PositiveRuleset positiveRuleset) {
        this.name = name;
        this.shortName = shortName;
        this.positiveRuleset = positiveRuleset;
    }

    public String getName() {
        return this.name;
    }

    public String getShortName() {
        return this.shortName;
    }

    public PositiveRuleset getPositiveRuleset() {
        return positiveRuleset;
    }

    public static Strain fromSuit(Suit suit) {
        return strainFromSuit.get(suit);
    }

}
