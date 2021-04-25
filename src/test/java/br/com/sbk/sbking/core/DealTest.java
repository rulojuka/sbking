package br.com.sbk.sbking.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Comparator;

import org.junit.Test;

import br.com.sbk.sbking.core.cardComparators.CardInsideHandWithSuitComparator;
import br.com.sbk.sbking.core.exceptions.DoesNotFollowSuitException;
import br.com.sbk.sbking.core.exceptions.PlayedCardInAnotherPlayersTurnException;
import br.com.sbk.sbking.core.exceptions.PlayedHeartsWhenProhibitedException;
import br.com.sbk.sbking.core.rulesets.abstractClasses.Ruleset;

public class DealTest {

    private static final int COMPLETE_TRICK_NUMBER_OF_CARDS = 4;
    private static final Direction leader = Direction.NORTH;

    @Test
    public void shouldConstructADealWithTheDealerFromTheBoard() {
        Board board = mock(Board.class);
        Ruleset ruleset = mock(Ruleset.class);
        Direction dealer = Direction.NORTH;
        when(board.getDealer()).thenReturn(dealer);
        Deal deal = new Deal(board, ruleset, leader);

        assertEquals(leader, deal.getCurrentPlayer());
    }

    @Test
    public void shouldConstructADealWithTheGivenRuleset() {
        Board board = mock(Board.class);
        Ruleset ruleset = mock(Ruleset.class);
        Direction dealer = Direction.NORTH;
        when(board.getDealer()).thenReturn(dealer);
        Deal deal = new Deal(board, ruleset, leader);

        assertEquals(ruleset, deal.getRuleset());
    }

    @Test
    public void shouldConstructADealWithNoPoints() {
        int noPoints = 0;
        Board board = mock(Board.class);
        when(board.getDealer()).thenReturn(Direction.NORTH);
        Ruleset ruleset = mock(Ruleset.class);
        Deal deal = new Deal(board, ruleset, leader);
        assertEquals(noPoints, deal.getNorthSouthPoints());
        assertEquals(noPoints, deal.getEastWestPoints());
    }

    @Test
    public void shouldConstructADealWithACurrentTrickWithTheCorrectDealer() {
        Board board = mock(Board.class);
        Ruleset ruleset = mock(Ruleset.class);
        Direction dealer = Direction.NORTH;
        Direction trickLeader = leader;
        when(board.getDealer()).thenReturn(dealer);
        Deal deal = new Deal(board, ruleset, trickLeader);
        // The correct test would be verifying if new Trick(dealer) was called
        // but Mockito doesn't do that. So, coupling the test with Trick :(
        assertEquals(trickLeader, deal.getCurrentTrick().getLeader());
    }

    @Test
    public void shouldConstructADealWithNoCompletedTricks() {
        Board board = mock(Board.class);
        when(board.getDealer()).thenReturn(Direction.NORTH);
        Ruleset ruleset = mock(Ruleset.class);

        Deal deal = new Deal(board, ruleset, leader);

        assertEquals(0, deal.getCompletedTricks());
    }

    @Test
    public void shouldConstructADealWithASortedBoard() {
        Board board = mock(Board.class);
        when(board.getDealer()).thenReturn(Direction.NORTH);
        Ruleset ruleset = mock(Ruleset.class);
        Comparator<Card> comparator = mock(Comparator.class);
        when(ruleset.getComparator()).thenReturn(comparator);

        new Deal(board, ruleset, leader);

        verify(board, atLeastOnce()).sortAllHands(comparator);
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
        Deal deal = new Deal(board, ruleset, leader);

        assertEquals(currentPlayerHand, deal.getHandOf(currentPlayer));
    }

    @Test
    public void shouldAlwaysGetCurrentTrickEvenWhenEmpty() {
        Board board = mock(Board.class);
        Ruleset ruleset = mock(Ruleset.class);
        when(board.getDealer()).thenReturn(Direction.NORTH);
        Deal deal = new Deal(board, ruleset, leader);

        deal.getCurrentTrick();
    }

