package core;

import static org.junit.Assert.*;

import java.util.Iterator;

import javax.swing.ImageIcon;

import org.junit.BeforeClass;
import org.junit.Test;

public class CardTest {

	private static Suit suit;
	private static Rank rank;
	private static Card card;
	private static ImageIcon image;
	
	@BeforeClass 
	public static void setup(){
		Iterator suitIterator = Suit.VALUES.iterator();
		suit = (Suit) suitIterator.next();
		Iterator rankIterator = Rank.VALUES.iterator();
		rank = (Rank) rankIterator.next();
		image = new ImageIcon();
		card = new Card(suit,rank,image);
	}

	@Test
	public void shouldGetFilename() {
		assertEquals("d2.png", Card.getFilename(suit,rank));
	}

	@Test
	public void shouldGetSuit() {
		assertEquals(suit,card.getSuit());
	}

	@Test
	public void testGetRank() {
		assertEquals(rank,card.getRank());
	}

	@Test
	public void testGetCardImage() {
		assertEquals(image,card.getCardImage());
	}

	@Test
	public void testToString() {
		assertEquals("Two of Diamonds", card.toString());
	}

	@Test
	public void testCompareTo() {
		Iterator rankIterator = Rank.VALUES.iterator();
		rankIterator.next();
		Rank otherRank = (Rank) rankIterator.next();
		Card otherCard = new Card(suit,otherRank,image);
		assertEquals(1, card.compareTo(otherCard));
	}

	@Test
	public void testPoints() {
		Iterator rankIterator = Rank.VALUES.iterator();
		for(int i=0;i<11;i++)
			rankIterator.next();
		Rank otherRank = (Rank) rankIterator.next();
		Card otherCard = new Card(suit,otherRank,image); /*King of diamonds*/
		assertEquals(0, card.points());
		assertEquals(3, otherCard.points());
	}

}
