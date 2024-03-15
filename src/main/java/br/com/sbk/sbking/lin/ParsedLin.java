package br.com.sbk.sbking.lin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import br.com.sbk.sbking.core.Auction;
import br.com.sbk.sbking.core.BoardNumber;
import br.com.sbk.sbking.core.Call;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.core.PlasticBoard;

public class ParsedLin {

    private Map<LinKey, List<Integer>> symbolToListOfIndexes;
    private ArrayList<LinKeyValuePair> list;
    private List<Auction> auctions;

    public ParsedLin(List<LinKeyValuePair> list) {
        symbolToListOfIndexes = new EnumMap<LinKey, List<Integer>>(LinKey.class);
        for (LinKey key : LinKey.values()) {
            symbolToListOfIndexes.put(key, new ArrayList<Integer>());
        }

        this.list = new ArrayList<>();
        int i = 0;
        for (LinKeyValuePair linKeyValuePair : list) {
            symbolToListOfIndexes.get(linKeyValuePair.getKey()).add(i);
            this.list.add(linKeyValuePair);
            i++;
        }

        auctions = null;
    }

    public List<Auction> getAuctions() {
        if (this.auctions != null) {
            return Collections.unmodifiableList(auctions);
        } else {
            List<Auction> auctionList = new ArrayList<Auction>();
            List<Integer> qxIndexes = symbolToListOfIndexes.get(LinKey.QX);
            if (!qxIndexes.isEmpty()) {
                int firstQxIndex = qxIndexes.get(0);
                Auction currentAuction = null;
                boolean firstBoard = true;
                Direction currentDirection = Direction.NORTH;
                for (int currentIndex = firstQxIndex; currentIndex < list.size(); currentIndex++) {
                    LinKey key = list.get(currentIndex).getKey();
                    String value = list.get(currentIndex).getValue();
                    if (key.equals(LinKey.QX)) {
                        if (firstBoard) {
                            firstBoard = false;
                        } else { // Board is finished. Add auction to list
                            auctionList.add(currentAuction);
                        }
                        // Then start the new board
                        Integer boardNumber = Integer.parseInt(value.substring(1));
                        Direction dealer = PlasticBoard.getDealerFromBoardNumber(new BoardNumber(boardNumber));
                        currentAuction = new Auction(dealer);
                        currentDirection = dealer;
                    } else if (key.equals(LinKey.MB)) { // For every bid
                        Call currentCall = LinParser.parseFromLinCall(value);
                        currentAuction.makeCall(currentDirection, currentCall);
                        currentDirection = currentDirection.next();
                    }
                }
                auctionList.add(currentAuction); // Finish last auction.
            }
            this.auctions = auctionList;
            return this.getAuctions(); // Guaranteeing memoization
        }
    }

}
