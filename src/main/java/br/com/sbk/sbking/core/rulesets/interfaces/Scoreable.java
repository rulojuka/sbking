package br.com.sbk.sbking.core.rulesets.interfaces;

import br.com.sbk.sbking.core.Trick;

public interface Scoreable {

    int getScoreMultiplier();

    int getPoints(Trick trick);

    int getTotalPoints();

    boolean isNegative();

}
