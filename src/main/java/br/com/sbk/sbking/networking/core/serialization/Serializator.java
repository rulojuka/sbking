package br.com.sbk.sbking.networking.core.serialization;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import java.io.IOException;

public class Serializator {

    private final ObjectInputStreamWrapper objectInputStreamWrapper;
    private final ObjectOutputStreamWrapper objectOutputStreamWrapper;

    public Serializator(ObjectInputStreamWrapper objectInputStreamWrapper,
            ObjectOutputStreamWrapper objectOutputStreamWrapper) {
        this.objectInputStreamWrapper = objectInputStreamWrapper;
        this.objectOutputStreamWrapper = objectOutputStreamWrapper;
    }

    public void tryToSerialize(Object object) {
        try {
            this.objectOutputStreamWrapper.resetAndWriteObject(object);
        } catch (IOException e) {
            LOGGER.error("Error trying to serialize object:" + object);
            LOGGER.error(e);
            this.close();
        }
    }

    public void close() {
        try {
            this.finalize();
        } catch (Exception e) {
            LOGGER.error("Error trying to finalize serializator:");
            LOGGER.error(e);
        }
    }

    @Override
    protected void finalize() throws Exception {
        try {
            this.objectOutputStreamWrapper.close();
            this.objectInputStreamWrapper.close();
        } catch (Exception e) {
            LOGGER.error("Error trying to finalize serializator");
            LOGGER.error(e);
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
            LOGGER.error("Error trying to deserialize object.");
            LOGGER.error(e);
            LOGGER.error("Returning new DisconnectedObject");
            return new DisconnectedObject();
        }
        return deserializedObject;
    }
}
