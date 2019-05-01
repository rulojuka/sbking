package br.com.sbk.sbking.networking.core.properties;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PropertiesConstantsTest {

	private static final String CORRECT_PORT_NAME = "port";
	private static final String CORRECT_HOST_NAME = "host";

	@Test
	public void shouldDefineThePortConstantToTheCorrectValue() {
		assertEquals(CORRECT_PORT_NAME, PropertiesConstants.PORT);
	}

	@Test
	public void shouldDefineTheHostConstantToTheCorrectValue() {
		assertEquals(CORRECT_HOST_NAME, PropertiesConstants.HOST);
	}

}
