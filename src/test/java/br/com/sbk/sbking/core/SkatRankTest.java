package br.com.sbk.sbking.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

public class SkatRankTest {

	private static SkatRank seven;
	private static SkatRank eight;
	private static SkatRank nine;
	private static SkatRank ten;
	private static SkatRank jack;
	private static SkatRank queen;
	private static SkatRank king;
	private static SkatRank ace;

    @BeforeClass
	public static void setup() {
		seven = SkatRank.SEVEN;
		eight = SkatRank.EIGHT;
		nine = SkatRank.NINE;
		ten = SkatRank.TEN;
		jack = SkatRank.JACK;
		queen = SkatRank.QUEEN;
		king = SkatRank.KING;
		ace = SkatRank.ACE;
	}

    @Test
	public void theSameRankShouldAlwaysBeTheSameObject() {
		SkatRank rank1 = SkatRank.ACE;
		SkatRank rank2 = SkatRank.ACE;
		assertTrue(rank1 == rank2);
	}

	@Test
	public void theSameRankShouldAlwaysBeEqual() {
		SkatRank rank1 = SkatRank.ACE;
		SkatRank rank2 = SkatRank.ACE;
		assertEquals(rank1, rank2);
	}
    
    @Test
	public void shouldGetName() {
		assertEquals("Seven", seven.getName());
		assertEquals("Eight", eight.getName());
		assertEquals("Nine", nine.getName());
		assertEquals("Ten", ten.getName());
		assertEquals("Jack", jack.getName());
		assertEquals("Queen", queen.getName());
		assertEquals("King", king.getName());
		assertEquals("Ace", ace.getName());
	}

    @Test
	public void shouldGetSymbol() {
		assertEquals("7", seven.getSymbol());
		assertEquals("8", eight.getSymbol());
		assertEquals("9", nine.getSymbol());
		assertEquals("T", ten.getSymbol());
		assertEquals("B", jack.getSymbol());
		assertEquals("D", queen.getSymbol());
		assertEquals("K", king.getSymbol());
		assertEquals("A", ace.getSymbol());
	}

}
