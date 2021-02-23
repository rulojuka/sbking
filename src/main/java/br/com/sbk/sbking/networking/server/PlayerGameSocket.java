package br.com.sbk.sbking.networking.server;

import java.io.IOException;

import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.core.rulesets.RulesetFromShortDescriptionIdentifier;
import br.com.sbk.sbking.core.rulesets.abstractClasses.Ruleset;
import br.com.sbk.sbking.gui.models.PositiveOrNegative;
import br.com.sbk.sbking.networking.core.serialization.DisconnectedObject;

public class PlayerGameSocket extends ClientGameSocket {
	private Direction direction;

	public PlayerGameSocket(PlayerNetworkInformation playerNetworkInformation, Direction direction,
			GameServer gameServer) {
		super(playerNetworkInformation, gameServer);
		this.direction = direction;
	}

	public Direction getDirection() {
		return direction;
	}

	@Override
	protected void setup() throws IOException, InterruptedException {
		super.waitForClientSetup();
		this.sendDirection(direction);
	}

	@Override
	protected void processCommand() {
		Object readObject = this.serializator.tryToDeserialize(Object.class);
		if (readObject instanceof DisconnectedObject) {
			this.hasDisconnected = true;
		}
		if (readObject instanceof String) {
			String string = (String) readObject;
			logger.info(this.direction + " sent this message: --" + string + "--");
			String POSITIVE = "POSITIVE";
			String NEGATIVE = "NEGATIVE";
			String NICKNAME = "NICKNAME";
			if (string.startsWith(NICKNAME)) {
				String nickname = string.substring(NICKNAME.length());
				logger.info("Setting new nickname: --" + nickname + "--");
				this.playerNetworkInformation.setNickname(nickname);
			} else {
				KingGameServer kingGameServer = (KingGameServer) gameServer;
				if (POSITIVE.equals(string) || NEGATIVE.equals(string)) {
					PositiveOrNegative positiveOrNegative = new PositiveOrNegative();
					if (POSITIVE.equals(string)) {
						positiveOrNegative.setPositive();
					} else {
						positiveOrNegative.setNegative();
					}
					kingGameServer.notifyChoosePositiveOrNegative(positiveOrNegative, this.direction);
				} else {
					Ruleset gameModeOrStrain = RulesetFromShortDescriptionIdentifier.identify(string);
					if (gameModeOrStrain != null) {
						kingGameServer.notifyChooseGameModeOrStrain(gameModeOrStrain, direction);
					}
				}
			}
		}
		if (readObject instanceof Card) {
			Card playedCard = (Card) readObject;
			logger.info(this.direction + " is trying to play the " + playedCard);
			gameServer.notifyPlayCard(playedCard, this.direction);
		}
	}

}