package br.com.sbk.sbking.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import br.com.sbk.sbking.core.exceptions.TrickAlreadyFullException;

public class TrickTest {

    private static final int COMPLETE_TRICK_NUMBER_OF_CARDS = 4;

    @Test
    public void shouldBePossibleToAddCardsUpToAMaximum() {
        Trick trick = new Trick(Direction.NORTH);
        Card card = mock(Card.class);
        for (int i = 0; i < COMPLETE_TRICK_NUMBER_OF_CARDS; i++) {
            trick.addCard(card);
        }

        verifyZeroInteractions(card);
    }

    @Test(expected = TrickAlreadyFullException.class)
    public void shouldThrowExceptionWhenAddingMoreCardsThanTheMaximum() {
        Trick trick = new Trick(Direction.NORTH);
        Card card = mock(Card.class);
        for (int i = 0; i < COMPLETE_TRICK_NUMBER_OF_CARDS; i++) {
            trick.addCard(card);
        }
        trick.addCard(card);
        verifyZeroInteractions(card);
    }

    @Test
    public void shouldReturnIfItIsEmpty() {
        Trick trick = new Trick(Direction.NORTH);
        assertTrue(trick.isEmpty());
    }

    @Test
    public void shouldReturnIfItIsComplete() {
        Trick trick = new Trick(Direction.NORTH);
        Card card = mock(Card.class);
        for (int i = 0; i < COMPLETE_TRICK_NUMBER_OF_CARDS; i++) {
            trick.addCard(card);
        }
        assertTrue(trick.isComplete());
        verifyZeroInteractions(card);
    }

    @Test
    public void shouldGetLeaderFromConstruction() {
        Direction direction = Direction.SOUTH;
        Trick newTrick = new Trick(direction);
        assertEquals(direction, newTrick.getLeader());
    }

    @Test
    public void shouldGetLeadSuitFromFirstCardAdded() {
        Trick trick = new Trick(Direction.NORTH);
        Suit clubs = Suit.CLUBS;
        Card cardOfClubs = mock(Card.class);
        when(cardOfClubs.getSuit()).thenReturn(clubs);
        Card anyOtherCard = mock(Card.class);
        trick.addCard(cardOfClubs);
        trick.addCard(anyOtherCard);
        assertEquals(clubs, trick.getLeadSuit());
        verify(cardOfClubs, only()).getSuit();
    }

    @Test
    public void shouldGetWinnerWithoutTrumpSuit() {
        Direction leader = Direction.SOUTH;
        Trick trick = new Trick(leader);

        Card jackOfClubs = mock(Card.class);
        when(jackOfClubs.getRank()).thenReturn(Rank.JACK);
        when(jackOfClubs.getSuit()).thenReturn(Suit.CLUBS);

        Card queenOfHearts = mock(Card.class);
        when(queenOfHearts.getRank()).thenReturn(Rank.QUEEN);
        when(queenOfHearts.getSuit()).thenReturn(Suit.HEARTS);

        Card aceOfSpades = mock(Card.class);
        when(aceOfSpades.getRank()).thenReturn(Rank.ACE);
        when(aceOfSpades.getSuit()).thenReturn(Suit.SPADES);

        Card kingOfClubs = mock(Card.class);
        when(kingOfClubs.getRank()).thenReturn(Rank.KING);
        when(kingOfClubs.getSuit()).thenReturn(Suit.CLUBS);

        when(jackOfClubs.compareRank(kingOfClubs)).thenReturn(-1);
        when(kingOfClubs.compareRank(jackOfClubs)).thenReturn(1);

        trick.addCard(jackOfClubs);
        trick.addCard(queenOfHearts);
        trick.addCard(aceOfSpades);
        trick.addCard(kingOfClubs);

        Direction winner = Direction.EAST;
        assertEquals(winner, trick.getWinnerWithoutTrumpSuit());

        verify(jackOfClubs, atLeastOnce()).getSuit();
        verify(queenOfHearts, atLeastOnce()).getSuit();
        verify(aceOfSpades, atLeastOnce()).getSuit();
        verify(kingOfClubs, atLeastOnce()).getSuit();

        // Java's Treeset compares the Card with itself!
        // That is why if we do verifications like
        // verify(jackOfClubs, atLeastOnce()).compareRank(kingOfClubs);
        // Then the verifyNoMoreInteractions(jackOfClubs) would break.
        // We probably should not care about implementation, but...

        verifyNoMoreInteractions(queenOfHearts);
        verifyNoMoreInteractions(aceOfSpades);

        ArgumentCaptor<Card> cardsJackOfClubsComparedTo = ArgumentCaptor.forClass(Card.class);
        ArgumentCaptor<Card> cardsKingOfClubsComparedTo = ArgumentCaptor.forClass(Card.class);
        Mockito.verify(jackOfClubs).compareRank(cardsJackOfClubsComparedTo.capture());
        Mockito.verify(kingOfClubs).compareRank(cardsKingOfClubsComparedTo.capture());
        // There must be a comparison between Jack of Clubs and King of Clubs at some
        // point
        assertTrue(cardsJackOfClubsComparedTo.getAllValues().contains(kingOfClubs)
                || cardsKingOfClubsComparedTo.getAllValues().contains(jackOfClubs));
    }

