package br.com.sbk.sbking.core;

import static br.com.sbk.sbking.core.GameConstants.NUMBER_OF_TRICKS_IN_A_COMPLETE_HAND;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.sbk.sbking.core.exceptions.DoesNotFollowSuitException;
import br.com.sbk.sbking.core.exceptions.PlayedCardInAnotherPlayersTurnException;
import br.com.sbk.sbking.core.exceptions.PlayedHeartsWhenProhibitedException;
import br.com.sbk.sbking.core.rulesets.abstractClasses.Ruleset;

@SuppressWarnings("serial")
public class Deal implements Serializable {

    private Board board;
    private int completedTricks;
    private int startingNumberOfCardsInTheHand;
    private Direction currentPlayer;
    private Score score;
    private Map<Direction, Player> players = new HashMap<Direction, Player>();

    private Ruleset ruleset;

    private List<Trick> tricks;
    private Trick currentTrick;
    private Direction dummy;

    public Deal(Board board, Ruleset ruleset) {
        this.board = board;
        this.ruleset = ruleset;
        this.currentPlayer = this.board.getDealer().getLeaderWhenDealer();
        this.score = new Score(ruleset);
        this.completedTricks = 0;
        this.startingNumberOfCardsInTheHand = NUMBER_OF_TRICKS_IN_A_COMPLETE_HAND;
        this.tricks = new ArrayList<Trick>();
        this.players = new HashMap<Direction, Player>();
    }

    public Player getPlayerOf(Direction direction) {
        return this.players.get(direction);
    }

    public void setPlayerOf(Direction direction, Player player) {
        this.players.put(direction, player);
    }

    public void unsetPlayerOf(Direction direction) {
        this.players.remove(direction);
    }

    public Hand getHandOf(Direction direction) {
        return this.board.getHandOf(direction);
    }

    public Trick getCurrentTrick() {
        if (this.currentTrick == null) {
            return new Trick(currentPlayer);
        } else {
            return this.currentTrick;
        }
    }

    public Direction getCurrentPlayer() {
        return this.currentPlayer;
    }

