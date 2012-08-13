package core;

import java.util.*;

public final class Game {
   private String name;
   private String symbol;
   private Suit trump; 
   
   public final static Game TRICKS = new Game( "Negative: Tricks", "V" );
   public final static Game HEARTS = new Game( "Negative: Hearts", "C" );
   public final static Game MEN = new Game( "Negative: Men", "H" );
   public final static Game WOMEN = new Game( "Negative: Women", "M" );
   public final static Game LAST = new Game( "Negative: 2 Last", "2" );
   public final static Game KING = new Game( "Negative: King", "K" );
   
   public final static Game POSI = new Game( "Positive", "+" );
   
   public final static java.util.List VALUES = 
      Collections.unmodifiableList( 
         Arrays.asList( new Game[] { TRICKS, HEARTS, MEN, WOMEN, LAST, KING, POSI } ) );
   
   private Game( String symbol, Suit trumpSuit ) {
   	  if( symbol == "+" ){
   	  	return Posi;
   	  }
      name = nameValue;
      symbol = symbolValue;
   }

   public String getName() {
       return name;
   }
 
   public String getSymbol() {
      return symbol;
   }
    
   public String toString() {
      return name;
   }
   
}

    
