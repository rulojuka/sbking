package br.com.sbk.sbking.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import br.com.sbk.sbking.core.exceptions.TrickAlreadyFullException;

public class TrickTest {

	private Trick trick;
	private static final int COMPLETE_TRICK_NUMBER_OF_CARDS = 4;

	@Before
	public void setup() {
		trick = new Trick(Direction.NORTH);
	}

	@Test
	public void shouldBePossibleToAddCardsUpToAMaximum() {
		for (int i = 0; i < COMPLETE_TRICK_NUMBER_OF_CARDS; i++) {
			trick.addCard(new Card(Suit.CLUBS, Rank.TWO));
		}
	}

	@Test(expected = TrickAlreadyFullException.class)
	public void shouldThrowExceptionWhenAddingMoreCardsThanTheMaximum() {
		for (int i = 0; i < COMPLETE_TRICK_NUMBER_OF_CARDS; i++) {
			trick.addCard(new Card(Suit.CLUBS, Rank.TWO));
		}
		trick.addCard(new Card(Suit.CLUBS, Rank.TWO));
	}

	@Test
	public void shouldReturnIfItIsEmpty() {
		assertTrue(trick.isEmpty());
	}

	@Test
	public void shouldReturnIfItIsComplete() {
		for (int i = 0; i < COMPLETE_TRICK_NUMBER_OF_CARDS; i++) {
			trick.addCard(new Card(Suit.CLUBS, Rank.TWO));
		}
		assertTrue(trick.isComplete());
	}

	@Test
	public void shouldGetLeaderFromConstruction() {
		Direction direction = Direction.SOUTH;
		Trick newTrick = new Trick(direction);
		assertEquals(direction, newTrick.getLeader());
	}

	@Test
	public void shouldGetLeadSuitFromFirstCardAdded() {
		Suit clubs = Suit.CLUBS;
		Card twoOfClubs = new Card(clubs, Rank.TWO);
		Card queenOfHearts = new Card(Suit.HEARTS, Rank.QUEEN);
		trick.addCard(twoOfClubs);
		trick.addCard(queenOfHearts);
		assertEquals(clubs, trick.getLeadSuit());
	}

	@Test
	public void shouldGetWinnerWithoutTrumpSuit() {
		Direction leader = Direction.SOUTH;
		Trick myTrick = new Trick(leader);
		Card jackOfClubs = new Card(Suit.CLUBS, Rank.JACK);
		Card queenOfHearts = new Card(Suit.HEARTS, Rank.QUEEN);
		Card aceOfSpades = new Card(Suit.SPADES, Rank.ACE);
		Card kingOfClubs = new Card(Suit.CLUBS, Rank.KING);
		myTrick.addCard(jackOfClubs);
		myTrick.addCard(queenOfHearts);
		myTrick.addCard(aceOfSpades);
		myTrick.addCard(kingOfClubs);
		Direction winner = Direction.EAST;
		assertEquals(winner, myTrick.getWinnerWithoutTrumpSuit());
	}

	@Test
	public void shouldGetWinnerWithTrumpSuit() {
		Direction leader = Direction.SOUTH;
		Trick myTrick = new Trick(leader);
		Card jackOfClubs = new Card(Suit.CLUBS, Rank.JACK);
		Card queenOfHearts = new Card(Suit.HEARTS, Rank.QUEEN);
		Card aceOfSpades = new Card(Suit.SPADES, Rank.ACE);
		Card kingOfClubs = new Card(Suit.CLUBS, Rank.KING);
		myTrick.addCard(jackOfClubs);
		myTrick.addCard(queenOfHearts);
		myTrick.addCard(aceOfSpades);
		myTrick.addCard(kingOfClubs);
		Direction winnerWithDiamondsAsTrump = Direction.EAST;
		Direction winnerWithClubsAsTrump = Direction.EAST;
		Direction winnerWithHeartsAsTrump = Direction.WEST;
		Direction winnerWithSpadesAsTrump = Direction.NORTH;
		assertEquals(winnerWithDiamondsAsTrump, myTrick.getWinnerWithTrumpSuit(Suit.DIAMONDS));
		assertEquals(winnerWithClubsAsTrump, myTrick.getWinnerWithTrumpSuit(Suit.CLUBS));
		assertEquals(winnerWithHeartsAsTrump, myTrick.getWinnerWithTrumpSuit(Suit.HEARTS));
		assertEquals(winnerWithSpadesAsTrump, myTrick.getWinnerWithTrumpSuit(Suit.SPADES));
	}

