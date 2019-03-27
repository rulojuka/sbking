package br.com.sbk.sbking.networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Direction;

public class Serializator {

	ObjectInputStream objectInputStream;
	ObjectOutputStream objectOutputStream;

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
			//objectOutputStream.reset(); // Seriously, Java??
			//objectOutputStream.writeObject(object);
			objectOutputStream.writeUnshared(object);
			objectOutputStream.flush();
			System.out.println("Serialized data writen to " + this.objectOutputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Object tryToDeserialize() {
		Object deserializedObject = null;
		try {
			deserializedObject = objectInputStream.readObject();
			if (deserializedObject instanceof Deal) {
				Deal deal = (Deal) deserializedObject;
				System.out.println("Deserializing a deal. Current trick of this deal is:");
				System.out.println(deal.getCurrentTrick());
			}
		} catch (IOException i) {
			i.printStackTrace();
		} catch (ClassNotFoundException c) {
			c.printStackTrace();
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
			i.printStackTrace();
		} catch (ClassNotFoundException c) {
			c.printStackTrace();
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
			i.printStackTrace();
		} catch (ClassNotFoundException c) {
			c.printStackTrace();
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
			i.printStackTrace();
		} catch (ClassNotFoundException c) {
			c.printStackTrace();
		}

		return ret;
	}

}
