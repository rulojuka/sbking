package br.com.sbk.sbking.networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.log4j.Logger;

public class Serializator {

	private final ObjectInputStream objectInputStream;
	private final ObjectOutputStream objectOutputStream;
	private static final Logger logger = Logger.getLogger(Serializator.class);

	public Serializator(ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream) {
		this.objectInputStream = objectInputStream;
		this.objectOutputStream = objectOutputStream;
	}

	@Override
	protected void finalize() throws Exception {
		objectOutputStream.close();
		objectInputStream.close();
	}

	public void tryToSerialize(Object object) {
		try {
			// Don't forget to reset or writeObject will probably send and already sent
			// object again.
			objectOutputStream.reset();
			objectOutputStream.writeObject(object);
		} catch (IOException e) {
			logger.error("Error trying to serialize object:" + object);
			logger.error(e);
		}
	}

	public <T> T tryToDeserialize(Class<T> clazz) {
		Object deserializedObject = tryToDeserializeObject();
		@SuppressWarnings("unchecked")
		T deserializedType = (T) deserializedObject;
		return deserializedType;
	}

	private Object tryToDeserializeObject() {
		Object deserializedObject = null;
		try {
			deserializedObject = objectInputStream.readObject();
		} catch (IOException i) {
			logger.error("Error trying to deserialize object.");
			logger.error(i);
		} catch (ClassNotFoundException c) {
			logger.error(c);
		}
		return deserializedObject;
	}
}
