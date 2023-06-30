package br.com.sbk.sbking.core;

import static br.com.sbk.sbking.core.GameConstants.NUMBER_OF_TRICKS_IN_A_COMPLETE_HAND;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import br.com.sbk.sbking.core.comparators.CardInsideHandWithSuitComparator;
import br.com.sbk.sbking.core.exceptions.DoesNotFollowSuitException;
import br.com.sbk.sbking.core.exceptions.PlayedCardInAnotherPlayersTurnException;
import br.com.sbk.sbking.core.exceptions.PlayedHeartsWhenProhibitedException;
import br.com.sbk.sbking.core.rulesets.abstractrulesets.Ruleset;

public class Deal {

    /**
     * @deprecated Kryo needs a no-arg constructor
     */
    @Deprecated
    @SuppressWarnings("unused")
    private Deal() {
    }

    private Board board;
    private int completedTricks;
    private int startingNumberOfCardsInTheHand;
    private Direction currentPlayer;
    private Score score;
    private Map<Direction, Player> players = new EnumMap<Direction, Player>(Direction.class);

    private Ruleset ruleset;

    private List<Trick> tricks;
    private Trick currentTrick;
    private Direction dummy;

    private Direction claimer;
    private Map<Direction, Boolean> acceptedClaimMap = new EnumMap<Direction, Boolean>(Direction.class);
    private Boolean isPartnershipGame;

    public Deal(Board board, Ruleset ruleset, Direction leader, Boolean isPartnershipGame) {
        this.board = board;
        this.currentPlayer = leader;
        this.setRuleset(ruleset);
        this.completedTricks = 0;
        this.startingNumberOfCardsInTheHand = board.getHandOf(leader).size();
        this.tricks = new ArrayList<Trick>();
        this.players = new EnumMap<Direction, Player>(Direction.class);
        for (Direction direction : Direction.values()) {
            acceptedClaimMap.put(direction, false);
        }
        this.isPartnershipGame = isPartnershipGame;
    }

    public void setRuleset(Ruleset ruleset) {
        this.ruleset = ruleset;
        this.board.sortAllHands(ruleset.getComparator());
        this.score = new Score(ruleset);
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
        if (currentTrickHasCards()) {
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
        HandEvaluations handEvaluations = handOfCurrentPlayer.getHandEvaluations();
        if (this.currentTrickNotStartedYet() && this.ruleset.prohibitsHeartsUntilOnlySuitLeft() && card.isHeart()
                && !handEvaluations.onlyHasHearts()) {
            throw new PlayedHeartsWhenProhibitedException();
        }
    }

    private boolean currentTrickNotStartedYet() {
        return this.currentTrick == null || this.currentTrick.isEmpty() || this.currentTrick.isComplete();
    }

    private boolean currentTrickHasCards() {
        return !currentTrickNotStartedYet();
    }

    private void throwExceptionIfCardDoesNotFollowSuit(Card card, Hand handOfCurrentPlayer) {
        if (!this.ruleset.followsSuit(this.currentTrick, handOfCurrentPlayer, card)) {
            throw new DoesNotFollowSuitException();
        }
    }

    private Trick startNewTrick() {
        Trick newTrick = new Trick(currentPlayer);
        tricks.add(newTrick);
        boolean isOneOfLastTwoTricks = completedTricks >= (NUMBER_OF_TRICKS_IN_A_COMPLETE_HAND - 2);
        if (isOneOfLastTwoTricks) {
            newTrick.setLastTwo();
        }
        return newTrick;
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

    private Direction getCorrectUndoDirectionConsideringDummy(Direction direction) {
        Direction lastPlayer;
        if (this.currentTrickHasCards()) {
            lastPlayer = this.currentPlayer.next(3);
        } else if (this.completedTricks > 0) {
            lastPlayer = this.getPreviousTrick().getLastPlayer();
        } else {
            return direction;
        }
        if (this.dummy != null && direction.next(2) == this.dummy) {
            if (lastPlayer == this.dummy.next(1) || lastPlayer == this.dummy) {
                return this.dummy;
            }
        }
        return direction;
    }

    private Trick getPreviousTrick() {
        if (this.completedTricks == 0) {
            throw new RuntimeException("There is no previous trick.");
        }
        return this.tricks.get(this.completedTricks - 1);
    }

    public void undo(Direction direction) {
        direction = getCorrectUndoDirectionConsideringDummy(direction);
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

    public void giveBackAllCardsToHands() {
        for (Trick trick : tricks) {
            Direction currentDirection = trick.getLeader();
            for (Card card : trick.getCards()) {
                this.board.getHandOf(currentDirection).addCard(card);
                currentDirection = currentDirection.next();
            }
        }
        this.currentTrick = startNewTrick();
        this.board.sortAllHands(ruleset.getComparator());
    }

    public void claim(Direction direction) {
        if (this.claimer == null) {
            this.claimer = direction;
        }
    }

    public void acceptClaim(Direction direction) {
        this.acceptedClaimMap.put(direction, true);
        if (this.hasOtherPlayersAcceptedClaim()) {
            this.finishDeal(this.claimer);
        }
    }

    public void rejectClaim() {
        this.claimer = null;
        for (Direction direction : Direction.values()) {
            acceptedClaimMap.put(direction, false);
        }
    }

    private boolean hasOtherPlayersAcceptedClaim() {
        return this.acceptedClaimMap.entrySet().stream().filter(entry -> !isClaimerOrPartner(entry) && !isDummy(entry))
                .map(Entry::getValue).reduce(Boolean::logicalAnd).get();
    }

    private boolean isClaimerOrPartner(Map.Entry<Direction, Boolean> entry) {
        Direction direction = entry.getKey();
        boolean isClaimer = direction == this.claimer;
        boolean isClaimerPartner = this.isPartnershipGame && direction.next(2) == this.claimer;
        return isClaimer || isClaimerPartner;
    }

    private boolean isDummy(Map.Entry<Direction, Boolean> entry) {
        return entry.getKey() == this.dummy;
    }

    private void finishDeal(Direction winner) {
        this.finishScore(winner);
    }

    private void finishScore(Direction winner) {
        int totalPoints = this.ruleset.getTotalPoints();
        this.score.finishScore(winner, totalPoints);
    }

    public Direction getClaimer() {
        return this.claimer;
    }

    public Boolean getIsPartnershipGame() {
        return this.isPartnershipGame;
    }

    public Map<Direction, Boolean> getAcceptedClaimMap() {
        return this.acceptedClaimMap;
    }
}
