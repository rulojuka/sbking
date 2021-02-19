package br.com.sbk.sbking.networking.server;

import java.io.IOException;
import java.net.Socket;

import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.core.rulesets.RulesetFromShortDescriptionIdentifier;
import br.com.sbk.sbking.core.rulesets.abstractClasses.Ruleset;
import br.com.sbk.sbking.gui.models.PositiveOrNegative;
import br.com.sbk.sbking.networking.core.serialization.Serializator;

public class PlayerGameSocket extends ClientGameSocket {
	private Direction direction;

	public PlayerGameSocket(Serializator serializator, Socket socket, Direction direction, GameServer gameServer) {
		super(serializator, socket, gameServer);
		this.direction = direction;
	}

	@Override
	public void run() {
		logger.info("Connected: " + socket);
		try {
			setup();
			while (true) {
				processCommand();
			}
		} catch (Exception e) {
			logger.debug("Error:" + socket, e);
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				logger.debug(e);
			}
			logger.info("Closed: " + socket + ". Removing (myself) from playerSocketList");
			gameServer.removeClientGameSocket(this);
		}
	}

	private void setup() throws IOException, InterruptedException {
		logger.info("Sleeping for 500ms waiting for client to setup itself");
		Thread.sleep(500);
		this.sendDirection(direction);
	}

	private void processCommand() {
		Object readObject = this.serializator.tryToDeserialize(Object.class);
		if (readObject instanceof String) {
			String string = (String) readObject;
			logger.info(this.direction + " sent this message: --" + string + "--");
			String POSITIVE = "POSITIVE";
			String NEGATIVE = "NEGATIVE";
			if (POSITIVE.equals(string) || NEGATIVE.equals(string)) {
				PositiveOrNegative positiveOrNegative = new PositiveOrNegative();
				if (POSITIVE.equals(string)) {
					positiveOrNegative.setPositive();
				} else {
					positiveOrNegative.setNegative();
				}
				gameServer.notifyChoosePositiveOrNegative(positiveOrNegative, this.direction);
			} else {
				Ruleset gameModeOrStrain = RulesetFromShortDescriptionIdentifier.identify(string);
				if (gameModeOrStrain != null) {
					gameServer.notifyChooseGameModeOrStrain(gameModeOrStrain, direction);
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