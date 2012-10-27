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
   
   public Direction add(int x){
      Direction resp = this;
      if(x<0)
        return resp;
      for(int i=0;i<x;i++){
        resp=resp.next();
      }
      return resp;
   }
   
   public int diff(Direction other){
      int resp = VALUES.indexOf( other ) - VALUES.indexOf( this );
      if(resp<0)
        resp+=4;
      if(resp>=4)
        resp-=4;
      return resp;
   }
   
   public Direction next(){
   		int index = VALUES.indexOf( this ) + 1;
   		if( index >= 4 )
   			index -= 4;
   		return (Direction) VALUES.get(index);
   }

}

