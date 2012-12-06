package core;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;

import org.junit.BeforeClass;
import org.junit.Test;

public class SuitTest {

  private static Suit diamonds;
  private static Suit clubs;
  private static Suit spades;
  private static Suit hearts;
	
  @BeforeClass 
  public static void setup(){
		Iterator suitIterator = Suit.VALUES.iterator();
		diamonds = (Suit) suitIterator.next();
		clubs = (Suit) suitIterator.next();
		hearts = (Suit) suitIterator.next();
		spades = (Suit) suitIterator.next();
  }
  
  @Test
  public void shouldBeInOrder() {
	Iterator suitIterator = Suit.VALUES.iterator();
    assertEquals(diamonds, (Suit) suitIterator.next());
    assertEquals(clubs, (Suit) suitIterator.next());
    assertEquals(hearts, (Suit) suitIterator.next());
    assertEquals(spades, (Suit) suitIterator.next());
  }
  
  @Test
  public void shouldGetName() {
    assertEquals("Diamonds", diamonds.getName());
    assertEquals("Diamonds", diamonds.toString());
  }
  
  @Test
  public void shouldGetSymbol() {
    assertEquals("d", diamonds.getSymbol());
  }

  @Test
  public void shouldCompareCorrectly() {
	assertEquals(clubs.compareTo(diamonds),-1);
    assertEquals(clubs.compareTo(hearts),1);
    assertEquals(clubs.compareTo(spades),2);
  }
  
}