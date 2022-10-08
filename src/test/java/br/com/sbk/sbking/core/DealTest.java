package br.com.sbk.sbking.core;

import static br.com.sbk.sbking.core.GameConstants.NUMBER_OF_TRICKS_IN_A_COMPLETE_HAND;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;

import java.util.Comparator;

import org.junit.Before;
import org.junit.Test;

import br.com.sbk.sbking.core.comparators.CardInsideHandWithSuitComparator;
import br.com.sbk.sbking.core.exceptions.DoesNotFollowSuitException;
import br.com.sbk.sbking.core.exceptions.PlayedCardInAnotherPlayersTurnException;
import br.com.sbk.sbking.core.exceptions.PlayedHeartsWhenProhibitedException;
import br.com.sbk.sbking.core.rulesets.abstractrulesets.Ruleset;

public class DealTest {

    private static final int COMPLETE_TRICK_NUMBER_OF_CARDS = 4;
    private static final Direction leader = Direction.NORTH;

    private Board board;
    private Ruleset ruleset;
    private Direction dealer;
    private Hand hand;

    @Before
    public void setup() {
        int anyNumberOfCards = 13;
        this.board = mock(Board.class);
        this.ruleset = mock(Ruleset.class);
        this.hand = mock(Hand.class);
        this.dealer = Direction.NORTH;
        when(this.board.getHandOf(any(Direction.class))).thenReturn(this.hand);
        when(this.hand.size()).thenReturn(anyNumberOfCards);
    }

    @Test
    public void shouldConstructADealWithTheDealerFromTheBoard() {

        when(board.getDealer()).thenReturn(dealer);
        Deal deal = new Deal(board, ruleset, leader, null);

        assertEquals(leader, deal.getCurrentPlayer());
    }

    @Test
    public void shouldConstructADealWithTheGivenRuleset() {
        when(board.getDealer()).thenReturn(dealer);
        Deal deal = new Deal(board, ruleset, leader, null);

        assertEquals(ruleset, deal.getRuleset());
    }

    @Test
    public void shouldConstructADealWithNoPoints() {
        int noPoints = 0;
        when(board.getDealer()).thenReturn(Direction.NORTH);
        Deal deal = new Deal(board, ruleset, leader, null);
        assertEquals(noPoints, deal.getNorthSouthPoints());
        assertEquals(noPoints, deal.getEastWestPoints());
    }

    @Test
    public void shouldConstructADealWithACurrentTrickWithTheCorrectDealer() {
        Direction trickLeader = leader;
        when(board.getDealer()).thenReturn(dealer);
        Deal deal = new Deal(board, ruleset, trickLeader, null);
        // The correct test would be verifying if new Trick(dealer) was called
        // but Mockito doesn't do that. So, coupling the test with Trick :(
        assertEquals(trickLeader, deal.getCurrentTrick().getLeader());
    }

    @Test
    public void shouldConstructADealWithNoCompletedTricks() {
        when(board.getDealer()).thenReturn(Direction.NORTH);

        Deal deal = new Deal(board, ruleset, leader, null);

        assertEquals(0, deal.getCompletedTricks());
    }

    @Test
    public void shouldConstructADealWithASortedBoard() {
        when(board.getDealer()).thenReturn(Direction.NORTH);
        @SuppressWarnings("unchecked")
        Comparator<Card> comparator = mock(Comparator.class);
        when(ruleset.getComparator()).thenReturn(comparator);

        new Deal(board, ruleset, leader, null);

        verify(board, atLeastOnce()).sortAllHands(comparator);
    }

    @Test
    public void shouldGetHandOfACertainDirection() {
        dealer = Direction.WEST;
        Direction currentPlayer = dealer.getLeaderWhenDealer();
        when(board.getHandOf(currentPlayer)).thenReturn(hand);
        when(board.getDealer()).thenReturn(dealer);
        Deal deal = new Deal(board, ruleset, leader, null);

        assertEquals(hand, deal.getHandOf(currentPlayer));
    }

    @Test
    public void shouldAlwaysGetCurrentTrickEvenWhenEmpty() {
        when(board.getDealer()).thenReturn(Direction.NORTH);
        Deal deal = new Deal(board, ruleset, leader, null);

        deal.getCurrentTrick();
    }

