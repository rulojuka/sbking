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
	protected void finalize() throws Throwable {
		super.finalize();
		objectOutputStream.close();
		objectInputStream.close();
	}

	public void tryToSerialize(Object object) {
		try {

			// objectOutputStream.writeUnshared(object);
			objectOutputStream.reset();
			objectOutputStream.writeObject(object);
			// objectOutputStream.flush();
			//logger.info("Serialized data writen to " + this.objectOutputStream);
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
		} catch (ClassNotFoundException c) {
			logger.debug(c);
		}

		return deserializedObject;
	}

	public String tryToDeserializeString() {
		Object deserializedObject = null;
		String ret = null;
		try {
			deserializedObject = objectInputStream.readObject();
			ret = (String) deserializedObject;
		} catch (IOException i) {
			logger.debug(i);
		} catch (ClassNotFoundException c) {
			logger.debug(c);
		}

		return ret;
	}

	public Deal tryToDeserializeDeal() {
		Object deserializedObject = null;
		Deal ret = null;
		try {
			deserializedObject = objectInputStream.readObject();
			ret = (Deal) deserializedObject;
		} catch (IOException i) {
			logger.debug(i);
		} catch (ClassNotFoundException c) {
			logger.debug(c);
		}

		return ret;
	}

	public Direction tryToDeserializeDirection() {
		Object deserializedObject = null;
		Direction ret = null;
		try {
			deserializedObject = objectInputStream.readObject();
			ret = (Direction) deserializedObject;
		} catch (IOException i) {
			logger.debug(i);
		} catch (ClassNotFoundException c) {
			logger.debug(c);
		}

		return ret;
	}

	public GameScoreboard tryToDeserializeGameScoreboard() {
		Object deserializedObject = null;
		GameScoreboard ret = null;
		try {
			deserializedObject = objectInputStream.readObject();
			ret = (GameScoreboard) deserializedObject;
		} catch (IOException i) {
			logger.debug(i);
		} catch (ClassNotFoundException c) {
			logger.debug(c);
		}

		return ret;
	}

	public Board tryToDeserializeBoard() {
		Object deserializedObject = null;
		Board ret = null;
		try {
			deserializedObject = objectInputStream.readObject();
			ret = (Board) deserializedObject;
		} catch (IOException i) {
			logger.debug(i);
		} catch (ClassNotFoundException c) {
			logger.debug(c);
		}

		return ret;
	}

}
