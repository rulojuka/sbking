package core;

import java.util.*;
import javax.swing.*;
import java.awt.*;

public class Deck {
   private java.util.List deck;
   private int index;
   private static final int DECK_SIZE = 52;
   public String directory = "/home/users/rulojuka/sbking/data/images/cards/";
   
   public Deck() {
        deck = new ArrayList();
        index = 0;
        Iterator suitIterator = Suit.VALUES.iterator();
        while ( suitIterator.hasNext() ) {
            Suit suit = (Suit) suitIterator.next();
            Iterator rankIterator = Rank.VALUES.iterator();
            while ( rankIterator.hasNext() ) {
                Rank rank = (Rank) rankIterator.next();
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
         return (Card) deck.get( index++ );
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