    @Test(expected = PlayedCardInAnotherPlayersTurnException.class)
    public void playCardShouldThrowExceptionWhenCardIsNotFromCurrentPlayer() {
        Direction currentPlayer = leader;
        Hand handOfCurrentPlayer = mock(Hand.class);
        Card card = mock(Card.class);

        when(board.getDealer()).thenReturn(dealer);
        when(board.getHandOf(currentPlayer)).thenReturn(handOfCurrentPlayer);
        when(handOfCurrentPlayer.containsCard(card)).thenReturn(false);

        Deal deal = new Deal(board, ruleset, currentPlayer, null);
        deal.playCard(card);
    }

    @Test(expected = PlayedHeartsWhenProhibitedException.class)
    public void playCardShouldThrowExceptionIfStartingATrickWithHeartsWhenRulesetProhibitsIt() {
        Direction currentPlayer = leader;
        Card card = mock(Card.class);

        when(board.getDealer()).thenReturn(dealer);
        when(board.getHandOf(currentPlayer)).thenReturn(hand);
        when(hand.containsCard(card)).thenReturn(true);

        when(ruleset.prohibitsHeartsUntilOnlySuitLeft()).thenReturn(true);
        when(card.isHeart()).thenReturn(true);
        HandEvaluations handEvaluations = mock(HandEvaluations.class);
        when(hand.getHandEvaluations()).thenReturn(handEvaluations);
        when(handEvaluations.onlyHasHearts()).thenReturn(false);

        Deal deal = new Deal(board, ruleset, currentPlayer, null);
        deal.playCard(card);
    }

    @Test
    public void firstPlayedCardInATrickShouldAlwaysFollowSuit() {
        Card card = mock(Card.class);

        when(board.getDealer()).thenReturn(dealer);
        when(board.getHandOf(any(Direction.class))).thenReturn(hand);
        when(hand.containsCard(card)).thenReturn(true);

        when(ruleset.followsSuit(any(), any(), any())).thenReturn(false);

        Deal deal = new Deal(board, ruleset, leader, null);

        deal.playCard(card);

    }

    @Test(expected = DoesNotFollowSuitException.class)
    public void playCardShouldThrowExceptionIfCardDoesNotFollowSuit() {
        Card card = mock(Card.class);

        when(board.getDealer()).thenReturn(dealer);
        when(board.getHandOf(any(Direction.class))).thenReturn(hand);
        when(hand.containsCard(card)).thenReturn(true);

        when(ruleset.followsSuit(any(), any(), any())).thenReturn(false);

        Deal deal = new Deal(board, ruleset, leader, null);

        deal.playCard(card);
        deal.playCard(card);

    }

    @Test
    public void playCardShouldStartNewTrickIfCurrentTrickIsNotStartedYet() {
        Card card = mock(Card.class);

        when(board.getDealer()).thenReturn(dealer);
        when(board.getHandOf(any(Direction.class))).thenReturn(hand);
        when(hand.containsCard(card)).thenReturn(true);

        Deal deal = new Deal(board, ruleset, leader, null);

        assertTrue(deal.getCurrentTrick().isEmpty());
        deal.playCard(card);
        assertFalse(deal.getCurrentTrick().isEmpty());

    }

    @Test
    public void playCardShouldMoveCardFromHandToCurrentTrick() {
        Card card = mock(Card.class);
        int numberOfCardsInTheTrick;

        when(board.getDealer()).thenReturn(dealer);
        when(board.getHandOf(any(Direction.class))).thenReturn(hand);
        when(hand.containsCard(card)).thenReturn(true);

        Deal deal = new Deal(board, ruleset, leader, null);
        numberOfCardsInTheTrick = deal.getCurrentTrick().getCards().size();
        deal.playCard(card);

        verify(hand).removeCard(card);
        assertEquals(numberOfCardsInTheTrick + 1, deal.getCurrentTrick().getCards().size());
    }

    @Test
    public void playCardShouldMoveTurnToNextPlayerIfTrickHasNotEnded() {
        Direction currentPlayer = leader;
        Direction nextPlayer = currentPlayer.next();
        Card card = mock(Card.class);

        when(board.getDealer()).thenReturn(dealer);
        when(board.getHandOf(any(Direction.class))).thenReturn(hand);
        when(hand.containsCard(card)).thenReturn(true);

        Deal deal = new Deal(board, ruleset, currentPlayer, null);
        deal.playCard(card);

        assertEquals(nextPlayer, deal.getCurrentPlayer());
    }

