package br.com.sbk.sbking.networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.gui.models.PositiveOrNegative;

public class SBKingClient implements Runnable {

	private Socket socket;

	private Serializator serializator;

	private NetworkGameMode networkGameMode;
	private Direction direction;
	private boolean allPlayersConnected;

	private Direction chooserDirection;
	private PositiveOrNegative positiveOrNegative;

	final static Logger logger = Logger.getLogger(SBKingClient.class);

	public SBKingClient() {
		try {
			socket = new Socket("localhost", 60000);
		} catch (UnknownHostException e) {
			logger.debug(e);
		} catch (IOException e) {
			logger.debug(e);
		}
		setupSerializator();
	}

	private void setupSerializator() {
		this.serializator = null;
		try {
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
			this.serializator = new Serializator(objectInputStream, objectOutputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("Finished serializator setup.");
	}

	@Override
	public void run() {
		logger.info("Entering on the infinite loop to process commands.");
		while (true) {
			processCommand();
		}
	}

	private void initializeDirection(Direction direction) {
		this.direction = direction;
	}

	private void processCommand() {
		logger.info("Waiting for a command");
		Object readObject = this.serializator.tryToDeserialize();
		String controlMessage = (String) readObject;
		logger.info("Read control: --" + controlMessage + "--");

		String MESSAGE = "MESSAGE";
		String DEAL = "DEAL";
		String DIRECTION = "DIRECTION";
		String WAIT = "WAIT";
		String CONTINUE = "CONTINUE";
		String ALLCONNECTED = "ALLCONNECTED";
		String CHOOSER = "CHOOSER";
		String POSITIVEORNEGATIVE = "POSITIVEORNEGATIVE";

		if (MESSAGE.equals(controlMessage)) {
			String string = this.serializator.tryToDeserializeString();
			logger.info("I received a message: --" + string + "--");
			if (ALLCONNECTED.equals(string)) {
				setAllPlayersConnected();
			}
		} else if (DEAL.equals(controlMessage)) {
			Deal deal = this.serializator.tryToDeserializeDeal();
			logger.info("I received a deal that contains this trick: " + deal.getCurrentTrick()
					+ " and will paint it on screen");
			paintBoardWithDeal(deal);
		} else if (DIRECTION.equals(controlMessage)) {
			Direction direction = this.serializator.tryToDeserializeDirection();
			logger.info("I received my direction: " + direction);
			this.initializeDirection(direction);
		} else if (WAIT.equals(controlMessage)) {
			logger.info("Waiting for a CONTINUE message");
			do {
				this.serializator.tryToDeserialize();
				readObject = this.serializator.tryToDeserialize();
				controlMessage = (String) readObject;
			} while (!CONTINUE.equals(controlMessage));
			logger.info("Received a CONTINUE message");

		} else if (CHOOSER.equals(controlMessage)) {
			Direction direction = this.serializator.tryToDeserializeDirection();
			logger.info("Received choosers direction: " + direction);

			setChooserDirection(direction);
		} else if (POSITIVEORNEGATIVE.equals(controlMessage)) {
			String positiveOrNegative = this.serializator.tryToDeserializeString();
			if ("POSITIVE".equals(positiveOrNegative)) {
				this.selectedPositive();
			} else if ("NEGATIVE".equals(positiveOrNegative)) {
				this.selectedNegative();
			} else {
				logger.info("Could not understand POSITIVE or NEGATIVE.");
			}
		} else {
			logger.info("Could not understand control.");
		}

		// Run these when received an order to start game from server.
		// NetworkCardPlayer networkCardPlayer = new
		// NetworkCardPlayer(this.serializator);
		// this.networkGameMode = new NetworkGameMode(networkCardPlayer,
		// this.direction);

		// FIXME
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendPositive() {
		logger.debug("Sending positive to server");
		String positive = "POSITIVE";
		this.serializator.tryToSerialize(positive);
	}

	public void sendNegative() {
		logger.debug("Sending negative to server");
		String negative = "NEGATIVE";
		this.serializator.tryToSerialize(negative);
	}

	private void setAllPlayersConnected() {
		allPlayersConnected = true;
	}

	private void paintBoardWithDeal(Deal deal) {
		networkGameMode.paintBoardElements(deal);
	}

	private void setChooserDirection(Direction direction) {
		this.chooserDirection = direction;
	}

	public boolean isEveryoneConnected() {
		return allPlayersConnected;
	}

	public Direction getDirection() {
		return this.direction;
	}

	public boolean isDirectionSet() {
		return this.direction != null;
	}

	public boolean isChooserSet() {
		return this.chooserDirection != null;
	}

	public Direction getChooser() {
		return this.chooserDirection;
	}

	public void selectedPositive() {
		this.positiveOrNegative = new PositiveOrNegative();
		this.positiveOrNegative.setPositive();
	}

	public void selectedNegative() {
		this.positiveOrNegative = new PositiveOrNegative();
		this.positiveOrNegative.setNegative();
	}

	public boolean isPositiveOrNegativeSelected() {
		if(this.positiveOrNegative==null)
			return false;
		return this.positiveOrNegative.isSelected();
	}

	public boolean isPositive() {
		return this.positiveOrNegative.isPositive();
	}

	public boolean isNegative() {
		return this.positiveOrNegative.isNegative();
	}

}
