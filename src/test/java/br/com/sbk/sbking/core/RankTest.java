package br.com.sbk.sbking.core;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;

import org.junit.BeforeClass;
import org.junit.Test;

public class RankTest {

  private static Rank two;
  private static Rank three;
  private static Rank four;
	
  @BeforeClass 
  public static void setup(){
		Iterator rankIterator = Rank.VALUES.iterator();
		two = (Rank) rankIterator.next();
		three = (Rank) rankIterator.next();
		four = (Rank) rankIterator.next();
  }
  
  @Test
  public void shouldBeInOrder() {
	Iterator rankIterator = Rank.VALUES.iterator();
    assertEquals(two, (Rank) rankIterator.next());
    assertEquals(three, (Rank) rankIterator.next());
    assertEquals(four, (Rank) rankIterator.next());
  }
  
  @Test
  public void shouldGetName() {
    assertEquals("Two", two.getName());
    assertEquals("Two", two.toString());
  }
  
  @Test
  public void shouldGetSymbol() {
    assertEquals("2", two.getSymbol());
  }

  @Test
  public void shouldCompareCorrectly() {
	assertEquals(three.compareTo(two),-1);
    assertEquals(three.compareTo(four),1);
  }
  
}