package br.com.sbk.sbking.networking.server;

import java.io.IOException;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.sbk.sbking.core.Board;
import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.core.Player;
import br.com.sbk.sbking.core.rulesets.RulesetFromShortDescriptionIdentifier;
import br.com.sbk.sbking.core.rulesets.abstractClasses.Ruleset;
import br.com.sbk.sbking.gui.models.KingGameScoreboard;
import br.com.sbk.sbking.gui.models.PositiveOrNegative;
import br.com.sbk.sbking.networking.core.serialization.DisconnectedObject;
import br.com.sbk.sbking.networking.core.serialization.Serializator;

public class ClientGameSocket implements Runnable {
	final static Logger logger = LogManager.getLogger(ClientGameSocket.class);

	private PlayerNetworkInformation playerNetworkInformation;
	private Table table;
	private boolean hasDisconnected = false;
	private Direction direction;

	public boolean isSpectator() {
		return direction == null;
	}

	public ClientGameSocket(PlayerNetworkInformation playerNetworkInformation, Direction direction, Table table) {
		this.playerNetworkInformation = playerNetworkInformation;
		this.table = table;
		this.direction = direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public void unsetDirection(){
		this.direction = null;
	}

	private void setup() throws IOException, InterruptedException {
		this.waitForClientSetup();
		if (this.isSpectator()) {
			this.sendIsSpectator();
		} else {
			this.sendIsNotSpectator();
			this.waitForClientSetup();
			this.sendDirection(direction);
		}
		this.waitForClientSetup();
	}

	@Override
	public void run() {
		logger.info("Connected: " + this.getSocket());
		try {
			setup();
			while (!hasDisconnected) {
				processCommand();
			}
		} catch (Exception e) {
			logger.debug("Error:" + this.getSocket(), e);
		} finally {
			disconnect();
		}
	}

	private void processCommand() {

		Object readObject = this.getSerializator().tryToDeserialize(Object.class);
		if (readObject instanceof DisconnectedObject) {
			this.hasDisconnected = true;
			return;
		}

		if (this.isSpectator()) {
			if (readObject instanceof String) {
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
				logger.info("A spectator is trying to sit on " + direction);
				this.table.moveSpectatorToSeat(this, direction);
			}
		} else {
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
					KingGameServer kingGameServer = (KingGameServer) this.table.getGameServer();
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
			} else if (readObject instanceof Card) {
				Card playedCard = (Card) readObject;
				logger.info(this.direction + " is trying to play the " + playedCard);
				table.getGameServer().notifyPlayCard(playedCard, this.direction);
			} else if (readObject instanceof Direction) {
				Direction direction = (Direction) readObject;
				// Leave the seat or sit in another place
				logger.info(this.direction + " is trying to leave his sit or sit on " + direction);
				this.table.moveOrUnsitPlayer(this, direction);
			}
		}
	}

	private void disconnect() {
		logger.info("Entered disconnect.");
		try {
			this.getSocket().close();
		} catch (IOException e) {
			logger.debug(e);
		}
		logger.info("Closed: " + this.getSocket() + ". Removing (myself) from playerSocketList");
		this.table.removeClientGameSocket(this);
	}

	private void waitForClientSetup() throws IOException, InterruptedException {
		logger.info("Sleeping for 300ms waiting for client to setup itself");
		Thread.sleep(300);
	}

	public void sendDeal(Deal deal) {
		String control = "DEAL";
		this.getSerializator().tryToSerialize(control);
		this.getSerializator().tryToSerialize(deal);
	}

	public void sendMessage(String string) {
		String control = "MESSAGE";
		this.getSerializator().tryToSerialize(control);
		this.getSerializator().tryToSerialize(string);
	}

	public void sendBoard(Board board) {
		String control = "BOARD";
		this.getSerializator().tryToSerialize(control);
		this.getSerializator().tryToSerialize(board);
	}

	public void sendDirection(Direction direction) {
		String control = "DIRECTION";
		this.getSerializator().tryToSerialize(control);
		this.getSerializator().tryToSerialize(direction);
	}

	public void sendChooserPositiveNegative(Direction direction) {
		String control = "CHOOSERPOSITIVENEGATIVE";
		this.getSerializator().tryToSerialize(control);
		this.getSerializator().tryToSerialize(direction);
	}

	public void sendPositiveOrNegative(String message) {
		String control = "POSITIVEORNEGATIVE";
		this.getSerializator().tryToSerialize(control);
		this.getSerializator().tryToSerialize(message);
	}

	public void sendChooserGameModeOrStrain(Direction chooser) {
		String control = "CHOOSERGAMEMODEORSTRAIN";
		this.getSerializator().tryToSerialize(control);
		this.getSerializator().tryToSerialize(chooser);
	}

	public void sendGameModeOrStrain(String message) {
		String control = "GAMEMODEORSTRAIN";
		this.getSerializator().tryToSerialize(control);
		this.getSerializator().tryToSerialize(message);
	}

	public void sendInitializeDeal() {
		String control = "INITIALIZEDEAL";
		this.getSerializator().tryToSerialize(control);
	}

	public void sendFinishDeal() {
		String control = "FINISHDEAL";
		this.getSerializator().tryToSerialize(control);
	}

	public void sendFinishGame() {
		String control = "FINISHGAME";
		this.getSerializator().tryToSerialize(control);
	}

	public void sendGameScoreboard(KingGameScoreboard gameScoreboard) {
		String control = "GAMESCOREBOARD";
		this.getSerializator().tryToSerialize(control);
		this.getSerializator().tryToSerialize(gameScoreboard);
	}

	public void sendInvalidRuleset() {
		String control = "INVALIDRULESET";
		this.getSerializator().tryToSerialize(control);
	}

	public void sendValidRuleset() {
		String control = "VALIDRULESET";
		this.getSerializator().tryToSerialize(control);
	}

	public void sendIsSpectator() {
		String control = "ISSPECTATOR";
		this.getSerializator().tryToSerialize(control);
	}

	public void sendIsNotSpectator() {
		String control = "ISNOTSPECTATOR";
		this.getSerializator().tryToSerialize(control);
	}

	public Socket getSocket() {
		return this.playerNetworkInformation.getSocket();
	}

	public Player getPlayer() {
		return this.playerNetworkInformation.getPlayer();
	}

	public Serializator getSerializator() {
		return this.playerNetworkInformation.getSerializator();
	}

	public Direction getDirection() {
		return direction;
	}

	public PlayerNetworkInformation getPlayerNetworkInformation() {
		return playerNetworkInformation;
	}

}