package br.com.sbk.sbking.networking.core.serialization;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class SerializatorTest {

    private ObjectInputStreamWrapper objectInputStreamWrapper;
    private ObjectOutputStreamWrapper objectOutputStreamWrapper;
    private Serializator serializator;

    @Before
    public void setup() {
        objectInputStreamWrapper = mock(ObjectInputStreamWrapper.class);
        objectOutputStreamWrapper = mock(ObjectOutputStreamWrapper.class);
        serializator = new Serializator(objectInputStreamWrapper, objectOutputStreamWrapper);
    }

    @Test
    public void finalizeShouldCallCloseOnBothWrappers() throws Exception {
        serializator.finalize();

        Mockito.verify(objectInputStreamWrapper).close();
        Mockito.verify(objectOutputStreamWrapper).close();
    }

    @Test
    public void shouldResetAndWriteObjectToObjectOutputStreamWrapperWhenSerializingWithoutExceptions()
            throws IOException {
        Object anyObject = mock(Object.class);

        serializator.tryToSerialize(anyObject);

        verify(objectOutputStreamWrapper).resetAndWriteObject(anyObject);
    }

    @Test
    public void shouldNotUseInputStreamWhenSerializing() {
        Object anyObject = mock(Object.class);

        serializator.tryToSerialize(anyObject);

        Mockito.verifyZeroInteractions(objectInputStreamWrapper);
    }

    @Test
    public void shouldReadObjectFromObjectInputStreamWrapperWhenDeserializingWithoutExceptions()
            throws ClassNotFoundException, IOException {
        Class<AnyClass> anyClass = AnyClass.class;

        serializator.tryToDeserialize(anyClass);

        verify(objectInputStreamWrapper).readObject();
    }

    @Test
    public void shouldNotUseOutputStreamWhenDeserializing() {
        Class<AnyClass> anyClass = AnyClass.class;

        serializator.tryToDeserialize(anyClass);

        Mockito.verifyZeroInteractions(objectOutputStreamWrapper);
    }

    private class AnyClass {
    }

}