    @Test(expected = PlayedCardInAnotherPlayersTurnException.class)
    public void playCardShouldThrowExceptionWhenCardIsNotFromCurrentPlayer() {
        Direction dealer = Direction.NORTH;
        Direction currentPlayer = leader;
        Hand handOfCurrentPlayer = mock(Hand.class);
        Board board = mock(Board.class);
        Ruleset ruleset = mock(Ruleset.class);
        Card card = mock(Card.class);

        when(board.getDealer()).thenReturn(dealer);
        when(board.getHandOf(currentPlayer)).thenReturn(handOfCurrentPlayer);
        when(handOfCurrentPlayer.containsCard(card)).thenReturn(false);

        Deal deal = new Deal(board, ruleset, currentPlayer);
        deal.playCard(card);
    }

    @Test(expected = PlayedHeartsWhenProhibitedException.class)
    public void playCardShouldThrowExceptionIfStartingATrickWithHeartsWhenRulesetProhibitsIt() {
        Direction dealer = Direction.NORTH;
        Direction currentPlayer = leader;
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

        Deal deal = new Deal(board, ruleset, currentPlayer);
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

        Deal deal = new Deal(board, ruleset, leader);

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

        Deal deal = new Deal(board, ruleset, leader);

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

        Deal deal = new Deal(board, ruleset, leader);

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

        Deal deal = new Deal(board, ruleset, leader);
        numberOfCardsInTheTrick = deal.getCurrentTrick().getCards().size();
        deal.playCard(card);

        verify(handOfCurrentPlayer).removeCard(card);
        assertEquals(numberOfCardsInTheTrick + 1, deal.getCurrentTrick().getCards().size());
    }

    @Test
    public void playCardShouldMoveTurnToNextPlayerIfTrickHasNotEnded() {
        Direction dealer = Direction.NORTH;
        Direction currentPlayer = leader;
        Direction nextPlayer = currentPlayer.next();
        Hand handOfCurrentPlayer = mock(Hand.class);
        Board board = mock(Board.class);
        Ruleset ruleset = mock(Ruleset.class);
        Card card = mock(Card.class);

        when(board.getDealer()).thenReturn(dealer);
        when(board.getHandOf(any(Direction.class))).thenReturn(handOfCurrentPlayer);
        when(handOfCurrentPlayer.containsCard(card)).thenReturn(true);

        Deal deal = new Deal(board, ruleset, currentPlayer);
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

        Deal deal = new Deal(board, ruleset, leader);

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

        Deal deal = new Deal(board, ruleset, leader);

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

        Deal deal = new Deal(board, ruleset, leader);

        for (int i = 0; i < COMPLETE_TRICK_NUMBER_OF_CARDS; i++) {
            assertEquals(noCompletedTricks, deal.getCompletedTricks());
            deal.playCard(card);
        }

        assertEquals(oneCompletedTricks, deal.getCompletedTricks());

    }

    @Test
    public void shouldSortAllHandsByTrumpSuit() {
        Board board = mock(Board.class);
        Ruleset ruleset = mock(Ruleset.class);
        Direction dealer = Direction.NORTH;
        when(board.getDealer()).thenReturn(dealer);
        Deal deal = new Deal(board, ruleset, leader);
        Suit anySuit = Suit.CLUBS;

        deal.sortAllHandsByTrumpSuit(anySuit);

        verify(board, atLeastOnce()).sortAllHands(any(CardInsideHandWithSuitComparator.class));

    }

    private Deal initDeal(Hand handOfCurrentPlayer) {
        Direction dealer = Direction.NORTH;
        Board board = mock(Board.class);
        Ruleset ruleset = mock(Ruleset.class);
        Card card = mock(Card.class);

        when(board.getHandOf(any(Direction.class))).thenReturn(handOfCurrentPlayer);
        when(handOfCurrentPlayer.containsCard(card)).thenReturn(true);
        when(board.getDealer()).thenReturn(dealer);

        return new Deal(board, ruleset, leader);
    }

