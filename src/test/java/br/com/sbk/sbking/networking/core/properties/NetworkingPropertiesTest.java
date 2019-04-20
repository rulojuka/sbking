package br.com.sbk.sbking.networking.core.properties;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import br.com.sbk.sbking.networking.core.properties.FileProperties;
import br.com.sbk.sbking.networking.core.properties.NetworkingProperties;
import br.com.sbk.sbking.networking.core.properties.SystemProperties;

public class NetworkingPropertiesTest {

	private FileProperties fileProperties;
	private SystemProperties systemProperties;
	private NetworkingProperties networkingProperties;

	private final String portString = "60000";
	private final int portNumber = 60000;
	private final String host = "localhost";

	@Before
	public void setup() {
		this.fileProperties = mock(FileProperties.class);
		this.systemProperties = mock(SystemProperties.class);
		this.networkingProperties = new NetworkingProperties(fileProperties, systemProperties);
	}

	@Test
	public void shouldGetHostFromSystemPropertiesIfItReturnsNotNull() {
		Mockito.when(systemProperties.getHost()).thenReturn(host);

		assertEquals(host, this.networkingProperties.getHost());

		Mockito.verifyZeroInteractions(fileProperties);
	}

	@Test
	public void shouldGetHostFromFilePropertiesIfSystemPropertiesReturnsNull() {
		Mockito.when(systemProperties.getHost()).thenReturn(null);
		Mockito.when(fileProperties.getHost()).thenReturn(host);

		assertEquals(host, this.networkingProperties.getHost());
	}

	@Test
	public void shouldGetPortFromSystemPropertiesIfItReturnsNotNull() {
		Mockito.when(systemProperties.getPort()).thenReturn(portString);

		assertEquals(portNumber, this.networkingProperties.getPort());

		Mockito.verifyZeroInteractions(fileProperties);
	}

	@Test
	public void shouldGetPortFromFilePropertiesIfSystemPropertiesReturnsNull() {
		Mockito.when(systemProperties.getPort()).thenReturn(null);
		Mockito.when(fileProperties.getPort()).thenReturn(portString);

		assertEquals(portNumber, this.networkingProperties.getPort());
	}

}
