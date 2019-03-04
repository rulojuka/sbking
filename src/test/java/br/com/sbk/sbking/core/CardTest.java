package br.com.sbk.sbking.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

public class CardTest {

	private static Suit diamonds;
	private static Rank two;
	private static Card twoOfDiamonds;

	@BeforeClass
	public static void setup() {
		diamonds = Suit.DIAMONDS;
		two = Rank.TWO;
		twoOfDiamonds = new Card(diamonds, two);
	}

	@Test
	public void shouldGetSuit() {
		assertEquals(diamonds, twoOfDiamonds.getSuit());
	}

	@Test
	public void testGetRank() {
		assertEquals(two, twoOfDiamonds.getRank());
	}

	@Test
	public void testToString() {
		assertEquals("Two of Diamonds", twoOfDiamonds.toString());
	}

	@Test
	public void testCompareTo() {
		Card threeOfDiamonds = new Card(Suit.DIAMONDS, Rank.THREE);
		assertTrue(threeOfDiamonds.compareTo(twoOfDiamonds) > 0 );
	}

	@Test
	public void testPoints() {
		Card kingOfDiamonds = new Card(Suit.DIAMONDS, Rank.KING);
		assertEquals(0, twoOfDiamonds.points());
		assertEquals(3, kingOfDiamonds.points());
	}

}