	@Test(expected = UnsupportedOperationException.class)
	public void getListOfCardsShouldReturnAnUnmodifiableList() {
		Card twoOfClubs = new Card(Suit.CLUBS, Rank.TWO);
		for (int i = 0; i < COMPLETE_TRICK_NUMBER_OF_CARDS; i++) {
			trick.addCard(twoOfClubs);
		}
		List<Card> receivedList = trick.getListOfCards();
		assertEquals(twoOfClubs.getRank(), receivedList.get(0).getRank());
		assertEquals(twoOfClubs.getSuit(), receivedList.get(0).getSuit());
		receivedList.add(0, twoOfClubs);

	}

	@Test
	public void shouldGetNumberOfMen() {
		Card jackOfClubs = new Card(Suit.CLUBS, Rank.JACK);
		Card queenOfHearts = new Card(Suit.HEARTS, Rank.QUEEN);
		Card kingOfDiamonds = new Card(Suit.DIAMONDS, Rank.KING);
		Card aceOfSpades = new Card(Suit.SPADES, Rank.ACE);
		trick.addCard(jackOfClubs);
		trick.addCard(queenOfHearts);
		trick.addCard(kingOfDiamonds);
		trick.addCard(aceOfSpades);
		assertEquals(2, trick.getNumberOfMen());
	}

	@Test
	public void shouldGetNumberOfWomen() {
		Card jackOfClubs = new Card(Suit.CLUBS, Rank.JACK);
		Card queenOfHearts = new Card(Suit.HEARTS, Rank.QUEEN);
		Card kingOfDiamonds = new Card(Suit.DIAMONDS, Rank.KING);
		Card aceOfSpades = new Card(Suit.SPADES, Rank.ACE);
		trick.addCard(jackOfClubs);
		trick.addCard(queenOfHearts);
		trick.addCard(kingOfDiamonds);
		trick.addCard(aceOfSpades);
		assertEquals(1, trick.getNumberOfWomen());
	}

	@Test
	public void shouldNotBeLastTwoUntilSetLastTwo() {
		assertFalse(trick.isLastTwo());
		trick.setLastTwo();
		assertTrue(trick.isLastTwo());
	}

	@Test
	public void shouldReturnIfItHasTheKingOfHearts() {
		Card jackOfClubs = new Card(Suit.CLUBS, Rank.JACK);
		Card queenOfHearts = new Card(Suit.HEARTS, Rank.QUEEN);
		Card kingOfHearts = new Card(Suit.HEARTS, Rank.KING);
		Card aceOfSpades = new Card(Suit.SPADES, Rank.ACE);
		trick.addCard(jackOfClubs);
		trick.addCard(queenOfHearts);
		assertFalse(trick.hasKingOfHearts());
		trick.addCard(kingOfHearts);
		trick.addCard(aceOfSpades);
		assertTrue(trick.hasKingOfHearts());
	}

	@Test
	public void shouldReturnTheNumberOfHeartsCards() {
		Card jackOfClubs = new Card(Suit.CLUBS, Rank.JACK);
		Card queenOfHearts = new Card(Suit.HEARTS, Rank.QUEEN);
		Card kingOfHearts = new Card(Suit.HEARTS, Rank.KING);
		Card aceOfSpades = new Card(Suit.SPADES, Rank.ACE);
		trick.addCard(jackOfClubs);
		trick.addCard(queenOfHearts);
		trick.addCard(kingOfHearts);
		trick.addCard(aceOfSpades);
		assertEquals(2, trick.getNumberOfHeartsCards());
	}

}
