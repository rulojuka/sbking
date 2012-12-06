package core;

import javax.swing.*;

public class Card implements Comparable {
    
   private Suit suitValue;
   private Rank rankValue;
   private ImageIcon cardImage;
   
   public Card( Suit suit, Rank rank, ImageIcon cardFace ) {
      cardImage = cardFace;
      suitValue = suit;
      rankValue = rank;
   }
    
   public static String getFilename( Suit suit, Rank rank ) {
      return suit.getSymbol() + rank.getSymbol() + ".png";
   }

   public Suit getSuit() {
      return suitValue;
   }

   public Rank getRank() {
      return rankValue;
   }

   public ImageIcon getCardImage() {
	  return cardImage;
   }

   public String toString() {
      return rankValue.toString() + " of " + suitValue.toString();
   }
  
   public String rankToString() {
      return rankValue.toString();
   }

   public String suitToString() {
      return suitValue.toString();
   }
   
   public int compareTo( Object otherCardObject ) {
     Card otherCard = (Card) otherCardObject;
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
