package br.com.sbk.sbking.learning;

import static org.junit.Assert.assertEquals;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.sbk.sbking.core.Board;
import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Dealer;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.core.Hand;
import br.com.sbk.sbking.core.Rank;
import br.com.sbk.sbking.core.Scoreboard;
import br.com.sbk.sbking.core.Suit;
import br.com.sbk.sbking.core.Trick;
import br.com.sbk.sbking.core.rulesets.abstractClasses.Ruleset;
import br.com.sbk.sbking.core.rulesets.concrete.NegativeTricksRuleset;
import br.com.sbk.sbking.networking.Serializator;

public class SerializationTest {

	private Serializator serializator;
	private final String FILEPATH = "/tmp/file.ser";
	private FileOutputStream fileOutputStream;
	private ObjectOutputStream objectOutputStream;
	private FileInputStream fileInputStream;
	private ObjectInputStream objectInputStream;

	@Before
	public void initializeSerializator() {
		try {
			fileOutputStream = new FileOutputStream(FILEPATH);
			objectOutputStream = new ObjectOutputStream(fileOutputStream);

			fileInputStream = new FileInputStream(FILEPATH);
			objectInputStream = new ObjectInputStream(fileInputStream);
			this.serializator = new Serializator(objectInputStream, objectOutputStream);

		} catch (IOException i) {
			i.printStackTrace();
		}
	}

	@After
	public void destroySerializator() {
		try {
			objectInputStream.close();
			fileInputStream.close();
			objectOutputStream.close();
			fileOutputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void suitShouldBeSerializableAndDeserializable() {
		Suit diamonds = Suit.DIAMONDS;
		serializator.tryToSerialize(diamonds);
		Object deserializedObject = serializator.tryToDeserialize();
		Suit deserializedSuit = (Suit) deserializedObject;
		assertEquals(diamonds, deserializedSuit);
	}

	@Test
	public void rankShouldBeSerializableAndDeserializable() {
		Rank king = Rank.KING;
		serializator.tryToSerialize(king);
		Object deserializedObject = serializator.tryToDeserialize();
		Rank deserializedRank = (Rank) deserializedObject;
		assertEquals(king, deserializedRank);
	}

	@Test
	public void directionShouldBeSerializableAndDeserializable() {
		Direction east = Direction.EAST;
		serializator.tryToSerialize(east);
		Object deserializedObject = serializator.tryToDeserialize();
		Direction deserializedDirection = (Direction) deserializedObject;
		assertEquals(east, deserializedDirection);
	}

	@Test
	public void cardShouldBeSerializableAndDeserializable() {
		Suit diamonds = Suit.DIAMONDS;
		Rank king = Rank.KING;
		Card kingOfDiamonds = new Card(diamonds, king);
		serializator.tryToSerialize(kingOfDiamonds);
		Object deserializedObject = serializator.tryToDeserialize();
		Card deserializedCard = (Card) deserializedObject;
		assertEquals(kingOfDiamonds, deserializedCard);
	}

	@Test
	public void handShouldBeSerializableAndDeserializable() {
		Card firstCard = new Card(Suit.DIAMONDS, Rank.JACK);
		Card secondCard = new Card(Suit.CLUBS, Rank.EIGHT);
		Hand hand = new Hand();
		hand.addCard(firstCard);
		hand.addCard(secondCard);

		serializator.tryToSerialize(hand);

		Object deserializedObject = serializator.tryToDeserialize();
		Hand deserializedHand = (Hand) deserializedObject;
		assertEquals(hand, deserializedHand);
	}

	@Test
	public void boardShouldBeSerializableAndDeserializable() {
		Direction east = Direction.EAST;
		Dealer dealer = new Dealer(east);
		Board board = dealer.dealBoard();

		serializator.tryToSerialize(board);

		Object deserializedObject = serializator.tryToDeserialize();
		Board deserializedBoard = (Board) deserializedObject;
		assertEquals(board, deserializedBoard);
	}

	@Test
	public void scoreboardShouldBeSerializableAndDeserializable() {
		Scoreboard scoreboard = new Scoreboard(new NegativeTricksRuleset());

		serializator.tryToSerialize(scoreboard);

		Object deserializedObject = serializator.tryToDeserialize();
		Scoreboard deserializedScoreboard = (Scoreboard) deserializedObject;
		assertEquals(scoreboard, deserializedScoreboard);
	}

	@Test
	public void rulesetShouldBeSerializableAndDeserializable() {
		Ruleset ruleset = new NegativeTricksRuleset();

		serializator.tryToSerialize(ruleset);

		Object deserializedObject = serializator.tryToDeserialize();
		Ruleset deserializedRuleset = (Ruleset) deserializedObject;
		assertEquals(ruleset, deserializedRuleset);
	}

	@Test
	public void trickShouldBeSerializableAndDeserializable() {
		Trick trick = new Trick(Direction.SOUTH);
		Card firstCard = new Card(Suit.DIAMONDS, Rank.JACK);
		Card secondCard = new Card(Suit.CLUBS, Rank.EIGHT);
		trick.addCard(firstCard);
		trick.addCard(secondCard);

		serializator.tryToSerialize(trick);

		Object deserializedObject = serializator.tryToDeserialize();
		Trick deserializedTrick = (Trick) deserializedObject;
		assertEquals(trick, deserializedTrick);
	}

	@Test
	public void dealShouldBeSerializableAndDeserializable() {
		Dealer dealer = new Dealer(Direction.SOUTH);
		Deal deal = dealer.deal(new NegativeTricksRuleset());

		serializator.tryToSerialize(deal);

		Object deserializedObject = serializator.tryToDeserialize();
		Deal deserializedDeal = (Deal) deserializedObject;
		assertEquals(deal, deserializedDeal);
	}
	
	@Test
	public void dealWithCurrentTrickShouldBeSerializableAndDeserializable() {
		Dealer dealer = new Dealer(Direction.SOUTH);
		Deal deal = dealer.deal(new NegativeTricksRuleset());
		Card anyCardOfCurrentPlayer = deal.getHandOf(deal.getCurrentPlayer()).get(0);
		deal.playCard(anyCardOfCurrentPlayer);

		serializator.tryToSerialize(deal);

		Object deserializedObject = serializator.tryToDeserialize();
		Deal deserializedDeal = (Deal) deserializedObject;
		assertEquals(deal, deserializedDeal);
		assertEquals(anyCardOfCurrentPlayer,deserializedDeal.getCurrentTrick().getCards().get(0));
	}

}
