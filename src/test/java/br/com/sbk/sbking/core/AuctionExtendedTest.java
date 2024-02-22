package br.com.sbk.sbking.core;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.TreeMap;

import org.junit.jupiter.api.Test;

class AuctionExtendedTest {

    @Test
    void shouldFinishSomeValidAuctions() throws IOException {
        String data = readFromFilename("/auctions-valid.txt");
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
        String linList[] = readFromFilename("/lin/lin_list.txt").split("\n");
        Map<String, Integer> quantity = new TreeMap<>();
        Auction currentAuction = new Auction(Direction.NORTH);
        Call currentCall = BiddingBox.PASS;
        String currentToken = "";
        // boolean myvar = true;
        for (String linFile : linList) {
            try {

                // System.out.println("Lin file: " + linFile);
                String data = readFromFilenameNoSpace("/lin/" + linFile);
                int limitForSplitNotToRemoveTrailingInformation = -1;
                String linTokens[] = data.trim().split("\\|", limitForSplitNotToRemoveTrailingInformation);
                int length = linTokens.length;
                String boardStart = "qx";
                String makeBid = "mb";
                Direction currentDirection = Direction.NORTH;
                currentAuction = new Auction(currentDirection);
                Boolean firstAuction = true;
                for (int i = 1; i < length; i += 2) {
                    if (boardStart.equals(linTokens[i - 1])) {
                        if (firstAuction) {
                            firstAuction = false;
                        } else {
                            // myvar = currentAuction.isFinished();
                            // System.out.println(myvar);
                            if (currentAuction.getBids().size() > 0) { // Ignore empty auctions
                                if (!currentAuction.isFinished()) {
                                    // System.out.println(linFile + " false");
                                } else { // remove later
//                                    printAuction(currentAuction);
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
                        currentDirection = Direction.NORTH;
                        currentAuction = new Auction(currentDirection);
                    } else if (makeBid.equals(linTokens[i - 1])) {
                        currentToken = linTokens[i];
                        currentCall = parseFromLinCall(currentToken);
                        currentAuction.makeCall(currentDirection, currentCall);
                        currentDirection = currentDirection.next();
                    }
                }

            } catch (Exception e) {
                // printAuction(currentAuction);
                // System.out.println(linFile + " exception. Current call: " + currentToken);
            }

        }

//        for (Entry<String, Integer> entry : quantity.entrySet()) {
//            System.out.println(entry.getValue() + " " + entry.getKey());
//        }

        /*
         *
         * an - "annotation" : bid explanation) mb - "make bid" mc - "make claim" md - "make deal?" : cards en each players hand nt - "note" :
         * commentary from kibitzers pc - "play card" pg - "??" : separator node, used at the end of tricks and annotations pn - "player names" qx -
         * "??" : board info - "o/c" for open/closed. It also initiates the deal rs - "results" : results for previous sessions st - ??? sv -
         * "?? vulnerability" : "o/n/e/b" None-NS-EW-Both vg - "vugraph" : Event information
         */

    }

    private Call parseFromLinCall(String linCall) {
        String pass = "P";
        String[] _double = { "D", "DBL" };
        String[] redouble = { "DD", "REDBL", "R" };

        // System.out.println("Current linCall is: --" + linCall + "--");

        String linCallWithoutAlert = linCall.replace("!", "").toUpperCase();

        if (pass.equals(linCallWithoutAlert)) {
            return BiddingBox.PASS;
        }
        for (String doublePossibleString : _double) {
            if (doublePossibleString.equals(linCallWithoutAlert)) {
                return BiddingBox.DOUBLE;
            }
        }
        for (String redoublePossibleString : redouble) {
            if (redoublePossibleString.equals(linCallWithoutAlert)) {
                return BiddingBox.REDOUBLE;
            }
        }
        String label = linCallWithoutAlert.substring(0, 2);
        return BiddingBox.get(label);
    }

//    private void printAuction(Auction auction) {
//        List<Call> calls = auction.getBids();
//        for (Call call : calls) {
//            if (call.isPass()) {
//                System.out.print("P ");
//            } else if (call.isDouble()) {
//                System.out.print("X ");
//            } else if (call.isRedouble()) {
//                System.out.print("XX ");
//            } else {
//                Bid bid = (Bid) call;
//                System.out.print(bid.getOddTricks().getSymbol() + bid.getStrain().getSymbol() + " ");
//            }
//        }
//        System.out.println();
//    }

    private String readFromFilename(String filename) throws IOException {
        InputStream inputStream = AuctionExtendedTest.class.getResourceAsStream(filename);
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }

    private String readFromFilenameNoSpace(String filename) throws IOException {
        InputStream inputStream = AuctionExtendedTest.class.getResourceAsStream(filename);
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line.trim());
            }
        }
        return resultStringBuilder.toString();
    }

}
