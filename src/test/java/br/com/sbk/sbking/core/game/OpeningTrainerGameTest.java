package br.com.sbk.sbking.core.game;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import org.junit.Test;

import br.com.sbk.sbking.core.Board;
import br.com.sbk.sbking.core.BridgeContract;
import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.core.Hand;
import br.com.sbk.sbking.core.Rank;
import br.com.sbk.sbking.core.Strain;
import br.com.sbk.sbking.core.Suit;

public class OpeningTrainerGameTest {

    @Test
    public void getOpeningShouldReturnCorrectOpening() {

        Board boardWithTwoClubs = this.boardWithTwoClubsOpening();

        OpeningTrainerGame openingTrainerTwoClubs = new OpeningTrainerGame(boardWithTwoClubs);
        BridgeContract twoClubs = new BridgeContract(2, Strain.CLUBS, false, false, false,
                boardWithTwoClubs.getDealer());

        assertEquals(twoClubs, openingTrainerTwoClubs.getOpening());
    }

    private Board boardWithTwoClubsOpening() {
        Hand hand = new Hand();

        hand.addCard(new Card(Suit.SPADES, Rank.ACE));
        hand.addCard(new Card(Suit.SPADES, Rank.KING));
        hand.addCard(new Card(Suit.SPADES, Rank.QUEEN));
        hand.addCard(new Card(Suit.SPADES, Rank.JACK));

        hand.addCard(new Card(Suit.HEARTS, Rank.ACE));
        hand.addCard(new Card(Suit.HEARTS, Rank.KING));
        hand.addCard(new Card(Suit.HEARTS, Rank.QUEEN));
        hand.addCard(new Card(Suit.HEARTS, Rank.JACK));

        hand.addCard(new Card(Suit.DIAMONDS, Rank.ACE));
        hand.addCard(new Card(Suit.DIAMONDS, Rank.KING));
        hand.addCard(new Card(Suit.DIAMONDS, Rank.QUEEN));
        hand.addCard(new Card(Suit.DIAMONDS, Rank.JACK));
        hand.addCard(new Card(Suit.DIAMONDS, Rank.TEN));

        HashMap<Direction, Hand> hands = new HashMap<>();
        hands.put(Direction.NORTH, hand);
        hands.put(Direction.EAST, new Hand());
        hands.put(Direction.SOUTH, new Hand());
        hands.put(Direction.WEST, new Hand());
        Board board = new Board(hands, Direction.NORTH);
        return board;
    }

}
