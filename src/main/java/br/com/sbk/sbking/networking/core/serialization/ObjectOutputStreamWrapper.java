package br.com.sbk.sbking.networking.core.serialization;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class ObjectOutputStreamWrapper {

	private final ObjectOutputStream objectOutputStream;

	public ObjectOutputStreamWrapper(ObjectOutputStream objectInputStream) throws IOException {
		this.objectOutputStream = objectInputStream;
	}

	public void close() throws IOException {
		this.objectOutputStream.close();
	}

	public void resetAndWriteObject(Object obj) throws IOException {
		this.objectOutputStream.reset();
		this.objectOutputStream.writeObject(obj);
	}

}