    private Deal initDeal(Hand handOfCurrentPlayer, Board board) {
        Direction dealer = Direction.NORTH;
        Ruleset ruleset = mock(Ruleset.class);
        Card card = mock(Card.class);

        when(board.getHandOf(any(Direction.class))).thenReturn(handOfCurrentPlayer);
        when(handOfCurrentPlayer.containsCard(card)).thenReturn(true);
        when(board.getDealer()).thenReturn(dealer);

        return new Deal(board, ruleset, leader);
    }

    private Deal initDeal(Hand handOfCurrentPlayer, Ruleset ruleset) {
        Direction dealer = Direction.NORTH;
        Board board = mock(Board.class);
        Card card = mock(Card.class);

        when(board.getHandOf(any(Direction.class))).thenReturn(handOfCurrentPlayer);
        when(handOfCurrentPlayer.containsCard(card)).thenReturn(true);
        when(board.getDealer()).thenReturn(dealer);
        when(ruleset.followsSuit(any(), any(), any())).thenReturn(true);

        return new Deal(board, ruleset, leader);
    }

    private void playNTimesCard(Deal deal, int n, Hand handOfCurrentPlayer) {
        for (int i = 0; i < n; i++) {
            Card card = mock(Card.class);
            when(handOfCurrentPlayer.containsCard(card)).thenReturn(true);
            deal.playCard(card);
        }
    }

    @Test
    public void getTricksShouldReturnEmptyListIfDealHasNoTricks() {
        Hand handOfCurrentPlayer = mock(Hand.class);
        Deal deal = this.initDeal(handOfCurrentPlayer);

        assertTrue(deal.getTricks().isEmpty());
    }

    @Test
    public void undoShouldDoNothingWhenDealHasNoTricks() {
        Hand handOfCurrentPlayer = mock(Hand.class);
        Deal deal = this.initDeal(handOfCurrentPlayer);
        Direction directionThatCallsUndo = Direction.NORTH;

        deal.undo(directionThatCallsUndo);

        assertTrue(deal.getTricks().isEmpty());
    }

    @Test
    public void undoShouldDoNothingWhenFirstTrickAndCallerHasNotPlayed() {
        Hand handOfCurrentPlayer = mock(Hand.class);
        Board board = mock(Board.class);
        Deal deal = this.initDeal(handOfCurrentPlayer, board);
        Direction directionThatCallsUndo = Direction.EAST;
        playNTimesCard(deal, 1, handOfCurrentPlayer);
        Direction currentPlayerBeforeUndo = deal.getCurrentPlayer();

        deal.undo(directionThatCallsUndo);

        Direction currentPlayerAfterUndo = deal.getCurrentPlayer();
        verify(board, never()).putCardInHand(any());
        assertEquals(currentPlayerBeforeUndo, currentPlayerAfterUndo);
    }

    @Test
    public void undoShouldRemoveTrickAndSetCurrentPlayerWhenFirstPlayerAskedForUndo() {
        Hand handOfCurrentPlayer = mock(Hand.class);
        Deal deal = this.initDeal(handOfCurrentPlayer);
        Direction currentPlayer = deal.getCurrentPlayer();
        Direction directionThatCallsUndo = currentPlayer;
        playNTimesCard(deal, 1, handOfCurrentPlayer);

        deal.undo(directionThatCallsUndo);

        Direction currentPlayerAfterUndo = deal.getCurrentPlayer();
        assertTrue(deal.getCurrentTrick().getCards().isEmpty());
        assertEquals(0, deal.getTricks().size());
        assertEquals(directionThatCallsUndo, currentPlayerAfterUndo);
    }

