package br.com.sbk.sbking.networking.core.serialization;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Serializator {

	private final ObjectInputStreamWrapper objectInputStreamWrapper;
	private final ObjectOutputStreamWrapper objectOutputStreamWrapper;
	private static final Logger logger = LogManager.getLogger(Serializator.class);

	public Serializator(ObjectInputStreamWrapper objectInputStreamWrapper,
			ObjectOutputStreamWrapper objectOutputStreamWrapper) {
		this.objectInputStreamWrapper = objectInputStreamWrapper;
		this.objectOutputStreamWrapper = objectOutputStreamWrapper;
	}

	@Override
	protected void finalize() throws Exception {
		this.objectOutputStreamWrapper.close();
		this.objectInputStreamWrapper.close();
	}

	public void tryToSerialize(Object object) {
		try {
			this.objectOutputStreamWrapper.resetAndWriteObject(object);
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
			deserializedObject = this.objectInputStreamWrapper.readObject();
		} catch (Exception e) {
			logger.error("Error trying to deserialize object.");
			logger.error(e);
			e.printStackTrace();
			return new DisconnectedObject();
		}
		return deserializedObject;
	}
}
