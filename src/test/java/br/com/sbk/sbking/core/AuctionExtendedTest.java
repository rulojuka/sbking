package br.com.sbk.sbking.core;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.junit.jupiter.api.Test;

import br.com.sbk.sbking.lin.LinParser;
import br.com.sbk.sbking.lin.ParsedLin;
import br.com.sbk.sbking.utils.FileUtils;

class AuctionExtendedTest {

    @Test
    void shouldFinishSomeValidAuctions() throws IOException {
        String data = FileUtils.readFromFilename("/auctions-valid.txt", false);
        String validAuctions[] = data.split("\n");
        for (String validAuction : validAuctions) {
            Direction current = Direction.EAST;
            Auction auction = new Auction(current);
            for (String call : validAuction.split(" ")) {
                assertFalse(auction.isFinished());
                auction.makeCall(current, BiddingBox.get(call));
                current = current.next();
            }
            assertTrue(auction.isFinished());
        }
    }

    @Test
    void shouldReadLinFile() throws IOException {
        String linList[] = FileUtils.readFromFilename("/lin/lin_list.txt", false).split("\n");
        Map<String, Integer> quantity = new TreeMap<>();
        for (String linFile : linList) {
            try {
                String data = FileUtils.readFromFilename("/lin/" + linFile, true);
                ParsedLin parsedLin = LinParser.fromString(data);
                List<Auction> auctions = parsedLin.getAuctions();

                for (Auction currentAuction : auctions) {
                    if (currentAuction.getBids().size() > 0) { // Ignore empty auctions
                        if (!currentAuction.isFinished()) {
                            // System.out.println(linFile + " false");
                        } else { // remove later
                            System.out.println(currentAuction);
                            assertTrue(currentAuction.isFinished());
                            if (currentAuction.getBids().size() > 4) {
                                Contract finalContract = currentAuction.getFinalContract();
                                //                                        System.out.println(finalContract);
                                String finalContractText = finalContract.toString();
                                if (quantity.get(finalContractText) == null) {
                                    quantity.put(finalContractText, 1);
                                } else {
                                    int oldQuantity = quantity.get(finalContractText);
                                    quantity.put(finalContractText, oldQuantity + 1);
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                // System.out.println(currentAuction);
                // System.out.println(linFile + " exception. Current call: " + currentToken);
            }

        }

    }

}
