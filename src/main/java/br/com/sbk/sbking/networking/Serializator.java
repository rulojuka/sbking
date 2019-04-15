package br.com.sbk.sbking.networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.log4j.Logger;

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

	private Object tryToDeserializeObject() {
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

	public <T> T tryToDeserialize(Class<T> clazz) {
		Object deserializedObject = tryToDeserializeObject();
		@SuppressWarnings("unchecked")
		T deserializedType = (T) deserializedObject;
		return deserializedType;
	}

}
