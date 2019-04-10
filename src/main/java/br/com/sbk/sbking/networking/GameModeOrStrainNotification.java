package br.com.sbk.sbking.networking;

import br.com.sbk.sbking.core.rulesets.abstractClasses.Ruleset;

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
