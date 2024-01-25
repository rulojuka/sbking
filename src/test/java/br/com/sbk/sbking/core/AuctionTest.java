package br.com.sbk.sbking.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import br.com.sbk.sbking.core.exceptions.AuctionAlreadyFinishedException;
import br.com.sbk.sbking.core.exceptions.CallInAnotherPlayersTurnException;
import br.com.sbk.sbking.core.exceptions.InsufficientBidException;
import br.com.sbk.sbking.core.exceptions.InvalidCallException;

/**
 * The rules for a Bridge Auction are very well established and pretty simple.
 * This test will take this into consideration and ignore many complex cases
 * created by composition of simpler ones.
 *
 * With that said: beware of changing the Auction class. This new code should
 * probably go somewhere else unless the Laws of Bridge have changed.
 */

class AuctionTest {

    @Test
    void shouldGetDealerFromConstructor() {
        Direction east = Direction.EAST;
        Direction north = Direction.NORTH;
        assertEquals(east, new Auction(east).getDealer());
        assertEquals(north, new Auction(north).getDealer());
    }

    @Test
    void shouldReturnAnUnmodifiableListOfBids() {
        Auction subject = new Auction(Direction.EAST);
        List<Call> bids = subject.getBids();
        assertThrows(UnsupportedOperationException.class, () -> {
            bids.add(new PassingCall());
        });
    }

    @Test
    void shouldNotBeFinishedJustAfterCreation() {
        Auction auction = new Auction(Direction.EAST);
        assertFalse(auction.isFinished());
    }

    @Test
    void shouldThrowExceptionWhenAPlayerCallsOutOfTurn() {
        Auction auction = new Auction(Direction.EAST);
        Call pass = BiddingBox.PASS;
        assertThrows(CallInAnotherPlayersTurnException.class, () -> {
            auction.makeCall(auction.getDealer().next(),pass);
        });
    }

    @Test
    void shouldThrowExceptionWhenAPlayerCallsOnAFinishedAuction() {
        Direction direction = Direction.NORTH;
        Auction auction = new Auction(direction);
        Call pass = BiddingBox.PASS;
        for(int i=0; i<4; i++) {
            auction.makeCall(direction, pass);
            direction = direction.next();
        }
        assertThrows(AuctionAlreadyFinishedException.class, () -> {
            auction.makeCall(Direction.NORTH,pass);
        });
    }

    @Test
    void shouldThrowExceptionWhenAPlayerMakesAnInvalidDouble() {
        Auction auction = new Auction(Direction.NORTH);
        auction.makeCall(Direction.NORTH, BiddingBox.PASS);
        assertThrows(InvalidCallException.class, () -> {
            auction.makeCall(Direction.EAST,BiddingBox.DOUBLE);
        });
    }

    @Test
    void shouldThrowExceptionWhenAPlayerMakesAnInvalidRedouble() {
        Auction auction = new Auction(Direction.NORTH);
        auction.makeCall(Direction.NORTH, BiddingBox.PASS);
        assertThrows(InvalidCallException.class, () -> {
            auction.makeCall(Direction.EAST,BiddingBox.REDOUBLE);
        });
    }

    @Test
    void shouldThrowExceptionWhenAPlayerMakesAnInsufficientBid() {
        Auction auction = new Auction(Direction.NORTH);
        auction.makeCall(Direction.NORTH, BiddingBox.get("2C"));
        assertThrows(InsufficientBidException.class, () -> {
            auction.makeCall(Direction.EAST,BiddingBox.get("1N"));
        });
    }

    @Test
    void shouldThrowExceptionWhenAPlayerDoublesAPartnerBid() {
        Auction auction = new Auction(Direction.NORTH);
        auction.makeCall(Direction.NORTH, BiddingBox.get("1C"));
        auction.makeCall(Direction.EAST, BiddingBox.PASS);
        assertThrows(InvalidCallException.class, () -> {
            auction.makeCall(Direction.SOUTH,BiddingBox.DOUBLE);
        });
    }

    @Test
    void shouldThrowExceptionWhenAPlayerRedoublesAPartnerDouble() {
        Auction auction = new Auction(Direction.NORTH);
        auction.makeCall(Direction.NORTH, BiddingBox.get("1C"));
        auction.makeCall(Direction.EAST, BiddingBox.DOUBLE);
        auction.makeCall(Direction.SOUTH, BiddingBox.PASS);
        assertThrows(InvalidCallException.class, () -> {
            auction.makeCall(Direction.WEST,BiddingBox.REDOUBLE);
        });
    }

    @Test
    void shouldFinishSomeValidAuctions() {
        String validAuctions[]
                = new String[] {
                        "P P P P",
                        "1C P P P",
                        "P 1S P 2D P 2S P 3H P 3N X P P P",
                        "P 1C P 1D P 1H P 1S P 2S P 2N P 3N P 4H P P P",
                        "1C P 1S P 2H P 3H P 3S P 4S P 5D P 6C P 6H P P P",
                        "P 1C P 2C P 2N P 3N P P P",
                        "P 1N P P P",
                        "P 1D P 1S P 1N 2C 2S P P P",
                        "1D P 1H P 1N P 2N P P P",
                        "1S P 2S X P 3C 3S 4D X XX P P P"
        };
        for (String validAuction : validAuctions) {
            Direction current = Direction.EAST;
            Auction auction = new Auction(current);
            for(String call : validAuction.split(" ")) {
                assertFalse(auction.isFinished());
                auction.makeCall(current, BiddingBox.get(call));
                current = current.next();
            }
            assertTrue(auction.isFinished());
        }
    }

}
