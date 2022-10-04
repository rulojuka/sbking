package br.com.sbk.sbking.core;

import br.com.sbk.sbking.core.rulesets.abstractrulesets.PositiveRuleset;
import br.com.sbk.sbking.core.rulesets.concrete.PositiveNoTrumpsRuleset;
import br.com.sbk.sbking.core.rulesets.concrete.PositiveWithTrumpsRuleset;

public enum Strain {
    DIAMONDS("Diamonds", new PositiveWithTrumpsRuleset(Suit.DIAMONDS)),
    CLUBS("Clubs", new PositiveWithTrumpsRuleset(Suit.CLUBS)),
    HEARTS("Hearts", new PositiveWithTrumpsRuleset(Suit.HEARTS)),
    SPADES("Spades", new PositiveWithTrumpsRuleset(Suit.SPADES)), NOTRUMPS("No Trumps", new PositiveNoTrumpsRuleset());

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
        if(NOTRUMPS.equals(this)){
            return "NT";
        } else {
            PositiveWithTrumpsRuleset ruleset = (PositiveWithTrumpsRuleset) this.getPositiveRuleset();
            char unicodeSymbol = ruleset.getTrumpSuit().getUnicodeSymbol();
            return Character.toString(unicodeSymbol);
        }
    }

    public static Strain getFromSuit(Suit suit){
        switch(suit){
            case CLUBS:
                return Strain.CLUBS;
            case DIAMONDS:
                return Strain.DIAMONDS;
            case HEARTS:
                return Strain.HEARTS;
            case SPADES:
                return Strain.SPADES;
            default:
                return Strain.NOTRUMPS;
        }
    }

}