    @Test
    public void shouldGetWinnerWithTrumpSuit() {
        Direction leader = Direction.SOUTH;
        Trick trick = new Trick(leader);

        Card jackOfClubs = mock(Card.class);
        when(jackOfClubs.getRank()).thenReturn(Rank.JACK);
        when(jackOfClubs.getSuit()).thenReturn(Suit.CLUBS);

        Card queenOfHearts = mock(Card.class);
        when(queenOfHearts.getRank()).thenReturn(Rank.QUEEN);
        when(queenOfHearts.getSuit()).thenReturn(Suit.HEARTS);

        Card aceOfSpades = mock(Card.class);
        when(aceOfSpades.getRank()).thenReturn(Rank.ACE);
        when(aceOfSpades.getSuit()).thenReturn(Suit.SPADES);

        Card kingOfClubs = mock(Card.class);
        when(kingOfClubs.getRank()).thenReturn(Rank.KING);
        when(kingOfClubs.getSuit()).thenReturn(Suit.CLUBS);

        when(jackOfClubs.compareRank(kingOfClubs)).thenReturn(-1);
        when(kingOfClubs.compareRank(jackOfClubs)).thenReturn(1);

        trick.addCard(jackOfClubs);
        trick.addCard(queenOfHearts);
        trick.addCard(aceOfSpades);
        trick.addCard(kingOfClubs);

        Direction winnerWithDiamondsAsTrump = Direction.EAST;
        Direction winnerWithClubsAsTrump = Direction.EAST;
        Direction winnerWithHeartsAsTrump = Direction.WEST;
        Direction winnerWithSpadesAsTrump = Direction.NORTH;
        assertEquals(winnerWithDiamondsAsTrump, trick.getWinnerWithTrumpSuit(Suit.DIAMONDS));
        assertEquals(winnerWithClubsAsTrump, trick.getWinnerWithTrumpSuit(Suit.CLUBS));
        assertEquals(winnerWithHeartsAsTrump, trick.getWinnerWithTrumpSuit(Suit.HEARTS));
        assertEquals(winnerWithSpadesAsTrump, trick.getWinnerWithTrumpSuit(Suit.SPADES));

        verify(jackOfClubs, atLeastOnce()).getSuit();
        verify(queenOfHearts, atLeastOnce()).getSuit();
        verify(aceOfSpades, atLeastOnce()).getSuit();
        verify(kingOfClubs, atLeastOnce()).getSuit();

        verify(jackOfClubs, never()).compareRank(queenOfHearts);
        verify(jackOfClubs, never()).compareRank(aceOfSpades);

        verify(queenOfHearts, never()).compareRank(jackOfClubs);
        verify(queenOfHearts, never()).compareRank(aceOfSpades);
        verify(queenOfHearts, never()).compareRank(kingOfClubs);

        verify(aceOfSpades, never()).compareRank(jackOfClubs);
        verify(aceOfSpades, never()).compareRank(queenOfHearts);
        verify(aceOfSpades, never()).compareRank(kingOfClubs);

        verify(kingOfClubs, never()).compareRank(queenOfHearts);
        verify(kingOfClubs, never()).compareRank(aceOfSpades);

        ArgumentCaptor<Card> cardsJackOfClubsComparedTo = ArgumentCaptor.forClass(Card.class);
        ArgumentCaptor<Card> cardsKingOfClubsComparedTo = ArgumentCaptor.forClass(Card.class);
        Mockito.verify(jackOfClubs, atLeast(0)).compareRank(cardsJackOfClubsComparedTo.capture());
        Mockito.verify(kingOfClubs, atLeast(0)).compareRank(cardsKingOfClubsComparedTo.capture());
        // There must be a comparison between Jack of Clubs and King of Clubs at some
        // point
        assertTrue(cardsJackOfClubsComparedTo.getAllValues().contains(kingOfClubs)
                || cardsKingOfClubsComparedTo.getAllValues().contains(jackOfClubs));

    }

