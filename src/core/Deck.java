package core;

import java.util.*;
import javax.swing.*;

public class Deck {
   private List<Card> deck;
   private int index;
   private static final int DECK_SIZE = 52;
   //FIXME This next line should not have a hardcoded directory
   public String directory = "/home/rulojuka/workspace/sbking/data/images/cards/";
   
   public Deck() {
        deck = new ArrayList<Card>();
        index = 0;
        Iterator<Suit> suitIterator = Suit.VALUES.iterator();
        while ( suitIterator.hasNext() ) {
            Suit suit = suitIterator.next();
            Iterator<Rank> rankIterator = Rank.VALUES.iterator();
            while ( rankIterator.hasNext() ) {
                Rank rank = rankIterator.next();
                String imageFile = directory + Card.getFilename( suit, rank );
                ImageIcon cardImage = new ImageIcon( imageFile );
                Card card = new Card( suit, rank, cardImage );
                deck.add( card );
            }   
        } 
   }
   
   public Card dealCard() {
      if ( index >= DECK_SIZE )
         return null;
      else
         return deck.get( index++ );
   }
   
   public void shuffle() {
      Collections.shuffle( deck );
   }     

   public void print(){
        index = 0;
        Card card;
        while( (card = this.dealCard()) != null ){
            System.out.print(card.toString() + "\n");
        }
   }
   
   public void restore(){
     index=0;
   }

}
