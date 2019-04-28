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
import br.com.sbk.sbking.core.BoardDealer;
import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.core.Hand;
import br.com.sbk.sbking.core.Rank;
import br.com.sbk.sbking.core.Score;
import br.com.sbk.sbking.core.ShuffledDeck;
import br.com.sbk.sbking.core.Suit;
import br.com.sbk.sbking.core.Trick;
import br.com.sbk.sbking.core.rulesets.abstractClasses.Ruleset;
import br.com.sbk.sbking.core.rulesets.concrete.NegativeTricksRuleset;
import br.com.sbk.sbking.networking.core.serialization.Serializator;

//FIXME This is an integration test.
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
		Suit deserializedSuit = serializator.tryToDeserialize(Suit.class);
		assertEquals(diamonds, deserializedSuit);
	}

	@Test
	public void rankShouldBeSerializableAndDeserializable() {
		Rank king = Rank.KING;
		serializator.tryToSerialize(king);
		Rank deserializedRank = serializator.tryToDeserialize(Rank.class);
		assertEquals(king, deserializedRank);
	}

	@Test
	public void directionShouldBeSerializableAndDeserializable() {
		Direction east = Direction.EAST;
		serializator.tryToSerialize(east);
		Direction deserializedDirection = serializator.tryToDeserialize(Direction.class);
		assertEquals(east, deserializedDirection);
	}

	@Test
	public void cardShouldBeSerializableAndDeserializable() {
		Suit diamonds = Suit.DIAMONDS;
		Rank king = Rank.KING;
		Card kingOfDiamonds = new Card(diamonds, king);
		serializator.tryToSerialize(kingOfDiamonds);
		Card deserializedCard = serializator.tryToDeserialize(Card.class);
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

		Hand deserializedHand = serializator.tryToDeserialize(Hand.class);
		assertEquals(hand, deserializedHand);
	}

	@Test
	public void scoreboardShouldBeSerializableAndDeserializable() {
		Score scoreboard = new Score(new NegativeTricksRuleset());

		serializator.tryToSerialize(scoreboard);

		Score deserializedScoreboard = serializator.tryToDeserialize(Score.class);
		assertEquals(scoreboard, deserializedScoreboard);
	}

	@Test
	public void rulesetShouldBeSerializableAndDeserializable() {
		Ruleset ruleset = new NegativeTricksRuleset();

		serializator.tryToSerialize(ruleset);

		Ruleset deserializedRuleset = serializator.tryToDeserialize(Ruleset.class);
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

		Trick deserializedTrick = serializator.tryToDeserialize(Trick.class);
		assertEquals(trick, deserializedTrick);
	}

	@Test
	public void dealShouldBeSerializableAndDeserializable() {
		Board anyBoard = createSouthBoard();
		NegativeTricksRuleset anyRuleset = new NegativeTricksRuleset();
		Deal deal = new Deal(anyBoard, anyRuleset);

		serializator.tryToSerialize(deal);

		Deal deserializedDeal = serializator.tryToDeserialize(Deal.class);
		assertEquals(deal, deserializedDeal);
	}

	@Test
	public void dealWithCurrentTrickShouldBeSerializableAndDeserializable() {
		Board anyBoard = createSouthBoard();
		NegativeTricksRuleset anyRuleset = new NegativeTricksRuleset();
		Deal deal = new Deal(anyBoard, anyRuleset);
		Card anyCardOfCurrentPlayer = deal.getHandOf(deal.getCurrentPlayer()).get(0);
		deal.playCard(anyCardOfCurrentPlayer);

		serializator.tryToSerialize(deal);

		Deal deserializedDeal = serializator.tryToDeserialize(Deal.class);
		assertEquals(deal, deserializedDeal);
		assertEquals(anyCardOfCurrentPlayer, deserializedDeal.getCurrentTrick().getCards().get(0));
	}

	private Board createSouthBoard() {
		BoardDealer boardDealer = new BoardDealer();
		Board anyBoard = boardDealer.dealBoard(Direction.SOUTH, new ShuffledDeck());
		return anyBoard;
	}

}
