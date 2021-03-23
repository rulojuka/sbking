package br.com.sbk.sbking.networking.core.serialization;

import java.io.IOException;
import java.io.ObjectInputStream;

public class ObjectInputStreamWrapper {

    private final ObjectInputStream objectInputStream;

    public ObjectInputStreamWrapper(ObjectInputStream objectInputStream) throws IOException {
        this.objectInputStream = objectInputStream;
    }

    public void close() throws IOException {
        this.objectInputStream.close();
    }

    public Object readObject() throws ClassNotFoundException, IOException {
        return this.objectInputStream.readObject();
    }

}
