package br.com.sbk.sbking.core;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.jupiter.api.Test;

class AuctionExtendedTest {

    @Test
    void shouldFinishSomeValidAuctions() throws IOException {
        String data = readFromFilename("/auctions-valid.txt");
        String validAuctions[] = data.split("\n");
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

    private String readFromFilename(String filename)
            throws IOException {
        InputStream inputStream = AuctionExtendedTest.class.getResourceAsStream(filename);
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br
                = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }

}
