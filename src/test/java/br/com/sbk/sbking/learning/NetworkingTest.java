package br.com.sbk.sbking.learning;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

public class NetworkingTest {

	@Test
	public void shouldGetHelloWorldViaSocket() {
		HelloWorldClient client = new HelloWorldClient();
		try {
			// server.initialize();
			client.connect();
			String serverResponse = client.askHelloWorldOnServer();

			assertEquals("Hello world from server.", serverResponse);

			// server.close();
		} catch (IOException e1) {
		}
	}

	@Test
	public void shouldCapitalizeToManyClientsAtOnce() {
		CapitalizeClient capitalizeClient1 = new CapitalizeClient();
		CapitalizeClient capitalizeClient2 = new CapitalizeClient();
		String message1 = "Hello world";
		String message2 = "The brown fox jumps over the lazy dog.";
		try {
			capitalizeClient1.connect();
			capitalizeClient2.connect();
			String response1 = capitalizeClient1.capitalize(message1);
			String response2 = capitalizeClient2.capitalize(message2);
			assertEquals(message1.toUpperCase(), response1);
			assertEquals(message2.toUpperCase(), response2);
		} catch (IOException e) {
		}
	}

}
