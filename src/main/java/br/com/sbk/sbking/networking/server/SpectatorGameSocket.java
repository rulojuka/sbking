package br.com.sbk.sbking.networking.server;

import java.io.IOException;

import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.networking.core.serialization.DisconnectedObject;

public class SpectatorGameSocket extends ClientGameSocket {
	public SpectatorGameSocket(PlayerNetworkInformation playerNetworkInformation, Table table) {
		super(playerNetworkInformation, table);
	}

	@Override
	protected void setup() throws IOException, InterruptedException {
		super.waitForClientSetup();
		this.sendIsSpectator();
	}

	@Override
	protected void processCommand() {
		Object readObject = this.serializator.tryToDeserialize(Object.class);
		if (readObject instanceof DisconnectedObject) {
			this.hasDisconnected = true;
		} else if (readObject instanceof String) {
			String string = (String) readObject;
			logger.info("A spectator sent this message: --" + string + "--");
			String NICKNAME = "NICKNAME";
			if (string.startsWith(NICKNAME)) {
				String nickname = string.substring(NICKNAME.length());
				logger.info("Setting new nickname: --" + nickname + "--");
				this.playerNetworkInformation.setNickname(nickname);
			}
		} else if (readObject instanceof Card) {
			Card playedCard = (Card) readObject;
			logger.info("A spectator is trying to play the " + playedCard);
		} else if (readObject instanceof Direction) {
			Direction direction = (Direction) readObject;
			// Sit in direction
			logger.info("A spectator is trying to sit on " + direction);
			this.table.addPlayer(this.playerNetworkInformation, direction);
		}
	}

	public void sendIsSpectator() {
		String control = "ISSPECTATOR";
		this.serializator.tryToSerialize(control);
	}

}