package br.com.sbk.sbking.networking.core.serialization;

import java.io.IOException;
import java.io.ObjectOutputStream;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ObjectOutputStreamWrapperTest {

	private ObjectOutputStream objectOutputStream;
	private ObjectOutputStreamWrapper objectOutputStreamWrapper;

	@Before
	public void setup() throws IOException {
		objectOutputStream = Mockito.mock(ObjectOutputStream.class);
		objectOutputStreamWrapper = new ObjectOutputStreamWrapper(objectOutputStream);
	}

	@Test
	public void shouldCallCloseOnObjectOutputStreamWhenClosing() throws IOException {
		objectOutputStreamWrapper.close();

		Mockito.verify(objectOutputStream).close();
	}

	@Test
	public void couldNotTestResetAndWriteObjectBecauseWriteObjectMethodIsFinal() {
		// Object anyObject = Mockito.mock(Object.class);
		// try {
		// objectOutputStreamWrapper.resetAndWriteObject(anyObject);
		// Mockito.verify(objectOutputStream).reset(); // This would work
		// Mockito.verify(objectOutputStream).writeObject(anyObject); // but Mockito
		// mock is powerless here
		// } catch (Exception e) {
		// throw new RuntimeException();
		// }
	}

}
