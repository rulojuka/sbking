package br.com.sbk.sbking.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.sbk.sbking.core.exceptions.AuctionAlreadyFinishedException;
import br.com.sbk.sbking.core.exceptions.CallInAnotherPlayersTurnException;
import br.com.sbk.sbking.core.exceptions.InsufficientBidException;
import br.com.sbk.sbking.core.exceptions.InvalidCallException;

/**
 * The rules for Bridge Auction are as follows (paraphrasing/quoting the 2017 Laws of Bridge - LAWS 17-22):
 *
 * - The first call is made by the dealer - Every other call is made by the player who goes next from the last caller - The auction ends after 4
 * consecutive passes (this only happens when they are the first 4 calls) - The auction ends after 3 consecutive passes if a bid has already been
 * made.
 *
 * - Pass is always a valid call - Double (X) can only be called if and only if the last non-pass call was made by the opponents AND it was a bid. -
 * Redouble (XX) can only be called if and only if the last non-pass call was made by the opponents AND it was a double (X). - The first bid is freely
 * chosen from all bids. All other bids must supersede the last bid (and consequently all previous ones).
 *
 */

public final class Auction {

    private static final int NUMBER_OF_INITIAL_PASSES_TO_END_AUCTION = 4;
    private static final int NUMBER_OF_PASSES_TO_END_AUCTION_AFTER_BID = 3;
    private Direction dealer;
    private Direction currentTurn;
    private List<Call> bids;
    private boolean finished;
    /**
     * These are initialized as -1
     */
    private int lastNonPassCallIndex;
    private int lastBidIndex;
    private Contract finalContract;

    public Auction(Direction dealer) {
        this.dealer = dealer;
        this.currentTurn = dealer;
        this.bids = new ArrayList<Call>();
        this.finished = false;
        this.lastNonPassCallIndex = -1;
        this.lastBidIndex = -1;
        this.finalContract = null;
    }

    public Direction getDealer() {
        return this.dealer;
    }

    public Direction getCurrentTurn() {
        return this.currentTurn;
    }

    public List<Call> getBids() {
        return Collections.unmodifiableList(bids);
    }

    public boolean isFinished() {
        return this.finished;
    }

    public void makeCall(Direction direction, Call call) {
        if (this.isFinished()) {
            throw new AuctionAlreadyFinishedException();
        }
        if (this.currentTurn != direction) {
            throw new CallInAnotherPlayersTurnException();
        }
        if (direction == null || call == null) {
            throw new IllegalArgumentException("Parameters should not be null");
        }

        if (call.isPass()) {
            addCall(call);
        } else if (call.isDouble()) {
            if (isDoubleValid()) {
                addCall(call);
            } else {
                throw new InvalidCallException();
            }
        } else if (call.isRedouble()) {
            if (isRedoubleValid()) {
                addCall(call);
            } else {
                throw new InvalidCallException();
            }
        } else if (call.isBid()) {
            if (lastBidIndex == -1) {
                addCall(call);
            } else {
                Bid lastBid = (Bid) this.bids.get(lastBidIndex);
                Bid currentBid = (Bid) call;
                if (currentBid.compareTo(lastBid) > 0) {
                    addCall(call);
                } else {
                    throw new InsufficientBidException();
                }
            }
        }

        checkAndUpdateForFinishedAuction();
    }

    private boolean isDoubleValid() {
        return lastNonPassCallIndex >= 0 && isLastNonPassBidFromOpponents() && this.bids.get(lastNonPassCallIndex).isBid();
    }

    private boolean isRedoubleValid() {
        return lastNonPassCallIndex >= 0 && isLastNonPassBidFromOpponents() && this.bids.get(lastNonPassCallIndex).isDouble();
    }

    private boolean isLastNonPassBidFromOpponents() {
        if (lastNonPassCallIndex < 0) {
            return false;
        }
        return (lastNonPassCallIndex - this.bids.size()) % 2 != 0;
    }

    private void addCall(Call call) {
        Call unmodifiableCopy = getCallFromBiddingBox(call);
        this.bids.add(unmodifiableCopy);
        this.currentTurn = this.currentTurn.next();
        if (!unmodifiableCopy.isPass()) {
            this.lastNonPassCallIndex = this.bids.size() - 1;
        }
        if (unmodifiableCopy.isBid()) {
            this.lastBidIndex = this.bids.size() - 1;
        }
    }

    private Call getCallFromBiddingBox(Call call) {
        return BiddingBox.get(call);
    }

    private void checkAndUpdateForFinishedAuction() {
        int numberOfTailPasses = this.getTailPasses();
        if (numberOfTailPasses >= NUMBER_OF_INITIAL_PASSES_TO_END_AUCTION) {
            this.finished = true;
        } else if (numberOfTailPasses == NUMBER_OF_PASSES_TO_END_AUCTION_AFTER_BID && lastBidIndex >= 0) {
            this.finished = true;
        }
    }

    private int getTailPasses() {
        int tailPasses = 0;
        for (int i = this.bids.size() - 1; i >= 0; i--) {
            if (this.bids.get(i).isPass()) {
                tailPasses++;
            } else {
                break;
            }
        }
        return tailPasses;
    }

    public Contract getFinalContract() {
        if (!this.isFinished()) {
            throw new IllegalStateException("Auction must be finished to have a final contract.");
        }

        if (this.finalContract != null) {
            return this.finalContract;
        }

        Bid lastBid = null;
        int lastBidIndex = -1;
        for (int i = this.bids.size() - 1; i >= 0; i--) {
            if (this.bids.get(i).isBid()) {
                lastBid = (Bid) this.bids.get(i);
                lastBidIndex = i;
                break;
            }
        }

        if (lastBid == null) {
            // Ignoring PASS for now
            throw new IllegalStateException("PASS is not implemented yet.");
        } else {
            List<Call> subList = this.bids.subList(lastBidIndex, this.bids.size());
            boolean redoubled = subList.stream().anyMatch(call -> call.isRedouble());
            boolean doubled = subList.stream().anyMatch(call -> call.isDouble());
            boolean nonVul = false;
            int level = lastBid.getOddTricks().getLevel();
            Strain strain = lastBid.getStrain();
            this.finalContract = new Contract(level, strain, doubled, redoubled, nonVul);
            return this.finalContract;
        }
    }

    @Override
    public String toString() {
        StringBuilder returnValue = new StringBuilder();
        List<Call> calls = this.getBids();
        for (Call call : calls) {
            if (call.isPass()) {
                returnValue.append("P ");
            } else if (call.isDouble()) {
                returnValue.append("X ");
            } else if (call.isRedouble()) {
                returnValue.append("XX ");
            } else {
                Bid bid = (Bid) call;
                returnValue.append(bid.getOddTricks().getSymbol() + bid.getStrain().getSymbol() + " ");
            }
        }
        return returnValue.toString();
    }

}
