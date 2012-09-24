package core;

import java.util.*;

public class Hand{
    
   private List hand = new ArrayList();
   public Direction owner;

   public void addCard( Card card ) {
      hand.add( card );
   }

   public Card getCard( int index ) {
      return (Card) hand.get( index );
   }
  
   public Card removeCard( Card card ) {
      int index = hand.indexOf( card );
      if ( index < 0 )
         return null;
      else
         return (Card) hand.remove( index );     
   }

   public Card removeCard( int index ) {
      return (Card) hand.remove( index );
   }

   public void discard() {
      hand.clear();
   }

   public int getNumberOfCards() {
      return hand.size();
   }

   public void sort() {
      Collections.sort( hand );
   }

   public boolean isEmpty() {
      return hand.isEmpty();
   }

   public boolean containsCard( Card card ) {
      return false;
   }

   public int findCard( Card card ) {
      return hand.indexOf( card );
   }
   
   public String toString() {
        return hand.toString();
    }
    
	public void setOwner( Direction direction ){
    	owner = direction;
    }
    
    public String owner(){
    	return owner.toString();
    }

   public void print(){
    	int i;
    	int n;
    	Card auxCard;
    	Rank auxRank;
        Suit auxSuit;
        
    	n = getNumberOfCards();
    	for(i=0;i<n;i++){
    		auxCard = (Card) hand.get(i);
    		auxRank = auxCard.getRank();
    		auxSuit = auxCard.getSuit();
    		if(i!=0)
    			System.out.print(" | ");
    		System.out.print( auxSuit.getSymbol() +  auxRank.getSymbol());
    	}
    	System.out.println();
    }
    
    public int HCP(){
    	int i,n;
    	int resp=0;
    	Card auxCard;
    	n = getNumberOfCards();
    	for(i=0;i<n;i++){
    		auxCard = (Card) hand.get(i);
    		resp+=auxCard.points();
    	}
    	return resp;
    }

}
