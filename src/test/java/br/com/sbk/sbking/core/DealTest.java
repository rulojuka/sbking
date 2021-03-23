package br.com.sbk.sbking.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

import br.com.sbk.sbking.core.exceptions.DoesNotFollowSuitException;
import br.com.sbk.sbking.core.exceptions.PlayedCardInAnotherPlayersTurnException;
import br.com.sbk.sbking.core.exceptions.PlayedHeartsWhenProhibitedException;
import br.com.sbk.sbking.core.rulesets.abstractClasses.Ruleset;

public class DealTest {

    private static final int COMPLETE_TRICK_NUMBER_OF_CARDS = 4;

    @Test
    public void shouldConstructADealWithTheDealerFromTheBoard() {
        Board board = mock(Board.class);
        Ruleset ruleset = mock(Ruleset.class);
        Direction dealer = Direction.NORTH;
        when(board.getDealer()).thenReturn(dealer);
        Deal deal = new Deal(board, ruleset);

        assertEquals(dealer.getLeaderWhenDealer(), deal.getCurrentPlayer());
    }

    @Test
    public void shouldConstructADealWithTheGivenRuleset() {
        Board board = mock(Board.class);
        Ruleset ruleset = mock(Ruleset.class);
        Direction dealer = Direction.NORTH;
        when(board.getDealer()).thenReturn(dealer);
        Deal deal = new Deal(board, ruleset);

        assertEquals(ruleset, deal.getRuleset());
    }

    @Test
    public void shouldConstructADealWithNoPoints() {
        int noPoints = 0;
        Board board = mock(Board.class);
        when(board.getDealer()).thenReturn(Direction.NORTH);
        Ruleset ruleset = mock(Ruleset.class);
        Deal deal = new Deal(board, ruleset);
        assertEquals(noPoints, deal.getNorthSouthPoints());
        assertEquals(noPoints, deal.getEastWestPoints());
    }

    @Test
    public void shouldConstructADealWithACurrentTrickWithTheCorrectDealer() {
        Board board = mock(Board.class);
        Ruleset ruleset = mock(Ruleset.class);
        Direction dealer = Direction.NORTH;
        Direction trickLeader = dealer.getLeaderWhenDealer();
        when(board.getDealer()).thenReturn(dealer);
        Deal deal = new Deal(board, ruleset);
        // The correct test would be verifying if new Trick(dealer) was called
        // but Mockito doesn't do that. So, coupling the test with Trick :(
        assertEquals(trickLeader, deal.getCurrentTrick().getLeader());
    }

    @Test
    public void shouldConstructADealWithNoCompletedTricks() {
        // FIXME fail("Not implemented yet.");
    }

    @Test
    public void shouldGetHandOfACertainDirection() {
        Board board = mock(Board.class);
        Ruleset ruleset = mock(Ruleset.class);
        Direction dealer = Direction.WEST;
        Direction currentPlayer = dealer.getLeaderWhenDealer();
        Hand currentPlayerHand = mock(Hand.class);
        when(board.getHandOf(currentPlayer)).thenReturn(currentPlayerHand);
        when(board.getDealer()).thenReturn(dealer);
        Deal deal = new Deal(board, ruleset);

        assertEquals(currentPlayerHand, deal.getHandOf(currentPlayer));
    }

    @Test
    public void shouldAlwaysGetCurrentTrickEvenWhenEmpty() {
        Board board = mock(Board.class);
        Ruleset ruleset = mock(Ruleset.class);
        when(board.getDealer()).thenReturn(Direction.NORTH);
        Deal deal = new Deal(board, ruleset);

        deal.getCurrentTrick();
    }

    @Test(expected = PlayedCardInAnotherPlayersTurnException.class)
    public void playCardShouldThrowExceptionWhenCardIsNotFromCurrentPlayer() {
        Direction dealer = Direction.NORTH;
        Direction currentPlayer = dealer.getLeaderWhenDealer();
        Hand handOfCurrentPlayer = mock(Hand.class);
        Board board = mock(Board.class);
        Ruleset ruleset = mock(Ruleset.class);
        Card card = mock(Card.class);

        when(board.getDealer()).thenReturn(dealer);
        when(board.getHandOf(currentPlayer)).thenReturn(handOfCurrentPlayer);
        when(handOfCurrentPlayer.containsCard(card)).thenReturn(false);

        Deal deal = new Deal(board, ruleset);
        deal.playCard(card);
    }

