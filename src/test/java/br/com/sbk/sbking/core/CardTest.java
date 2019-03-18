package br.com.sbk.sbking.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

public class CardTest {

	private static Suit diamonds;
	private static Rank two;
	private static Card twoOfDiamonds;
	private static Suit clubs;
	private static Rank three;
	private static Card threeOfClubs;
	private static Card jackOfClubs;
	private static Card queenOfHearts;
	private static Card kingOfDiamonds;
	private static Card queenOfDiamonds;
	private static Card kingOfHearts;

	@BeforeClass
	public static void setup() {
		diamonds = Suit.DIAMONDS;
		two = Rank.TWO;
		twoOfDiamonds = new Card(diamonds, two);
		clubs = Suit.CLUBS;
		three = Rank.THREE;
		threeOfClubs = new Card(clubs, three);
		jackOfClubs = new Card(Suit.CLUBS, Rank.JACK);
		queenOfHearts = new Card(Suit.HEARTS, Rank.QUEEN);
		queenOfDiamonds = new Card(Suit.DIAMONDS, Rank.QUEEN);
		kingOfDiamonds = new Card(Suit.DIAMONDS, Rank.KING);
		kingOfHearts = new Card(Suit.HEARTS, Rank.KING);
	}

	@Test
	public void shouldConstructACardWithASuitAndARank() {
		twoOfDiamonds = new Card(diamonds, two);
	}

	@Test
	public void shouldGetSuit() {
		assertEquals(diamonds, twoOfDiamonds.getSuit());
	}

	@Test
	public void shouldGetRank() {
		assertEquals(two, twoOfDiamonds.getRank());
	}

	@Test
	public void shouldBeRankComparable() {
		int rankComparison = twoOfDiamonds.getRank().compareTo(threeOfClubs.getRank());
		int cardComparison = twoOfDiamonds.compareRank(threeOfClubs);
		assertEquals(rankComparison, cardComparison);
	}

	@Test
	public void shouldBeSuitComparable() {
		int suitComparison = twoOfDiamonds.getSuit().compareTo(threeOfClubs.getSuit());
		int cardComparison = twoOfDiamonds.compareSuit(threeOfClubs);
		assertEquals(suitComparison, cardComparison);
	}

	@Test
	public void shouldReturnIfItIsAManOrNot() {
		assertTrue(jackOfClubs.isMan());
		assertTrue(kingOfDiamonds.isMan());
		assertFalse(twoOfDiamonds.isMan());

	}

	@Test
	public void shouldReturnIfItIsAWomanOrNot() {
		assertFalse(jackOfClubs.isWoman());
		assertTrue(queenOfDiamonds.isWoman());
	}

	@Test
	public void shouldReturnIfItIsAHeartOrNot() {
		assertFalse(jackOfClubs.isHeart());
		assertTrue(queenOfHearts.isHeart());
	}

	@Test
	public void shouldReturnIfItIsTheKingOfHeartsOrNot() {
		assertFalse(jackOfClubs.isHeart());
		assertTrue(kingOfHearts.isHeart());
	}

}
