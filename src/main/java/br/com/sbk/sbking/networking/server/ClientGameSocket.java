package br.com.sbk.sbking.networking.server;

import java.io.IOException;
import java.net.Socket;

import org.apache.log4j.Logger;

import br.com.sbk.sbking.core.Board;
import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.gui.models.GameScoreboard;
import br.com.sbk.sbking.networking.core.serialization.Serializator;

public abstract class ClientGameSocket implements Runnable {
	protected Socket socket;
	protected Serializator serializator;
	protected GameServer gameServer;
	final static Logger logger = Logger.getLogger(SpectatorGameSocket.class);

	public ClientGameSocket(Serializator serializator, Socket socket, GameServer gameServer) {
		this.serializator = serializator;
		this.socket = socket;
		this.gameServer = gameServer;
	}

	public abstract void run();

  public void sendDeal(Deal deal) {
		String control = "DEAL";
		this.serializator.tryToSerialize(control);
		this.serializator.tryToSerialize(deal);
  }
  
  public void sendMessage(String string) {
		String control = "MESSAGE";
		this.serializator.tryToSerialize(control);
		this.serializator.tryToSerialize(string);
	}

	public void sendBoard(Board board) {
		String control = "BOARD";
		this.serializator.tryToSerialize(control);
		this.serializator.tryToSerialize(board);
	}

	public void sendDirection(Direction direction) {
		String control = "DIRECTION";
		this.serializator.tryToSerialize(control);
		this.serializator.tryToSerialize(direction);
	}

	public void sendChooserPositiveNegative(Direction direction) {
		String control = "CHOOSERPOSITIVENEGATIVE";
		this.serializator.tryToSerialize(control);
		this.serializator.tryToSerialize(direction);
	}

	public void sendPositiveOrNegative(String message) {
		String control = "POSITIVEORNEGATIVE";
		this.serializator.tryToSerialize(control);
		this.serializator.tryToSerialize(message);
	}

	public void sendChooserGameModeOrStrain(Direction chooser) {
		String control = "CHOOSERGAMEMODEORSTRAIN";
		this.serializator.tryToSerialize(control);
		this.serializator.tryToSerialize(chooser);
	}

	public void sendGameModeOrStrain(String message) {
		String control = "GAMEMODEORSTRAIN";
		this.serializator.tryToSerialize(control);
		this.serializator.tryToSerialize(message);
	}

	public void sendInitializeDeal() {
		String control = "INITIALIZEDEAL";
		this.serializator.tryToSerialize(control);
	}

	public void sendFinishDeal() {
		String control = "FINISHDEAL";
		this.serializator.tryToSerialize(control);
	}

	public void sendFinishGame() {
		String control = "FINISHGAME";
		this.serializator.tryToSerialize(control);
	}

	public void sendGameScoreboard(GameScoreboard gameScoreboard) {
		String control = "GAMESCOREBOARD";
		this.serializator.tryToSerialize(control);
		this.serializator.tryToSerialize(gameScoreboard);
	}

	public void sendInvalidRuleset() {
		String control = "INVALIDRULESET";
		this.serializator.tryToSerialize(control);
	}

	public void sendValidRuleset() {
		String control = "VALIDRULESET";
		this.serializator.tryToSerialize(control);
	}

}