    @Test(expected = PlayedHeartsWhenProhibitedException.class)
    public void playCardShouldThrowExceptionIfStartingATrickWithHeartsWhenRulesetProhibitsIt() {
        Direction dealer = Direction.NORTH;
        Direction currentPlayer = dealer.getLeaderWhenDealer();
        Hand handOfCurrentPlayer = mock(Hand.class);
        Board board = mock(Board.class);
        Ruleset ruleset = mock(Ruleset.class);
        Card card = mock(Card.class);

        when(board.getDealer()).thenReturn(dealer);
        when(board.getHandOf(currentPlayer)).thenReturn(handOfCurrentPlayer);
        when(handOfCurrentPlayer.containsCard(card)).thenReturn(true);

        when(ruleset.prohibitsHeartsUntilOnlySuitLeft()).thenReturn(true);
        when(card.isHeart()).thenReturn(true);
        when(handOfCurrentPlayer.onlyHasHearts()).thenReturn(false);

        Deal deal = new Deal(board, ruleset);
        deal.playCard(card);
    }

    @Test
    public void firstPlayedCardInATrickShouldAlwaysFollowSuit() {
        Direction currentPlayer = Direction.NORTH;
        Hand handOfCurrentPlayer = mock(Hand.class);
        Board board = mock(Board.class);
        Ruleset ruleset = mock(Ruleset.class);
        Card card = mock(Card.class);

        when(board.getDealer()).thenReturn(currentPlayer);
        when(board.getHandOf(any(Direction.class))).thenReturn(handOfCurrentPlayer);
        when(handOfCurrentPlayer.containsCard(card)).thenReturn(true);

        when(ruleset.followsSuit(any(), any(), any())).thenReturn(false);

        Deal deal = new Deal(board, ruleset);

        deal.playCard(card);

    }

    @Test(expected = DoesNotFollowSuitException.class)
    public void playCardShouldThrowExceptionIfCardDoesNotFollowSuit() {
        Direction currentPlayer = Direction.NORTH;
        Hand handOfCurrentPlayer = mock(Hand.class);
        Board board = mock(Board.class);
        Ruleset ruleset = mock(Ruleset.class);
        Card card = mock(Card.class);

        when(board.getDealer()).thenReturn(currentPlayer);
        when(board.getHandOf(any(Direction.class))).thenReturn(handOfCurrentPlayer);
        when(handOfCurrentPlayer.containsCard(card)).thenReturn(true);

        when(ruleset.followsSuit(any(), any(), any())).thenReturn(false);

        Deal deal = new Deal(board, ruleset);

        deal.playCard(card);
        deal.playCard(card);

    }

    @Test
    public void playCardShouldStartNewTrickIfCurrentTrickIsNotStartedYet() {
        Direction currentPlayer = Direction.NORTH;
        Hand handOfCurrentPlayer = mock(Hand.class);
        Board board = mock(Board.class);
        Ruleset ruleset = mock(Ruleset.class);
        Card card = mock(Card.class);

        when(board.getDealer()).thenReturn(currentPlayer);
        when(board.getHandOf(any(Direction.class))).thenReturn(handOfCurrentPlayer);
        when(handOfCurrentPlayer.containsCard(card)).thenReturn(true);

        Deal deal = new Deal(board, ruleset);

        assertTrue(deal.getCurrentTrick().isEmpty());
        deal.playCard(card);
        assertFalse(deal.getCurrentTrick().isEmpty());

    }

    @Test
    public void playCardShouldMoveCardFromHandToCurrentTrick() {
        Direction currentPlayer = Direction.NORTH;
        Hand handOfCurrentPlayer = mock(Hand.class);
        Board board = mock(Board.class);
        Ruleset ruleset = mock(Ruleset.class);
        Card card = mock(Card.class);
        int numberOfCardsInTheTrick;

        when(board.getDealer()).thenReturn(currentPlayer);
        when(board.getHandOf(any(Direction.class))).thenReturn(handOfCurrentPlayer);
        when(handOfCurrentPlayer.containsCard(card)).thenReturn(true);

        Deal deal = new Deal(board, ruleset);
        numberOfCardsInTheTrick = deal.getCurrentTrick().getCards().size();
        deal.playCard(card);

        verify(handOfCurrentPlayer).removeCard(card);
        assertEquals(numberOfCardsInTheTrick + 1, deal.getCurrentTrick().getCards().size());
    }

