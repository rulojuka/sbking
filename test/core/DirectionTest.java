package core;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;

import org.junit.BeforeClass;
import org.junit.Test;

public class DirectionTest {

  private static Direction north;
  private static Direction east;
  private static Direction south;
  private static Direction west;
	
  @BeforeClass 
  public static void setup(){
		Iterator directionIterator = Direction.VALUES.iterator();
		north = (Direction) directionIterator.next();
		east = (Direction) directionIterator.next();
		south = (Direction) directionIterator.next();
		west = (Direction) directionIterator.next();
  }
  
  @Test
  public void shouldHaveTheRightDirection() {
    assertEquals("North", north.getDirection());
    assertEquals("North", north.toString());
  }
  
  @Test
  public void shouldGetIndex() {
    assertEquals(0, north.index());
    assertEquals(2, south.index());
  }
  
  @Test
  public void shouldGetNext() {
    assertEquals(east, north.next());
    assertEquals(south, north.next(10));
    // Seems wrong. Verify
  }
  
}