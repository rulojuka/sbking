package br.com.sbk.sbking.core;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

public class DirectionTest {

	private static Direction north;
	private static Direction east;
	private static Direction south;

	@BeforeClass
	public static void setup() {
		north = Direction.NORTH;
		east = Direction.EAST;
		south = Direction.SOUTH;
	}

	@Test
	public void shouldHaveACompleteName() {
		assertEquals("North", north.getCompleteName());
	}

	@Test
	public void shouldGetNext() {
		assertEquals(east, north.next());
		assertEquals(south, north.next(10));
	}

}