    @Test
    public void playCardShouldMoveTurnToNextPlayerIfTrickHasNotEnded() {
        Direction dealer = Direction.NORTH;
        Direction currentPlayer = dealer.getLeaderWhenDealer();
        Direction nextPlayer = currentPlayer.next();
        Hand handOfCurrentPlayer = mock(Hand.class);
        Board board = mock(Board.class);
        Ruleset ruleset = mock(Ruleset.class);
        Card card = mock(Card.class);

        when(board.getDealer()).thenReturn(dealer);
        when(board.getHandOf(any(Direction.class))).thenReturn(handOfCurrentPlayer);
        when(handOfCurrentPlayer.containsCard(card)).thenReturn(true);

        Deal deal = new Deal(board, ruleset);
        deal.playCard(card);

        assertEquals(nextPlayer, deal.getCurrentPlayer());
    }

    @Test
    public void playCardShouldMoveTurnToWinnerIfTrickHasEnded() {
        Direction currentPlayer = Direction.NORTH;
        Direction winner = Direction.SOUTH;
        Hand handOfCurrentPlayer = mock(Hand.class);
        Board board = mock(Board.class);
        Ruleset ruleset = mock(Ruleset.class);
        Card card = mock(Card.class);

        when(board.getDealer()).thenReturn(currentPlayer);
        when(board.getHandOf(any(Direction.class))).thenReturn(handOfCurrentPlayer);
        when(handOfCurrentPlayer.containsCard(card)).thenReturn(true);

        when(ruleset.followsSuit(any(), any(), any())).thenReturn(true);

        when(ruleset.getWinner(any())).thenReturn(winner);

        Deal deal = new Deal(board, ruleset);

        for (int i = 0; i < COMPLETE_TRICK_NUMBER_OF_CARDS; i++) {
            deal.playCard(card);
        }
        assertEquals(1, deal.getCompletedTricks());
        assertEquals(winner, deal.getCurrentPlayer());

    }

    @Test
    public void playCardShouldUpdateTheScoreboardIfTrickHasEnded() {
        Direction currentPlayer = Direction.NORTH;
        Direction winner = Direction.SOUTH;
        Hand handOfCurrentPlayer = mock(Hand.class);
        Board board = mock(Board.class);
        Ruleset ruleset = mock(Ruleset.class);
        Card card = mock(Card.class);
        int trickPoints = 1;

        when(board.getDealer()).thenReturn(currentPlayer);
        when(board.getHandOf(any(Direction.class))).thenReturn(handOfCurrentPlayer);
        when(handOfCurrentPlayer.containsCard(card)).thenReturn(true);

        when(ruleset.followsSuit(any(), any(), any())).thenReturn(true);

        when(ruleset.getWinner(any())).thenReturn(winner);
        when(ruleset.getPoints(any())).thenReturn(trickPoints);

        Deal deal = new Deal(board, ruleset);

        for (int i = 0; i < COMPLETE_TRICK_NUMBER_OF_CARDS; i++) {
            deal.playCard(card);
        }

        assertEquals(trickPoints, deal.getNorthSouthPoints());

    }

    @Test
    public void playCardShouldIncrementCompleteTricksIfTrickHasEnded() {
        Direction currentPlayer = Direction.NORTH;
        Direction winner = Direction.SOUTH;
        Hand handOfCurrentPlayer = mock(Hand.class);
        Board board = mock(Board.class);
        Ruleset ruleset = mock(Ruleset.class);
        Card card = mock(Card.class);
        int noCompletedTricks = 0;
        int oneCompletedTricks = noCompletedTricks + 1;

        when(board.getDealer()).thenReturn(currentPlayer);
        when(board.getHandOf(any(Direction.class))).thenReturn(handOfCurrentPlayer);
        when(handOfCurrentPlayer.containsCard(card)).thenReturn(true);

        when(ruleset.followsSuit(any(), any(), any())).thenReturn(true);

        when(ruleset.getWinner(any())).thenReturn(winner);

        Deal deal = new Deal(board, ruleset);

        for (int i = 0; i < COMPLETE_TRICK_NUMBER_OF_CARDS; i++) {
            assertEquals(noCompletedTricks, deal.getCompletedTricks());
            deal.playCard(card);
        }

        assertEquals(oneCompletedTricks, deal.getCompletedTricks());

    }

}