    @Test
    public void getListOfCardsShouldReturnTheCorrectList() {
        Trick trick = new Trick(Direction.NORTH);
        Card jackOfClubs = mock(Card.class);
        when(jackOfClubs.getRank()).thenReturn(Rank.JACK);
        when(jackOfClubs.getSuit()).thenReturn(Suit.CLUBS);

        Card queenOfHearts = mock(Card.class);
        when(queenOfHearts.getRank()).thenReturn(Rank.QUEEN);
        when(queenOfHearts.getSuit()).thenReturn(Suit.HEARTS);

        Card aceOfSpades = mock(Card.class);
        when(aceOfSpades.getRank()).thenReturn(Rank.ACE);
        when(aceOfSpades.getSuit()).thenReturn(Suit.SPADES);

        Card kingOfClubs = mock(Card.class);
        when(kingOfClubs.getRank()).thenReturn(Rank.KING);
        when(kingOfClubs.getSuit()).thenReturn(Suit.CLUBS);

        trick.addCard(jackOfClubs);
        trick.addCard(queenOfHearts);
        trick.addCard(aceOfSpades);
        trick.addCard(kingOfClubs);

        List<Card> receivedList = trick.getCards();

        verifyZeroInteractions(jackOfClubs);
        verifyZeroInteractions(queenOfHearts);
        verifyZeroInteractions(aceOfSpades);
        verifyZeroInteractions(kingOfClubs);

        assertEquals(jackOfClubs.getRank(), receivedList.get(0).getRank());
        assertEquals(jackOfClubs.getSuit(), receivedList.get(0).getSuit());
        assertEquals(queenOfHearts.getRank(), receivedList.get(1).getRank());
        assertEquals(queenOfHearts.getSuit(), receivedList.get(1).getSuit());
        assertEquals(aceOfSpades.getRank(), receivedList.get(2).getRank());
        assertEquals(aceOfSpades.getSuit(), receivedList.get(2).getSuit());
        assertEquals(kingOfClubs.getRank(), receivedList.get(3).getRank());
        assertEquals(kingOfClubs.getSuit(), receivedList.get(3).getSuit());

    }

    @Test(expected = UnsupportedOperationException.class)
    public void getListOfCardsShouldReturnAnUnmodifiableList() {
        Trick trick = new Trick(Direction.NORTH);
        Card jackOfClubs = mock(Card.class);
        when(jackOfClubs.getRank()).thenReturn(Rank.JACK);
        when(jackOfClubs.getSuit()).thenReturn(Suit.CLUBS);
        for (int i = 0; i < COMPLETE_TRICK_NUMBER_OF_CARDS; i++) {
            trick.addCard(jackOfClubs);
        }
        List<Card> receivedList = trick.getCards();
        verifyZeroInteractions(jackOfClubs);
        receivedList.add(0, jackOfClubs);

    }

    @Test
    public void shouldGetNumberOfMen() {
        Trick trick = new Trick(Direction.NORTH);

        Card jackOfClubs = mock(Card.class);
        when(jackOfClubs.isMan()).thenReturn(true);

        Card queenOfHearts = mock(Card.class);
        when(queenOfHearts.isMan()).thenReturn(false);

        Card kingOfDiamonds = mock(Card.class);
        when(kingOfDiamonds.isMan()).thenReturn(true);

        Card aceOfSpades = mock(Card.class);
        when(aceOfSpades.isMan()).thenReturn(false);

        trick.addCard(jackOfClubs);
        trick.addCard(queenOfHearts);
        trick.addCard(kingOfDiamonds);
        trick.addCard(aceOfSpades);

        assertEquals(2, trick.getNumberOfMen());

        verify(jackOfClubs, only()).isMan();
        verify(queenOfHearts, only()).isMan();
        verify(kingOfDiamonds, only()).isMan();
        verify(aceOfSpades, only()).isMan();
    }

