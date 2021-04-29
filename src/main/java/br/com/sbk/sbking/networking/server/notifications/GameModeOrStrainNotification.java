package br.com.sbk.sbking.networking.server.notifications;

import br.com.sbk.sbking.core.rulesets.abstractrulesets.Ruleset;

public class GameModeOrStrainNotification {

    private Ruleset gameModeOrStrain;

    public void notifyAllWithGameModeOrStrain(Ruleset gameModeOrStrain) {
        this.gameModeOrStrain = gameModeOrStrain;
        this.notifyAll();
    }

    public Ruleset getGameModeOrStrain() {
        return this.gameModeOrStrain;
    }

}
