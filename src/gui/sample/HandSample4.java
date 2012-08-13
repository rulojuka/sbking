import core.*;
import java.awt.*;
import javax.swing.*;
import java.util.*;

public class HandSample4 extends JApplet{
    
  private Deck myDeck;
  private Hand northHand;
  private Hand southHand;
  private Card myCard;
	private final int SIZE_OF_HAND = 13;
	private final int BETWEEN_CARDS_WIDTH = 26; /* 26 is good. 12 pixels to hide pictures*/
	private final int WIDTH = 1024;
	private final int HEIGHT = 768;
	private final int B_X = 10;
	private final int B_Y = 600;
	private final int NORTH_X = 1024/2 - BETWEEN_CARDS_WIDTH * 7; /* Ideal would be 6,5 (half of the cards) */
	private final int SOUTH_X = NORTH_X;
  private final int NORTH_Y = 20;
	private final int SOUTH_Y = 550;
	private final int CARD_WIDTH = 72;
	private final int CARD_HEIGHT = 96;
	
  private JButton[] buttons = new JButton[ SIZE_OF_HAND ]; /*Don't know how not to use this.*/
  private JButton[] northButtons = new JButton[ SIZE_OF_HAND ];
  private JButton[] buttons2 = new JButton[ SIZE_OF_HAND ]; /*Don't know how not to use this.*/
  private JButton[] southButtons = new JButton[ SIZE_OF_HAND ];
    
	javax.swing.JButton JButton1 = new javax.swing.JButton();
	private java.awt.Color TABLE_COLOR = new java.awt.Color(0,100,0);
	
	public void init(){
		
		// This line prevents the "Swing: checked access to system event queue" message seen in some browsers.
		getRootPane().putClientProperty("defeatSystemEventQueueCheck", Boolean.TRUE);
		
		getContentPane().setLayout(null);
		getContentPane().setBackground(TABLE_COLOR);
		setSize(WIDTH,HEIGHT);
		
		/*Cards*/
	  for(int i=SIZE_OF_HAND-1;i>=0;i--){ /*Only way I figured to paint in order*/
		  buttons[i] = new JButton();
		
		  buttons[i].setFocusPainted(false);
	    buttons[i].setRolloverEnabled(false); /*Does not bring up the focused button*/
    	buttons[i].setBorderPainted(false); /*Does not paint the border*/
    	buttons[i].setContentAreaFilled(false); /*Paint always in the same order???*/
		
		  buttons[i].setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		  buttons[i].setToolTipText("This is a card.");
		  getContentPane().add(buttons[i]);		
		  buttons[i].setBounds(NORTH_X + i*BETWEEN_CARDS_WIDTH,NORTH_Y,CARD_WIDTH,CARD_HEIGHT); 
		  // JLabel array mapping
		  northButtons[i] = buttons[i]; /*Really didn't understand the need of this line but break without it */
	  }
	  
	  for(int i=SIZE_OF_HAND-1;i>=0;i--){ /*Only way I figured to paint in order*/
		  buttons2[i] = new JButton();
		
		  buttons2[i].setFocusPainted(false);
	    buttons2[i].setRolloverEnabled(false); /*Does not bring up the focused button*/
    	buttons2[i].setBorderPainted(false); /*Does not paint the border*/
    	buttons2[i].setContentAreaFilled(false); /*Paint always in the same order???*/
		
		  buttons2[i].setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		  buttons2[i].setToolTipText("This is a card.");
		  getContentPane().add(buttons2[i]);		
		  buttons2[i].setBounds(NORTH_X + i*BETWEEN_CARDS_WIDTH,NORTH_Y,CARD_WIDTH,CARD_HEIGHT); 
		  // JLabel array mapping
		  southButtons[i] = buttons2[i]; /*Really didn't understand the need of this line but break without it */
	  }
		
		/*Other buttons*/
		JButton1.setText("Draw and Sort");
		JButton1.setActionCommand("Draw and Sort");
		getContentPane().add(JButton1);
		JButton1.setBounds(WIDTH/2 - 130,HEIGHT - 80,260,30);
		
		/*Initializing Hand*/
		myDeck = new Deck();
		northHand = new Hand();
		southHand = new Hand();
    deal();
		display();
		
		/*Listeners*/	
		SymAction lSymAction = new SymAction();
		JButton1.addActionListener(lSymAction);
		for(int i=0;i<SIZE_OF_HAND;i++){
			northButtons[i].addActionListener(lSymAction);
			southButtons[i].addActionListener(lSymAction);
		}
	}

	class SymAction implements java.awt.event.ActionListener{
		public void actionPerformed(java.awt.event.ActionEvent event){
			Object object = event.getSource();
			if (object == JButton1){
					draw(event);
					sort(event);
			}
			else{
				for(int i=0;i<SIZE_OF_HAND;i++){
					 if (object == northButtons[i]){
						removeNorthCard(event,i);
					 }
					 else if(object == southButtons[i]){
						removeSouthCard(event,i);
					 }
				}
			}
			display();
		}
	}

	private void draw(java.awt.event.ActionEvent event){
	  northHand.discardHand();
	  southHand.discardHand();
		myDeck.restore();
		myDeck.shuffle();
		restoreButtons();
		deal();

	}

	private void sort(java.awt.event.ActionEvent event){
		northHand.sort();
		southHand.sort();
	}
	
	private void removeNorthCard(java.awt.event.ActionEvent event, int id){
		int last = northHand.getNumberOfCards()-1;
		if(last>=id){
			northHand.removeCard(id);
			northButtons[last].setVisible(false);
		}
	}
  private void removeSouthCard(java.awt.event.ActionEvent event, int id){
		int last = southHand.getNumberOfCards()-1;
		if(last>=id){
			southHand.removeCard(id);
			southButtons[last].setVisible(false);
		}
	}
	private void restoreButtons(){
		for(int i=0;i< SIZE_OF_HAND;i++){
			northButtons[i].setVisible(true);
			southButtons[i].setVisible(true);
	    }
	}
	
	private void deal(){
		Card card;
		for(int i=0;i< SIZE_OF_HAND;i++){
        	card = myDeck.dealCard();
        	northHand.addCard( card );
	    }
	  for(int i=0;i< SIZE_OF_HAND;i++){
        	card = myDeck.dealCard();
        	southHand.addCard( card );
	    }
	}
	
	private void display(){
		int cards = northHand.getNumberOfCards();
		int discarded; /*Number of cards already discarded*/
		discarded = SIZE_OF_HAND - cards;
	   for (int i = 0; i <cards; i++ ) {
		    Card c = northHand.getCard( i );
		    northButtons[i].setIcon( c.getCardImage() );
		    northButtons[i].setBounds(NORTH_X + i*BETWEEN_CARDS_WIDTH + discarded*BETWEEN_CARDS_WIDTH/2,NORTH_Y,CARD_WIDTH,CARD_HEIGHT);
		}
		
		cards = southHand.getNumberOfCards();
		discarded = SIZE_OF_HAND - cards;
	   for (int i = 0; i <cards; i++ ) {
		    Card c = southHand.getCard( i );
		    southButtons[i].setIcon( c.getCardImage() );
		    southButtons[i].setBounds(SOUTH_X + i*BETWEEN_CARDS_WIDTH + discarded*BETWEEN_CARDS_WIDTH/2,SOUTH_Y,CARD_WIDTH,CARD_HEIGHT);
		}
	}
	
}

