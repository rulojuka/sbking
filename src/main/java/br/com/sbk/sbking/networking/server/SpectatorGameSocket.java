package br.com.sbk.sbking.networking.server;

import java.io.IOException;

import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.networking.core.serialization.DisconnectedObject;

public class SpectatorGameSocket extends ClientGameSocket {
	public SpectatorGameSocket(PlayerNetworkInformation playerNetworkInformation, GameServer gameServer) {
		super(playerNetworkInformation, gameServer);
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
		}
		
		if (readObject instanceof String) {
			String string = (String) readObject;
			logger.info("A spectator sent this message: --" + string + "--");
			String NICKNAME = "NICKNAME";
			if(string.startsWith(NICKNAME)){
				String nickname = string.substring(NICKNAME.length());
				logger.info("Setting new nickname: --" + nickname + "--");
				this.playerNetworkInformation.setNickname(nickname);
			}
		}
		if (readObject instanceof Card) {
			Card playedCard = (Card) readObject;
			logger.info("A spectator is trying to play the " + playedCard);
		}
	}

	public void sendIsSpectator() {
		String control = "ISSPECTATOR";
		this.serializator.tryToSerialize(control);
	}

}