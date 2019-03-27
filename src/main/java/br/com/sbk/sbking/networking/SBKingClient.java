package br.com.sbk.sbking.networking;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Direction;

public class SBKingClient {

	private Socket socket;

	private Serializator serializator;

	private NetworkGameMode networkGameMode;
	private Direction direction;

	public SBKingClient() throws Exception {

		socket = new Socket("localhost", 60000);
		setupSerializator();

	}

	public void run() {
		initializeDirection();
		NetworkCardPlayer networkCardPlayer = new NetworkCardPlayer(serializator);
		networkGameMode = new NetworkGameMode(networkCardPlayer, direction);
		System.out.println("Entering on the infinite loop to process commands.");
		while (true) {
			processCommand();
		}
	}

	private void initializeDirection() {
		processCommand();
	}

	private void setupSerializator() {
		this.serializator = null;
		ObjectOutputStream objectOutputStream;
		ObjectInputStream objectInputStream;
		try {
			objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
			objectInputStream = new ObjectInputStream(socket.getInputStream());
			this.serializator = new Serializator(objectInputStream, objectOutputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Finished serializator setup.");
	}

	private void processCommand() {
		System.out.println("ProcessCommand isEventDispatchThread: " + javax.swing.SwingUtilities.isEventDispatchThread());
		System.out.println("Waiting for a command");
		Object readObject = this.serializator.tryToDeserialize();
		String controlMessage = (String) readObject;
		System.out.println("Read control: --" + controlMessage + "--");

		String MESSAGE = "MESSAGE";
		String DEAL = "DEAL";
		String DIRECTION = "DIRECTION";
		String WAIT = "WAIT";
		String CONTINUE = "CONTINUE";

		if (MESSAGE.equals(controlMessage)) {
			String string = this.serializator.tryToDeserializeString();
			System.out.println("I received a message: --" + string + "--");
		} else if (DEAL.equals(controlMessage)) {
			Deal deal = this.serializator.tryToDeserializeDeal();
			System.out.println("I received a deal and will paint it on screen");
			networkGameMode.paintBoardElements(deal);
		} else if (DIRECTION.equals(controlMessage)) {
			Direction direction = this.serializator.tryToDeserializeDirection();
			System.out.println("I received my direction: " + direction);
			this.direction = direction;
		} else if (WAIT.equals(controlMessage)) {
			System.out.println("Waiting for a CONTINUE message");
			do {
				this.serializator.tryToDeserialize();
				readObject = this.serializator.tryToDeserialize();
				controlMessage = (String) readObject;
			} while (!CONTINUE.equals(controlMessage));

		} else {
			System.out.println("Could not understand control.");
		}
	}

}