    @Test
    public void playCardShouldMoveTurnToWinnerIfTrickHasEnded() {
        Direction winner = Direction.SOUTH;
        Card card = mock(Card.class);

        when(board.getDealer()).thenReturn(dealer);
        when(board.getHandOf(any(Direction.class))).thenReturn(hand);
        when(hand.containsCard(card)).thenReturn(true);

        when(ruleset.followsSuit(any(), any(), any())).thenReturn(true);

        when(ruleset.getWinner(any())).thenReturn(winner);

        Deal deal = new Deal(board, ruleset, leader, null);

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
        Card card = mock(Card.class);
        int trickPoints = 1;

        when(board.getDealer()).thenReturn(currentPlayer);
        when(board.getHandOf(any(Direction.class))).thenReturn(hand);
        when(hand.containsCard(card)).thenReturn(true);

        when(ruleset.followsSuit(any(), any(), any())).thenReturn(true);

        when(ruleset.getWinner(any())).thenReturn(winner);
        when(ruleset.getPoints(any())).thenReturn(trickPoints);

        Deal deal = new Deal(board, ruleset, leader, null);

        for (int i = 0; i < COMPLETE_TRICK_NUMBER_OF_CARDS; i++) {
            deal.playCard(card);
        }

        assertEquals(trickPoints, deal.getNorthSouthPoints());

    }

    @Test
    public void playCardShouldIncrementCompleteTricksIfTrickHasEnded() {
        Direction currentPlayer = Direction.NORTH;
        Direction winner = Direction.SOUTH;
        Card card = mock(Card.class);
        int noCompletedTricks = 0;
        int oneCompletedTricks = noCompletedTricks + 1;

        when(board.getDealer()).thenReturn(currentPlayer);
        when(board.getHandOf(any(Direction.class))).thenReturn(hand);
        when(hand.containsCard(card)).thenReturn(true);

        when(ruleset.followsSuit(any(), any(), any())).thenReturn(true);

        when(ruleset.getWinner(any())).thenReturn(winner);

        Deal deal = new Deal(board, ruleset, leader, null);

        for (int i = 0; i < COMPLETE_TRICK_NUMBER_OF_CARDS; i++) {
            assertEquals(noCompletedTricks, deal.getCompletedTricks());
            deal.playCard(card);
        }

        assertEquals(oneCompletedTricks, deal.getCompletedTricks());

    }

    @Test
    public void shouldSortAllHandsByTrumpSuit() {
        when(board.getDealer()).thenReturn(dealer);
        Deal deal = new Deal(board, ruleset, leader, null);
        Suit anySuit = Suit.CLUBS;

        deal.sortAllHandsByTrumpSuit(anySuit);

        verify(board, atLeastOnce()).sortAllHands(any(CardInsideHandWithSuitComparator.class));

    }

    private Deal initDeal(Hand handOfCurrentPlayer) {
        Card card = mock(Card.class);

        when(board.getHandOf(any(Direction.class))).thenReturn(handOfCurrentPlayer);
        when(handOfCurrentPlayer.containsCard(card)).thenReturn(true);
        when(board.getDealer()).thenReturn(dealer);

        return new Deal(board, ruleset, leader, null);
    }

    private Deal initDeal(Hand handOfCurrentPlayer, Board board) {
        Card card = mock(Card.class);

        when(board.getHandOf(any(Direction.class))).thenReturn(handOfCurrentPlayer);
        when(handOfCurrentPlayer.containsCard(card)).thenReturn(true);
        when(board.getDealer()).thenReturn(dealer);

        return new Deal(board, ruleset, leader, null);
    }

    private Deal initDeal(Hand handOfCurrentPlayer, Ruleset ruleset) {
        Card card = mock(Card.class);

        when(board.getHandOf(any(Direction.class))).thenReturn(handOfCurrentPlayer);
        when(handOfCurrentPlayer.containsCard(card)).thenReturn(true);
        when(board.getDealer()).thenReturn(dealer);
        when(ruleset.followsSuit(any(), any(), any())).thenReturn(true);

        return new Deal(board, ruleset, leader, null);
    }

    private Deal initDeal(Hand handOfCurrentPlayer, Ruleset ruleset, Boolean isPartnershipGame) {
        Card card = mock(Card.class);

        when(board.getHandOf(any(Direction.class))).thenReturn(handOfCurrentPlayer);
        when(handOfCurrentPlayer.containsCard(card)).thenReturn(true);
        when(board.getDealer()).thenReturn(dealer);
        when(ruleset.followsSuit(any(), any(), any())).thenReturn(true);

        return new Deal(board, ruleset, leader, isPartnershipGame);
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
        Deal deal = this.initDeal(hand);

        assertTrue(deal.getTricks().isEmpty());
    }

