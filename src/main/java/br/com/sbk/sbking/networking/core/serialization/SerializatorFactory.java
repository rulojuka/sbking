package br.com.sbk.sbking.networking.core.serialization;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SerializatorFactory {

	public Serializator getSerializator(Socket socket) throws IOException {
		ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
		ObjectInputStreamWrapper objectInputStreamWrapper = new ObjectInputStreamWrapper(objectInputStream);

		ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
		ObjectOutputStreamWrapper objectOutputStreamWrapper = new ObjectOutputStreamWrapper(objectOutputStream);

		return new Serializator(objectInputStreamWrapper, objectOutputStreamWrapper);
	}

}