    @Test
    public void shouldGetNumberOfWomen() {
        Trick trick = new Trick(Direction.NORTH);

        Card jackOfClubs = mock(Card.class);
        when(jackOfClubs.isWoman()).thenReturn(false);

        Card queenOfHearts = mock(Card.class);
        when(queenOfHearts.isWoman()).thenReturn(true);

        Card kingOfDiamonds = mock(Card.class);
        when(kingOfDiamonds.isWoman()).thenReturn(false);

        Card aceOfSpades = mock(Card.class);
        when(aceOfSpades.isWoman()).thenReturn(false);

        trick.addCard(jackOfClubs);
        trick.addCard(queenOfHearts);
        trick.addCard(kingOfDiamonds);
        trick.addCard(aceOfSpades);
        assertEquals(1, trick.getNumberOfWomen());

        verify(jackOfClubs, only()).isWoman();
        verify(queenOfHearts, only()).isWoman();
        verify(kingOfDiamonds, only()).isWoman();
        verify(aceOfSpades, only()).isWoman();
    }

    @Test
    public void shouldNotBeLastTwoUntilSetLastTwo() {
        Trick trick = new Trick(Direction.NORTH);
        assertFalse(trick.isLastTwo());
        trick.setLastTwo();
        assertTrue(trick.isLastTwo());
    }

    @Test
    public void shouldReturnIfItHasTheKingOfHearts() {
        Trick trick = new Trick(Direction.NORTH);

        Card jackOfClubs = mock(Card.class);
        when(jackOfClubs.isKingOfHearts()).thenReturn(false);

        Card queenOfHearts = mock(Card.class);
        when(queenOfHearts.isKingOfHearts()).thenReturn(false);

        Card kingOfHearts = mock(Card.class);
        when(kingOfHearts.isKingOfHearts()).thenReturn(true);

        Card aceOfSpades = mock(Card.class);
        when(aceOfSpades.isKingOfHearts()).thenReturn(false);

        trick.addCard(jackOfClubs);
        trick.addCard(queenOfHearts);
        assertFalse(trick.hasKingOfHearts());
        trick.addCard(kingOfHearts);
        trick.addCard(aceOfSpades);
        assertTrue(trick.hasKingOfHearts());

        verify(jackOfClubs, atLeast(0)).isKingOfHearts();
        verify(queenOfHearts, atLeast(0)).isKingOfHearts();
        verify(kingOfHearts, atLeast(0)).isKingOfHearts();
        verify(aceOfSpades, atLeast(0)).isKingOfHearts();
        verifyNoMoreInteractions(jackOfClubs);
        verifyNoMoreInteractions(queenOfHearts);
        verifyNoMoreInteractions(kingOfHearts);
        verifyNoMoreInteractions(aceOfSpades);

    }

    @Test
    public void shouldReturnTheNumberOfHeartsCards() {
        Trick trick = new Trick(Direction.NORTH);

        Card jackOfClubs = mock(Card.class);
        when(jackOfClubs.isHeart()).thenReturn(false);

        Card queenOfHearts = mock(Card.class);
        when(queenOfHearts.isHeart()).thenReturn(true);

        Card kingOfHearts = mock(Card.class);
        when(kingOfHearts.isHeart()).thenReturn(true);

        Card aceOfSpades = mock(Card.class);
        when(aceOfSpades.isHeart()).thenReturn(false);

        trick.addCard(jackOfClubs);
        trick.addCard(queenOfHearts);
        trick.addCard(kingOfHearts);
        trick.addCard(aceOfSpades);

        assertEquals(2, trick.getNumberOfHeartsCards());

        verify(jackOfClubs, only()).isHeart();
        verify(queenOfHearts, only()).isHeart();
        verify(kingOfHearts, only()).isHeart();
        verify(aceOfSpades, only()).isHeart();
    }