    @Test
    public void undoShouldDoNothingWhenDealHasNoTricks() {
        Deal deal = this.initDeal(hand);
        Direction directionThatCallsUndo = Direction.NORTH;

        deal.undo(directionThatCallsUndo);

        assertTrue(deal.getTricks().isEmpty());
    }

    @Test
    public void undoShouldDoNothingWhenFirstTrickAndCallerHasNotPlayed() {
        Deal deal = this.initDeal(hand, board);
        Direction directionThatCallsUndo = Direction.EAST;
        playNTimesCard(deal, 1, hand);
        Direction currentPlayerBeforeUndo = deal.getCurrentPlayer();

        deal.undo(directionThatCallsUndo);

        Direction currentPlayerAfterUndo = deal.getCurrentPlayer();
        verify(board, never()).putCardInHand(any());
        assertEquals(currentPlayerBeforeUndo, currentPlayerAfterUndo);
    }

    @Test
    public void undoShouldRemoveTrickAndSetCurrentPlayerWhenFirstPlayerAskedForUndo() {
        Deal deal = this.initDeal(hand);
        Direction currentPlayer = deal.getCurrentPlayer();
        Direction directionThatCallsUndo = currentPlayer;
        playNTimesCard(deal, 1, hand);

        deal.undo(directionThatCallsUndo);

        Direction currentPlayerAfterUndo = deal.getCurrentPlayer();
        assertTrue(deal.getCurrentTrick().getCards().isEmpty());
        assertEquals(0, deal.getTricks().size());
        assertEquals(directionThatCallsUndo, currentPlayerAfterUndo);
    }

    @Test
    public void undoShouldRemoveCardAndSetCurrentPlayerWhenLastPlayerAskedForUndo() {
        Deal deal = this.initDeal(hand, ruleset);
        when(ruleset.getWinner(any())).thenReturn(Direction.NORTH);
        playNTimesCard(deal, 3, hand);
        Direction currentPlayer = deal.getCurrentPlayer();
        playNTimesCard(deal, 1, hand);
        Direction directionThatCallsUndo = currentPlayer;

        deal.undo(directionThatCallsUndo);

        Direction currentPlayerAfterUndo = deal.getCurrentPlayer();
        assertEquals(3, deal.getCurrentTrick().getCards().size());
        assertEquals(1, deal.getTricks().size());
        assertEquals(directionThatCallsUndo, currentPlayerAfterUndo);
    }

    @Test
    public void undoShouldDoNothingWhenFirstTrickHasMoreThanOneCardAndCallerHasNotPlayedYet() {
        Deal deal = this.initDeal(hand, ruleset);
        playNTimesCard(deal, 2, hand);
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
        Deal deal = this.initDeal(hand, ruleset);
        Direction firstPlayer = deal.getCurrentPlayer();
        Direction anyOtherPlayer = firstPlayer.next(3);
        when(ruleset.getWinner(any())).thenReturn(firstPlayer);
        int numberOfTricks = 10;
        playNTimesCard(deal, numberOfTricks * 4 - 1, hand);
        when(ruleset.getWinner(any())).thenReturn(anyOtherPlayer);
        playNTimesCard(deal, 2, hand);

        deal.undo(firstPlayer);

        Direction currentPlayerAfterUndo = deal.getCurrentPlayer();
        assertEquals(4, deal.getCurrentTrick().getCards().size());
        assertEquals(numberOfTricks - 1, deal.getTricks().size());
        assertEquals(firstPlayer, currentPlayerAfterUndo);
    }

