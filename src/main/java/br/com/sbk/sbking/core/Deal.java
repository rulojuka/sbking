package br.com.sbk.sbking.core;

import static br.com.sbk.sbking.core.GameConstants.NUMBER_OF_TRICKS_IN_A_COMPLETE_HAND;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.sbk.sbking.core.cardComparators.CardInsideHandWithSuitComparator;
import br.com.sbk.sbking.core.exceptions.DoesNotFollowSuitException;
import br.com.sbk.sbking.core.exceptions.PlayedCardInAnotherPlayersTurnException;
import br.com.sbk.sbking.core.exceptions.PlayedHeartsWhenProhibitedException;
import br.com.sbk.sbking.core.rulesets.abstractClasses.Ruleset;

public class Deal {

    /**
     * @deprecated Kryo needs a no-arg constructor
     */
    private Deal() {
    }

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

    private Map<Direction, Boolean> claimMap;
    private Direction claimer;

    public Deal(Board board, Ruleset ruleset, Direction leader) {
        this.board = board;
        this.ruleset = ruleset;
        this.currentPlayer = leader;
        this.board.sortAllHands(ruleset.getComparator());
        this.score = new Score(ruleset);
        this.completedTricks = 0;
        this.startingNumberOfCardsInTheHand = NUMBER_OF_TRICKS_IN_A_COMPLETE_HAND;
        this.tricks = new ArrayList<Trick>();
        this.players = new HashMap<Direction, Player>();
        this.claimMap = new HashMap<Direction, Boolean>();
        for (Direction direction : Direction.values()) {
            claimMap.put(direction, false);
        }
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
        if (this.claimer != null && this.otherPlayersAcceptedClaim()) {
            return;
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
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Deal other = (Deal) obj;
        if (board == null) {
            if (other.board != null) {
                return false;
            }
        } else if (!board.equals(other.board)) {
            return false;
        }
        if (completedTricks != other.completedTricks) {
            return false;
        }
        if (currentPlayer != other.currentPlayer) {
            return false;
        }
        if (currentTrick == null) {
            if (other.currentTrick != null) {
                return false;
            }
        } else if (!currentTrick.equals(other.currentTrick)) {
            return false;
        }
        if (ruleset == null) {
            if (other.ruleset != null) {
                return false;
            }
        } else if (!ruleset.equals(other.ruleset)) {
            return false;
        }
        if (score == null) {
            if (other.score != null) {
                return false;
            }
        } else if (!score.equals(other.score)) {
            return false;
        }
        if (tricks == null) {
            if (other.tricks != null) {
                return false;
            }
        } else if (!tricks.equals(other.tricks)) {
            return false;
        }
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

    public void sortAllHandsByTrumpSuit(Suit trumpSuit) {
        CardInsideHandWithSuitComparator cardInsideHandWithSuitComparator = new CardInsideHandWithSuitComparator(
                trumpSuit);
        this.board.sortAllHands(cardInsideHandWithSuitComparator);
    }

    public void undo(Direction direction) {
        Map<Card, Direction> removedCardsUpToDirection = this.removeCardsUpTo(direction);
        if (!removedCardsUpToDirection.isEmpty()) {
            this.giveBackCardsToHands(removedCardsUpToDirection);
            if (this.currentTrick.isComplete()) {
                Direction winnerOfTrick = this.ruleset.getWinner(this.currentTrick);
                this.setCurrentPlayer(winnerOfTrick);
            } else {
                this.setCurrentPlayer(direction);
            }
        }
        this.removeLastTrickIfEmpty();
    }

    private void removeLastTrickIfEmpty() {
        if (tricks.isEmpty()) {
            return;
        }
        if (this.completedTricks < this.tricks.size()) {
            Trick lastTrick = this.tricks.get(this.completedTricks);
            if (lastTrick != null && lastTrick.isEmpty()) {
                this.tricks.remove(this.completedTricks);
                if (this.completedTricks > 0) {
                    this.currentTrick = this.tricks.get(this.completedTricks - 1);
                }
            }
        }
    }

    private Map<Card, Direction> removeCardsUpTo(Direction direction) {
        Map<Card, Direction> playedCardsUpToDirection = new HashMap<Card, Direction>();
        if (this.currentTrick == null) {
            return playedCardsUpToDirection;
        } else if (this.currentTrick.hasCardOf(direction)) {
            if (this.currentTrick.isComplete()) {
                this.undoScore(this.currentTrick);
                this.completedTricks--;
            }
            playedCardsUpToDirection = this.currentTrick.getCardsFromLastUpTo(direction);
            this.currentTrick.removeCardsFromLastUpTo(direction);
            return playedCardsUpToDirection;
        } else if (this.tricks.size() >= 2) {
            playedCardsUpToDirection = this.currentTrick.getCardDirectionMap();
            this.removeCurrentTrick();
            playedCardsUpToDirection.putAll(this.currentTrick.getCardsFromLastUpTo(direction));
            this.currentTrick.removeCardsFromLastUpTo(direction);
            this.completedTricks--;
            return playedCardsUpToDirection;
        }
        return playedCardsUpToDirection;
    }

    private void removeCurrentTrick() {
        Trick trickToBeRemoved = this.getCurrentTrick();
        this.setCurrentPlayer(trickToBeRemoved.getLeader());
        this.tricks.remove(this.tricks.size() - 1);
        this.currentTrick = this.tricks.get(this.tricks.size() - 1);
        Trick newCurrentTrick = this.getCurrentTrick();
        this.undoScore(newCurrentTrick);
    }

    private void giveBackCardsToHands(Map<Card, Direction> cardDirectionMap) {
        this.board.putCardInHand(cardDirectionMap);
        this.board.sortAllHands(ruleset.getComparator());
    }

    private void undoScore(Trick trick) {
        Direction winnerDirection = this.ruleset.getWinner(trick);
        score.subtractTrickFromDirection(trick, winnerDirection);
    }

    public List<Trick> getTricks() {
        return this.tricks;
    }

    public void claim(Direction direction) {
        if (this.claimer == null) {
            this.claimer = direction;
        } else {
            this.claimMap.put(direction, true);
            if (this.otherPlayersAcceptedClaim()) {
                this.finishDeal(this.claimer);
            }
        }
    }

    private boolean otherPlayersAcceptedClaim() {
        if (this.claimer.isNorthSouth()) {
            return this.claimMap.get(Direction.EAST) && this.claimMap.get(Direction.WEST);
        } else {
            return this.claimMap.get(Direction.NORTH) && this.claimMap.get(Direction.SOUTH);
        }
    }

    public Direction getClaimer() {
        return this.claimer;
    }

    private void finishDeal(Direction winner) {
        this.finishScore(winner);
    }

    private void finishScore(Direction winner) {
        int totalPoints = this.ruleset.getTotalPoints();
        this.score.finishScore(winner, totalPoints);
    }

}