    public void setCurrentPlayer(Direction currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public int getNorthSouthPoints() {
        return this.score.getNorthSouthPoints();
    }

    public int getEastWestPoints() {
        return this.score.getEastWestPoints();
    }

    public Ruleset getRuleset() {
        return this.ruleset;
    }

    public boolean isFinished() {
        return allPointsPlayed() || allTricksPlayed();
    }

    private boolean allPointsPlayed() {
        int totalPoints = this.ruleset.getTotalPoints();
        int pointsPlayed = this.score.getAlreadyPlayedPoints();
        return pointsPlayed == totalPoints;
    }

    private boolean allTricksPlayed() {
        return this.completedTricks == startingNumberOfCardsInTheHand;
    }

    public int getCompletedTricks() {
        return this.completedTricks;
    }

    /**
     * This method will see if playing the card is a valid move. If it is, it will
     * play it.
     *
     * @param card Card to be played on the board.
     */
    public void playCard(Card card) {
        Hand handOfCurrentPlayer = getHandOfCurrentPlayer();

        throwExceptionIfCardIsNotFromCurrentPlayer(handOfCurrentPlayer, card);
        throwExceptionIfStartingATrickWithHeartsWhenRulesetProhibitsIt(card, handOfCurrentPlayer);
        if (currentTrickAlreadyHasCards()) {
            throwExceptionIfCardDoesNotFollowSuit(card, handOfCurrentPlayer);
        }
        if (currentTrickNotStartedYet()) {
            this.currentTrick = startNewTrick();
        }

        moveCardFromHandToCurrentTrick(card, handOfCurrentPlayer);

        if (currentTrick.isComplete()) {
            Direction currentTrickWinner = this.getWinnerOfCurrentTrick();
            currentPlayer = currentTrickWinner;
            updateScoreboard(currentTrickWinner);
            completedTricks++;
        } else {
            currentPlayer = currentPlayer.next();
        }

    }

    private Hand getHandOfCurrentPlayer() {
        return this.board.getHandOf(this.currentPlayer);
    }

    private void throwExceptionIfCardIsNotFromCurrentPlayer(Hand handOfCurrentPlayer, Card card) {
        if (!handOfCurrentPlayer.containsCard(card)) {
            throw new PlayedCardInAnotherPlayersTurnException();
        }
    }

    private void throwExceptionIfStartingATrickWithHeartsWhenRulesetProhibitsIt(Card card, Hand handOfCurrentPlayer) {
        if (this.currentTrickNotStartedYet() && this.ruleset.prohibitsHeartsUntilOnlySuitLeft() && card.isHeart()
                && !handOfCurrentPlayer.onlyHasHearts()) {
            throw new PlayedHeartsWhenProhibitedException();
        }
    }

    private boolean currentTrickNotStartedYet() {
        return this.currentTrick == null || this.currentTrick.isEmpty() || this.currentTrick.isComplete();
    }

    private boolean currentTrickAlreadyHasCards() {
        return !currentTrickNotStartedYet();
    }

    private void throwExceptionIfCardDoesNotFollowSuit(Card card, Hand handOfCurrentPlayer) {
        if (!this.ruleset.followsSuit(this.currentTrick, handOfCurrentPlayer, card)) {
            throw new DoesNotFollowSuitException();
        }
    }

    private Trick startNewTrick() {
        Trick currentTrick = new Trick(currentPlayer);
        tricks.add(currentTrick);
        boolean isOneOfLastTwoTricks = completedTricks >= (NUMBER_OF_TRICKS_IN_A_COMPLETE_HAND - 2);
        if (isOneOfLastTwoTricks) {
            currentTrick.setLastTwo();
        }
        return currentTrick;
    }

    private void moveCardFromHandToCurrentTrick(Card card, Hand handOfCurrentPlayer) {
        // FIXME Should be a transaction
        handOfCurrentPlayer.removeCard(card);
        currentTrick.addCard(card);
    }

    private Direction getWinnerOfCurrentTrick() {
        return this.ruleset.getWinner(currentTrick);
    }

    private void updateScoreboard(Direction currentTrickWinner) {
        this.score.addTrickToDirection(currentTrick, currentTrickWinner);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((board == null) ? 0 : board.hashCode());
        result = prime * result + completedTricks;
        result = prime * result + ((currentPlayer == null) ? 0 : currentPlayer.hashCode());
        result = prime * result + ((currentTrick == null) ? 0 : currentTrick.hashCode());
        result = prime * result + ((ruleset == null) ? 0 : ruleset.hashCode());
        result = prime * result + ((score == null) ? 0 : score.hashCode());
        result = prime * result + ((tricks == null) ? 0 : tricks.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Deal other = (Deal) obj;
        if (board == null) {
            if (other.board != null)
                return false;
        } else if (!board.equals(other.board))
            return false;
        if (completedTricks != other.completedTricks)
            return false;
        if (currentPlayer != other.currentPlayer)
            return false;
        if (currentTrick == null) {
            if (other.currentTrick != null)
                return false;
        } else if (!currentTrick.equals(other.currentTrick))
            return false;
        if (ruleset == null) {
            if (other.ruleset != null)
                return false;
        } else if (!ruleset.equals(other.ruleset))
            return false;
        if (score == null) {
            if (other.score != null)
                return false;
        } else if (!score.equals(other.score))
            return false;
        if (tricks == null) {
            if (other.tricks != null)
                return false;
        } else if (!tricks.equals(other.tricks))
            return false;
        return true;
    }

    public Direction getDealer() {
        return this.board.getDealer();
    }

    public Score getScore() {
        return this.score;
    }

    public void setStartingNumberOfCardsInTheHand(int startingNumberOfCardsInTheHand) {
        this.startingNumberOfCardsInTheHand = startingNumberOfCardsInTheHand;
    }

    public void setDummy(Direction direction) {
        this.dummy = direction;
    }

    public Direction getDummy() {
        return this.dummy;
    }

    public boolean isDummyOpen() {
        return this.getCompletedTricks() > 0 || !this.getCurrentTrick().isEmpty();
    }

}
