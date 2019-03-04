package br.com.sbk.sbking.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

public class RankTest {

	private static Rank two;
	private static Rank three;
	private static Rank four;

	@BeforeClass
	public static void setup() {
		two = Rank.TWO;
		three = Rank.THREE;
		four = Rank.FOUR;
	}

	@Test
	public void shouldGetName() {
		assertEquals("Two", two.getName());
	}

	@Test
	public void shouldGetSymbol() {
		assertEquals("2", two.getSymbol());
	}

	@Test
	public void shouldCompareCorrectly() {
		assertTrue(three.compareTo(two) > 0);
		assertTrue(three.compareTo(four) < 0);
	}

}