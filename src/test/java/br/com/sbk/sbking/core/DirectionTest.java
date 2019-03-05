package br.com.sbk.sbking.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

public class DirectionTest {

	private static Direction north;
	private static Direction east;
	private static Direction south;
	private static Direction west;

	@BeforeClass
	public static void setup() {
		north = Direction.NORTH;
		east = Direction.EAST;
		south = Direction.SOUTH;
		west = Direction.WEST;
	}

	@Test
	public void theSameDirectionShouldAlwaysBeTheSameObject() {
		Direction north1 = Direction.NORTH;
		Direction north2 = Direction.NORTH;
		assertTrue(north1 == north2);
	}

	@Test
	public void theSameDirectionShouldAlwaysBeEqual() {
		Direction north1 = Direction.NORTH;
		Direction north2 = Direction.NORTH;
		assertEquals(north1, north2);
	}

	@Test
	public void shouldKnowItsDirection() {
		assertTrue(north.isNorth());
		assertFalse(east.isNorth());

		assertTrue(east.isEast());
		assertFalse(south.isEast());

		assertTrue(south.isSouth());
		assertFalse(west.isSouth());

		assertTrue(west.isWest());
		assertFalse(north.isWest());
	}

	@Test
	public void shouldKnowItsOrientation() {
		assertTrue(north.isNorthSouth());
		assertFalse(north.isEastWest());

		assertTrue(east.isEastWest());
		assertFalse(east.isNorthSouth());
	}

	@Test
	public void shouldKnowItsImmediateNext() {
		assertTrue(north.next() == east);
		assertTrue(east.next() == south);
		assertTrue(south.next() == west);
		assertTrue(west.next() == north);
	}

	@Test
	public void shouldKnowItsNonImmediateNext() {
		assertTrue(north.next(1) == east);
		assertTrue(north.next(2) == south);
		assertTrue(north.next(3) == west);
		assertTrue(north.next(4) == north);
	}

	@Test
	public void shouldGetCompleteName() {
		assertEquals("North", north.getCompleteName());
		assertEquals("East", east.getCompleteName());
		assertEquals("South", south.getCompleteName());
		assertEquals("West", west.getCompleteName());
	}

	@Test
	public void shouldGetAbbreviation() {
		assertEquals('N', north.getAbbreviation());
		assertEquals('E', east.getAbbreviation());
		assertEquals('S', south.getAbbreviation());
		assertEquals('W', west.getAbbreviation());
	}

}