    @Test
    public void undoShouldRemoveCardAndSetCurrentPlayerWhenLastPlayerAskedForUndo() {
        Hand handOfCurrentPlayer = mock(Hand.class);
        Ruleset ruleset = mock(Ruleset.class);
        Deal deal = this.initDeal(handOfCurrentPlayer, ruleset);
        when(ruleset.getWinner(any())).thenReturn(Direction.NORTH);
        playNTimesCard(deal, 3, handOfCurrentPlayer);
        Direction currentPlayer = deal.getCurrentPlayer();
        playNTimesCard(deal, 1, handOfCurrentPlayer);
        Direction directionThatCallsUndo = currentPlayer;

        deal.undo(directionThatCallsUndo);

        Direction currentPlayerAfterUndo = deal.getCurrentPlayer();
        assertEquals(3, deal.getCurrentTrick().getCards().size());
        assertEquals(1, deal.getTricks().size());
        assertEquals(directionThatCallsUndo, currentPlayerAfterUndo);
    }

    @Test
    public void undoShouldDoNothingWhenFirstTrickHasMoreThanOneCardAndCallerHasNotPlayedYet() {
        Hand handOfCurrentPlayer = mock(Hand.class);
        Ruleset ruleset = mock(Ruleset.class);
        Deal deal = this.initDeal(handOfCurrentPlayer, ruleset);
        playNTimesCard(deal, 2, handOfCurrentPlayer);
        Direction currentPlayer = deal.getCurrentPlayer();
        Direction directionThatCallsUndo = currentPlayer.next();

        deal.undo(directionThatCallsUndo);

        Direction currentPlayerAfterUndo = deal.getCurrentPlayer();
        assertEquals(2, deal.getCurrentTrick().getCards().size());
        assertEquals(1, deal.getTricks().size());
        assertEquals(currentPlayer, currentPlayerAfterUndo);
    }

    @Test
    public void undoShouldRemoveTwoTricksWhenLeaderOfThePreviousTrickAsksForUndoOnCurrentTrickBeforePlayCard() {
        Hand handOfCurrentPlayer = mock(Hand.class);
        Ruleset ruleset = mock(Ruleset.class);
        Deal deal = this.initDeal(handOfCurrentPlayer, ruleset);
        Direction firstPlayer = deal.getCurrentPlayer();
        Direction anyOtherPlayer = firstPlayer.next(3);
        when(ruleset.getWinner(any())).thenReturn(firstPlayer);
        int numberOfTricks = 10;
        playNTimesCard(deal, numberOfTricks * 4 - 1, handOfCurrentPlayer);
        when(ruleset.getWinner(any())).thenReturn(anyOtherPlayer);
        playNTimesCard(deal, 2, handOfCurrentPlayer);

        deal.undo(firstPlayer);

        Direction currentPlayerAfterUndo = deal.getCurrentPlayer();
        assertEquals(4, deal.getCurrentTrick().getCards().size());
        assertEquals(numberOfTricks - 1, deal.getTricks().size());
        assertEquals(firstPlayer, currentPlayerAfterUndo);
    }

    @Test
    public void undoShouldUpdateScoreWhenCallHappensImmediatelyAfterCurrentTrickIsComplete() {
        Hand handOfCurrentPlayer = mock(Hand.class);
        Ruleset ruleset = mock(Ruleset.class);
        Deal deal = this.initDeal(handOfCurrentPlayer, ruleset);
        Direction firstPlayer = deal.getCurrentPlayer();
        when(ruleset.getWinner(any())).thenReturn(firstPlayer);
        int anyNumberOfTricks = 10;
        playNTimesCard(deal, anyNumberOfTricks * 4 - 1, handOfCurrentPlayer);
        Score previousScore = deal.getScore();
        playNTimesCard(deal, 1, handOfCurrentPlayer);

        deal.undo(firstPlayer);

        Direction currentPlayerAfterUndo = deal.getCurrentPlayer();
        Score scoreAfterUndo = deal.getScore();
        assertEquals(4, deal.getCurrentTrick().getCards().size());
        assertEquals(anyNumberOfTricks - 1, deal.getTricks().size());
        assertEquals(firstPlayer, currentPlayerAfterUndo);
        assertEquals(previousScore, scoreAfterUndo);
    }