    @Test
    public void getCardDirectionMapShouldReturnAMapContainingTheDirectionOfEachCard() {
        Direction leader = Direction.SOUTH;
        Trick trick = new Trick(leader);
        int numberOfCards = 4;

        List<Card> cards = new ArrayList<Card>();
        for (int i = 0; i < numberOfCards; i++) {
            Card card = mock(Card.class);
            cards.add(card);
            trick.addCard(card);
        }

        Map<Card, Direction> cardDirectionMap = trick.getCardDirectionMap();

        for (int i = 0; i < numberOfCards; i++) {
            assertEquals(leader.next(i), cardDirectionMap.get(cards.get(i)));
        }
    }

    @Test
    public void hasCardOfShouldReturnTrueWhenDirectionHasAlreadyPlayed() {
        Trick trick = this.trickWithTwoCardsAndNorthAsLeader();
        assertTrue(trick.hasCardOf(Direction.EAST));
    }

    @Test
    public void hasCardOfShouldReturnFalseWhenDirectionHasNotPlayedYet() {
        Trick trick = this.trickWithTwoCardsAndNorthAsLeader();
        assertFalse(trick.hasCardOf(Direction.SOUTH));
    }

    @Test
    public void hasCardOfShouldReturnFalseWhenTheLeaderHasNotPlayedYet() {
        Direction leader = Direction.NORTH;
        Trick trickWithNoCards = new Trick(leader);
        assertFalse(trickWithNoCards.hasCardOf(leader));
    }

    private Trick trickWithTwoCardsAndNorthAsLeader() {
        Direction leader = Direction.NORTH;
        Trick trick = new Trick(leader);

        Card firstCard = mock(Card.class);
        Card secondCard = mock(Card.class);

        trick.addCard(firstCard);
        trick.addCard(secondCard);

        return trick;
    }

    @Test
    public void removeCardsUpToDirectionShouldEliminateAllAndOnlyTheCardsUpToThatDirection() {
        Trick trick = this.trickWithTwoCardsAndNorthAsLeader();
        trick.addCard(mock(Card.class));
        trick.removeCardsFromLastUpTo(Direction.EAST);
        assertFalse(trick.hasCardOf(Direction.EAST));
        assertFalse(trick.hasCardOf(Direction.SOUTH));
        assertTrue(trick.hasCardOf(Direction.NORTH));
        assertEquals(1, trick.getCards().size());
    }

    @Test
    public void removeCardsUpToDirectionShouldNotFailWhenTrickHasNoCards() {
        Trick trick = new Trick(Direction.NORTH);
        trick.removeCardsFromLastUpTo(Direction.EAST);
    }

    @Test
    public void removeCardsUpToLeaderShouldEliminateAllCardsFromTrick() {
        Trick trick = this.trickWithTwoCardsAndNorthAsLeader();
        trick.removeCardsFromLastUpTo(Direction.NORTH);
        assertTrue(trick.isEmpty());
    }

    @Test
    public void getCardsFromLastUpToLast() {
        Direction leader = Direction.NORTH;
        Trick trick = new Trick(leader);
        Direction next = leader.next();
        Card firstCard = mock(Card.class);
        Card secondCard = mock(Card.class);
        trick.addCard(firstCard);
        trick.addCard(secondCard);

        Map<Card, Direction> cardsUpToEast = trick.getCardsFromLastUpTo(next);

        Direction directionFromFirstCard = cardsUpToEast.get(firstCard);
        Direction directionFromSecondCard = cardsUpToEast.get(secondCard);
        assertNull(directionFromFirstCard);
        assertEquals(next, directionFromSecondCard);
    }

    @Test
    public void getCardsFromLastUpToDirectionThatDidNotPlayed() {
        Direction leader = Direction.NORTH;
        Trick trick = new Trick(leader);
        Card firstCard = mock(Card.class);
        trick.addCard(firstCard);

        assertTrue(trick.getCardsFromLastUpTo(leader.next()).isEmpty());
    }

    @Test
    public void getCardsFromLastUpToLeader() {
        Direction leader = Direction.NORTH;
        Trick trick = new Trick(leader);
        Card firstCard = mock(Card.class);
        Card secondCard = mock(Card.class);
        trick.addCard(firstCard);
        trick.addCard(secondCard);

        Map<Card, Direction> cardsUpToLeader = trick.getCardsFromLastUpTo(leader);

        assertEquals(leader, cardsUpToLeader.get(firstCard));
        assertEquals(leader.next(), cardsUpToLeader.get(secondCard));
    }

}
