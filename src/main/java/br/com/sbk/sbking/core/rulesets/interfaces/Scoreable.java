package br.com.sbk.sbking.core.rulesets.interfaces;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import br.com.sbk.sbking.core.Trick;
import br.com.sbk.sbking.core.rulesets.abstractrulesets.Ruleset;

@JsonDeserialize(as = Ruleset.class)
public interface Scoreable {

    int getScoreMultiplier();

    int getPoints(Trick trick);

    int getTotalPoints();

    boolean isNegative();

}