    @Test
    public void undoShouldUpdateCompletedTricksWhenFirstTrickIsCompleteAndAnyPlayerCallsUndo() {
        Hand handOfCurrentPlayer = mock(Hand.class);
        Ruleset ruleset = mock(Ruleset.class);
        Deal deal = this.initDeal(handOfCurrentPlayer, ruleset);
        Direction firstPlayer = deal.getCurrentPlayer();
        Direction anyPlayer = firstPlayer;
        when(ruleset.getWinner(any())).thenReturn(firstPlayer);
        playNTimesCard(deal, 1, handOfCurrentPlayer);

        deal.undo(anyPlayer);

        int completedTricksAfterUndo = deal.getCompletedTricks();
        Direction currentPlayerAfterUndo = deal.getCurrentPlayer();
        assertEquals(0, deal.getCurrentTrick().getCards().size());
        assertEquals(0, deal.getTricks().size());
        assertEquals(firstPlayer, currentPlayerAfterUndo);
        assertEquals(0, completedTricksAfterUndo);
    }

    @Test
    public void undoShouldUpdateCompletedTricksWhenNotFirstTrickAndTrickIsCompleteAndAnyPlayerCallsUndo() {
        Hand handOfCurrentPlayer = mock(Hand.class);
        Ruleset ruleset = mock(Ruleset.class);
        Deal deal = this.initDeal(handOfCurrentPlayer, ruleset);
        Direction firstPlayer = deal.getCurrentPlayer();
        Direction anyPlayer = firstPlayer;
        when(ruleset.getWinner(any())).thenReturn(firstPlayer);
        int anyNumberOfTricksDifferentThanOne = 13;
        playNTimesCard(deal, anyNumberOfTricksDifferentThanOne * 4, handOfCurrentPlayer);

        deal.undo(anyPlayer);

        int completedTricksAfterUndo = deal.getCompletedTricks();
        Direction currentPlayerAfterUndo = deal.getCurrentPlayer();
        assertEquals(4, deal.getCurrentTrick().getCards().size());
        assertEquals(anyNumberOfTricksDifferentThanOne - 1, deal.getTricks().size());
        assertEquals(firstPlayer, currentPlayerAfterUndo);
        assertEquals(anyNumberOfTricksDifferentThanOne - 1, completedTricksAfterUndo);
    }

    @Test
    public void undoShouldNotChangeTrickWhenUndoWasNotCalledByTheLeader() {
        Hand handOfCurrentPlayer = mock(Hand.class);
        Ruleset ruleset = mock(Ruleset.class);
        Deal deal = this.initDeal(handOfCurrentPlayer, ruleset);
        Direction firstPlayer = deal.getCurrentPlayer();
        Direction lastPlayer = firstPlayer.next();
        int anyNumberOfTricks = 2;
        when(ruleset.getWinner(any())).thenReturn(firstPlayer);
        playNTimesCard(deal, anyNumberOfTricks * 4, handOfCurrentPlayer);

        deal.undo(lastPlayer);

        int completedTricksAfterUndo = deal.getCompletedTricks();
        Direction currentPlayerAfterUndo = deal.getCurrentPlayer();
        assertEquals(1, deal.getCurrentTrick().getCards().size());
        assertEquals(anyNumberOfTricks, deal.getTricks().size());
        assertEquals(lastPlayer, currentPlayerAfterUndo);
        assertEquals(anyNumberOfTricks - 1, completedTricksAfterUndo);
    }

    @Test
    public void claimShouldFinishDealWhenAllPlayersAcceptClaim() {
        Hand handOfCurrentPlayer = mock(Hand.class);
        Ruleset ruleset = mock(Ruleset.class);
        Deal deal = this.initDeal(handOfCurrentPlayer, ruleset);
        Direction claimer = deal.getCurrentPlayer();

        deal.claim(claimer);
        this.playNTimesCard(deal, 1, handOfCurrentPlayer);
        deal.claim(claimer.next(1));
        this.playNTimesCard(deal, 1, handOfCurrentPlayer);
        deal.claim(claimer.next(2));
        this.playNTimesCard(deal, 1, handOfCurrentPlayer);
        deal.claim(claimer.next(3));

        assertEquals(true, deal.isFinished());
    }
}
