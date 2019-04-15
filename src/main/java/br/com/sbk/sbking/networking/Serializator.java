package br.com.sbk.sbking.networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.log4j.Logger;

import br.com.sbk.sbking.core.Board;
import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.gui.models.GameScoreboard;

public class Serializator {

	ObjectInputStream objectInputStream;
	ObjectOutputStream objectOutputStream;
	final static Logger logger = Logger.getLogger(Serializator.class);

	public Serializator(ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream) {
		this.objectInputStream = objectInputStream;
		this.objectOutputStream = objectOutputStream;
	}

	@Override
	protected void finalize() {
		try {
			objectOutputStream.close();
			objectInputStream.close();
		} catch (IOException e) {
			logger.error("Error closing objectOutputStream or objectInputStream");
			logger.debug(e);
		}
	}

	public void tryToSerialize(Object object) {
		try {
			// objectOutputStream.writeUnshared(object);
			objectOutputStream.reset();
			objectOutputStream.writeObject(object);
			// objectOutputStream.flush();
			// logger.info("Serialized data writen to " + this.objectOutputStream);
		} catch (IOException e) {
			logger.debug(e);
		}
	}

	public Object tryToDeserialize() {
		Object deserializedObject = null;
		try {
			deserializedObject = objectInputStream.readObject();
		} catch (IOException i) {
			logger.debug("EOFException!?!?!");
			logger.debug(i);
			this.finalize();
		} catch (ClassNotFoundException c) {
			logger.debug(c);
		}

		return deserializedObject;
	}

	public String tryToDeserializeString() {
		Object deserializedObject = tryToDeserialize();
		String deserializedString = (String) deserializedObject;
		return deserializedString;
	}

	public Deal tryToDeserializeDeal() {
		Object deserializedObject = tryToDeserialize();
		Deal deserializedDeal = (Deal) deserializedObject;
		return deserializedDeal;
	}

	public Direction tryToDeserializeDirection() {
		Object deserializedObject = tryToDeserialize();
		Direction deserializedDirection = (Direction) deserializedObject;
		return deserializedDirection;
	}

	public GameScoreboard tryToDeserializeGameScoreboard() {
		Object deserializedObject = tryToDeserialize();
		GameScoreboard deserializedGameScoreboard = (GameScoreboard) deserializedObject;
		return deserializedGameScoreboard;
	}

	public Board tryToDeserializeBoard() {
		Object deserializedObject = tryToDeserialize();
		Board deserializedBoard = (Board) deserializedObject;
		return deserializedBoard;
	}

}
