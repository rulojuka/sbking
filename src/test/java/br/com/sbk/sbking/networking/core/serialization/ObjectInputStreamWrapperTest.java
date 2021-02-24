package br.com.sbk.sbking.networking.core.serialization;

import java.io.IOException;
import java.io.ObjectInputStream;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ObjectInputStreamWrapperTest {

	private ObjectInputStream objectInputStream;
	private ObjectInputStreamWrapper objectInputStreamWrapper;

	@Before
	public void setup() throws IOException {
		objectInputStream = Mockito.mock(ObjectInputStream.class);
		objectInputStreamWrapper = new ObjectInputStreamWrapper(objectInputStream);
	}

	@Test
	public void shouldCallCloseOnObjectInputStreamWhenClosing() throws IOException {
		objectInputStreamWrapper.close();

		Mockito.verify(objectInputStream).close();
	}

	@Test
	public void couldNotTestReadObjectBecauseMethodIsFinal() {
		// try {
		// objectInputStreamWrapper.readObject(); // Mockito mock is powerless here
		// Mockito.verify(objectInputStream).readObject();
		// } catch (Exception e) {
		// throw new RuntimeException();
		// }
	}

}
