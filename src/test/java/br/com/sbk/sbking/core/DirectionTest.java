package br.com.sbk.sbking.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

public class DirectionTest {

	private static final String NORTH_COMPLETE_NAME = "North";
	private static final String EAST_COMPLETE_NAME = "East";
	private static final String SOUTH_COMPLETE_NAME = "South";
	private static final String WEST_COMPLETE_NAME = "West";

	private static final char NORTH_ABBREVIATION = 'N';
	private static final char EAST_ABBREVIATION = 'E';
	private static final char SOUTH_ABBREVIATION = 'S';
	private static final char WEST_ABBREVIATION = 'W';

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
		assertTrue(south.isNorthSouth());
		assertFalse(south.isEastWest());

		assertTrue(east.isEastWest());
		assertFalse(east.isNorthSouth());
		assertTrue(west.isEastWest());
		assertFalse(west.isNorthSouth());
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
	public void shouldGetPositiveOrNegativeChooserWhenDealer() {
		assertEquals(east,north.getPositiveOrNegativeChooserWhenDealer());
		assertEquals(south,east.getPositiveOrNegativeChooserWhenDealer());
		assertEquals(west,south.getPositiveOrNegativeChooserWhenDealer());
		assertEquals(north,west.getPositiveOrNegativeChooserWhenDealer());
	}
	
	@Test
	public void shouldGetGameModeOrStrainChooserWhenDealer() {
		assertEquals(west,north.getGameModeOrStrainChooserWhenDealer());
		assertEquals(north,east.getGameModeOrStrainChooserWhenDealer());
		assertEquals(east,south.getGameModeOrStrainChooserWhenDealer());
		assertEquals(south,west.getGameModeOrStrainChooserWhenDealer());
	}

	@Test
	public void shouldGetCompleteName() {
		assertEquals(NORTH_COMPLETE_NAME, north.getCompleteName());
		assertEquals(EAST_COMPLETE_NAME, east.getCompleteName());
		assertEquals(SOUTH_COMPLETE_NAME, south.getCompleteName());
		assertEquals(WEST_COMPLETE_NAME, west.getCompleteName());
	}

	@Test
	public void shouldGetAbbreviation() {
		assertEquals(NORTH_ABBREVIATION, north.getAbbreviation());
		assertEquals(EAST_ABBREVIATION, east.getAbbreviation());
		assertEquals(SOUTH_ABBREVIATION, south.getAbbreviation());
		assertEquals(WEST_ABBREVIATION, west.getAbbreviation());
	}

}