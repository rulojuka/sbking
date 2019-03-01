package core;

public class Card implements Comparable<Card> {
    
   private Suit suitValue;
   private Rank rankValue;

   
   public Card( Suit suit, Rank rank ) {
      suitValue = suit;
      rankValue = rank;
   }

   public Suit getSuit() {
      return suitValue;
   }

   public Rank getRank() {
      return rankValue;
   }

   public String toString() {
      return rankValue.toString() + " of " + suitValue.toString();
   }
   
   public int compareTo( Card otherCard ) { /*Returns <0 if card is bigger than otherCard*/
     int suitDiff = suitValue.compareTo( otherCard.suitValue );
     int rankDiff = rankValue.compareTo( otherCard.rankValue );   
       
     if ( suitDiff != 0 )
        return suitDiff;
     else
        return rankDiff;
   }
   
   public int points(){
   		int points = 0;
   		if( this.getRank().compareTo( Rank.ACE ) == 0)
   			points = 4;
   		if( this.getRank().compareTo( Rank.KING ) == 0)
   			points = 3;
   		if( this.getRank().compareTo( Rank.QUEEN ) == 0)
   			points = 2;
   		if( this.getRank().compareTo( Rank.JACK ) == 0)
   			points = 1; 
   		return points;  			   			   			
   }
   
}
