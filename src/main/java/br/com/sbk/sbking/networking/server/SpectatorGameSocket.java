package br.com.sbk.sbking.networking.server;

import java.io.IOException;

import br.com.sbk.sbking.core.Card;

public class SpectatorGameSocket extends ClientGameSocket {
	public SpectatorGameSocket(PlayerNetworkInformation playerNetworkInformation, GameServer gameServer) {
		super(playerNetworkInformation, gameServer);
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
		super.waitForClientSetup();
		this.sendIsSpectator();
	}

	private void processCommand() {
		// No commands yet.
		// The commands will come here later.

		// As there is no command yet, this line should halt
		Object readObject = this.serializator.tryToDeserialize(Object.class);
		if (readObject instanceof String) {
			String string = (String) readObject;
			logger.info("A spectator sent this message: --" + string + "--");
			String NICKNAME = "NICKNAME";
			if(string.startsWith(NICKNAME)){
				String nickname = string.substring(NICKNAME.length());
				logger.info(" Setting new nickname: --" + nickname + "--");
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