    @Test
    public void undoShouldUpdateScoreWhenCallHappensImmediatelyAfterCurrentTrickIsComplete() {
        Deal deal = this.initDeal(hand, ruleset);
        Direction firstPlayer = deal.getCurrentPlayer();
        when(ruleset.getWinner(any())).thenReturn(firstPlayer);
        int anyNumberOfTricks = 10;
        playNTimesCard(deal, anyNumberOfTricks * 4 - 1, hand);
        Score previousScore = deal.getScore();
        playNTimesCard(deal, 1, hand);

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
        Deal deal = this.initDeal(hand, ruleset);
        Direction firstPlayer = deal.getCurrentPlayer();
        Direction anyPlayer = firstPlayer;
        when(ruleset.getWinner(any())).thenReturn(firstPlayer);
        playNTimesCard(deal, 1, hand);

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
        Deal deal = this.initDeal(hand, ruleset);
        Direction firstPlayer = deal.getCurrentPlayer();
        Direction anyPlayer = firstPlayer;
        when(ruleset.getWinner(any())).thenReturn(firstPlayer);
        int anyNumberOfTricksDifferentThanOne = 13;
        playNTimesCard(deal, anyNumberOfTricksDifferentThanOne * 4, hand);

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
        Deal deal = this.initDeal(hand, ruleset);
        Direction firstPlayer = deal.getCurrentPlayer();
        Direction lastPlayer = firstPlayer.next();
        int anyNumberOfTricks = 2;
        when(ruleset.getWinner(any())).thenReturn(firstPlayer);
        playNTimesCard(deal, anyNumberOfTricks * 4, hand);

        deal.undo(lastPlayer);

        int completedTricksAfterUndo = deal.getCompletedTricks();
        Direction currentPlayerAfterUndo = deal.getCurrentPlayer();
        assertEquals(1, deal.getCurrentTrick().getCards().size());
        assertEquals(anyNumberOfTricks, deal.getTricks().size());
        assertEquals(lastPlayer, currentPlayerAfterUndo);
        assertEquals(anyNumberOfTricks - 1, completedTricksAfterUndo);
    }

    @Test
    public void giveBackAllCardsToHandsShouldReturnCardsToHands() {
        Deal deal = this.initDeal(hand, ruleset);
        Direction firstPlayer = deal.getCurrentPlayer();
        when(ruleset.getWinner(any())).thenReturn(firstPlayer);
        int numberOfCardsGiveBackToHands = COMPLETE_TRICK_NUMBER_OF_CARDS * NUMBER_OF_TRICKS_IN_A_COMPLETE_HAND;
        playNTimesCard(deal, numberOfCardsGiveBackToHands, hand);

        deal.giveBackAllCardsToHands();

        verify(hand, times(numberOfCardsGiveBackToHands)).addCard(any());
        assertTrue(deal.getCurrentTrick().isEmpty());
    }

    @Test
    public void claimShouldSetClaimer() {
        Deal deal = this.initDeal(hand, ruleset);
        Direction claimer = deal.getCurrentPlayer();

        deal.claim(claimer);

        assertEquals(claimer, deal.getClaimer());
    }

    @Test
    public void acceptClaimShouldAddPlayerToAcceptedClaimMap() {
        Deal deal = this.initDeal(hand, ruleset, true);
        Direction claimer = deal.getCurrentPlayer();

        deal.claim(claimer);
        deal.acceptClaim(claimer.next());

        assertEquals(true, deal.getAcceptedClaimMap().get(claimer.next()));
    }

    @Test
    public void acceptClaimShouldFinishDealIfAllPlayersAcceptedClaimAndItIsPartnershipGame() {
        Deal deal = this.initDeal(hand, ruleset, true);
        Direction claimer = deal.getCurrentPlayer();
        int totalPoints = 20;
        when(ruleset.getTotalPoints()).thenReturn(totalPoints);

        deal.claim(claimer);
        deal.acceptClaim(claimer.next());
        deal.acceptClaim(claimer.next(3));

        assertEquals(totalPoints, deal.getScore().getNorthSouthPoints());
    }

    @Test
    public void acceptClaimShouldFinishDealIfAllPlayersAcceptedClaimAndItIsNotPartnershipGame() {
        Deal deal = this.initDeal(hand, ruleset, false);
        Direction claimer = deal.getCurrentPlayer();
        int totalPoints = 20;
        when(ruleset.getTotalPoints()).thenReturn(totalPoints);

        deal.claim(claimer);
        deal.acceptClaim(claimer.next());
        deal.acceptClaim(claimer.next(2));
        deal.acceptClaim(claimer.next(3));

        assertEquals(totalPoints, deal.getScore().getNorthSouthPoints());
    }

    @Test
    public void rejectClaimShouldRemoveClaimerAndResetAcceptedClaims() {
        Deal deal = this.initDeal(hand, ruleset, true);
        Direction claimer = deal.getCurrentPlayer();

        deal.claim(claimer);
        this.playNTimesCard(deal, 1, hand);
        deal.acceptClaim(claimer.next());
        deal.rejectClaim();

        for (Direction direction : Direction.values()) {
            assertEquals(false, deal.getAcceptedClaimMap().get(direction));
        }
        assertEquals(null, deal.getClaimer());
    }
}
