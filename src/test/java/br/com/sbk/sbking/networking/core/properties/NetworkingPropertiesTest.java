package br.com.sbk.sbking.networking.core.properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class NetworkingPropertiesTest {

    private FileProperties fileProperties;
    private SystemProperties systemProperties;
    private NetworkingProperties networkingProperties;

    private final String host = "localhost";

    @BeforeEach
    public void setup() {
        this.fileProperties = mock(FileProperties.class);
        this.systemProperties = mock(SystemProperties.class);
        this.networkingProperties = new NetworkingProperties(fileProperties, systemProperties);
    }

    @Test
    public void shouldGetHostFromSystemPropertiesIfItReturnsNotNull() {
        Mockito.when(systemProperties.getHost()).thenReturn(host);

        assertEquals(host, this.networkingProperties.getHost());

        Mockito.verifyNoInteractions(fileProperties);
    }

    @Test
    public void shouldGetHostFromFilePropertiesIfSystemPropertiesReturnsNull() {
        Mockito.when(systemProperties.getHost()).thenReturn(null);
        Mockito.when(fileProperties.getHost()).thenReturn(host);

        assertEquals(host, this.networkingProperties.getHost());
    }

}
