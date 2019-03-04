package br.com.sbk.sbking.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

public class SuitTest {

	private static Suit diamonds;
	private static Suit clubs;
	private static Suit spades;
	private static Suit hearts;

	@BeforeClass
	public static void setup() {
		diamonds = Suit.DIAMONDS;
		clubs = Suit.CLUBS;
		spades = Suit.SPADES;
		hearts = Suit.HEARTS;
	}

	@Test
	public void shouldGetName() {
		assertEquals("Diamonds", diamonds.getName());
	}

	@Test
	public void shouldGetSymbol() {
		assertEquals("d", diamonds.getSymbol());
	}

	@Test
	public void shouldCompareCorrectly() {
		assertTrue(diamonds.compareTo(clubs) < 0);
		assertTrue(clubs.compareTo(hearts) < 0);
		assertTrue(hearts.compareTo(spades) < 0);
	}

}