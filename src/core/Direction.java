package core;

import java.util.*;

public final class Direction{
   private String direction;
   
   public final static Direction NORTH = new Direction( "North" );
   public final static Direction EAST = new Direction( "East" );
   public final static Direction SOUTH = new Direction( "South" );
   public final static Direction WEST = new Direction( "West" );
   
   public final static java.util.List VALUES = 
      Collections.unmodifiableList( 
         Arrays.asList( new Direction[] { NORTH, EAST, SOUTH, WEST } ) );
   
   private Direction( String directionValue ) {
      direction = directionValue;
   }

   public String getDirection() {
       return direction;
   }
    
   public String toString() {
       return direction;
   }
   
   public Direction next(){
   		int index = VALUES.indexOf( this ) + 1;
   		if( index >= 4 )
   			index -= 4;
   		return (Direction) VALUES.get(index);
   }

}

