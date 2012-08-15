import core.*;
import java.awt.*;
import javax.swing.*;
import java.util.*;

public class HandSample extends JFrame {

  private Deck myDeck;
  private Hand myHand;
  private Card myCard;
	private final int SIZE_OF_HAND = 13;
	private final int BETWEEN_CARDS_WIDTH = 26; /* 26 is good. 12 pixels to hide pictures*/
	private final int WIDTH = 450;
	private final int HEIGHT = 235;

  private JButton[] cardButtons = new JButton[ SIZE_OF_HAND ];
  private JButton[] buttons = new JButton[ SIZE_OF_HAND ]; /*Don't know how not to use this.*/

	javax.swing.JButton JButton1 = new javax.swing.JButton();
	javax.swing.JButton JButton2 = new javax.swing.JButton();
	javax.swing.JButton JButton3 = new javax.swing.JButton();
	javax.swing.JButton JButton4 = new javax.swing.JButton();
	javax.swing.JLabel scoreLbl = new javax.swing.JLabel();
	
	public static void main (String args[]) {
	  HandSample sample = new HandSample();
	  sample.init();
	  sample.setVisible(true);
    sample.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void init(){
		
		// This line prevents the "Swing: checked access to system event queue" message seen in some browsers.
		getRootPane().putClientProperty("defeatSystemEventQueueCheck", Boolean.TRUE);
		getContentPane().setLayout(null);
    getContentPane().setBackground(java.awt.Color.cyan);
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
			buttons[i].setBounds(12 + i*BETWEEN_CARDS_WIDTH,12,72,96); 
			// JLabel array mapping
			cardButtons[i] = buttons[i]; /*Really didn't understand the need of this line but break without it */
		}
		
		/*Other buttons*/
		JButton1.setText("Draw a Hand");
		JButton1.setActionCommand("Draw a Hand");
		getContentPane().add(JButton1);
		JButton1.setBounds(10,156,130,30);
		
		JButton2.setText("Sort Hand");
		JButton2.setActionCommand("Sort the Hand");
		getContentPane().add(JButton2);
		JButton2.setBounds(140,156,130,30);
		
		JButton3.setText("Draw and Sort");
		JButton3.setActionCommand("Draw and Sort");
		getContentPane().add(JButton3);
		JButton3.setBounds(10,120,260,30);
		
		scoreLbl.setToolTipText("Using High Card Points");
		getContentPane().add(scoreLbl);
		scoreLbl.setForeground(java.awt.Color.black);
		scoreLbl.setBounds(300,120,110,29);
		
		/*Initializing Hand*/
		myDeck = new Deck();
		myHand = new Hand();
    deal();
		display();
		
		/*Listeners*/	
		SymAction lSymAction = new SymAction();
		JButton1.addActionListener(lSymAction);
		JButton2.addActionListener(lSymAction);
		JButton3.addActionListener(lSymAction);
		for(int i=0;i<SIZE_OF_HAND;i++){
			cardButtons[i].addActionListener(lSymAction);
		}
	}

	class SymAction implements java.awt.event.ActionListener{
		public void actionPerformed(java.awt.event.ActionEvent event){
			Object object = event.getSource();
			if (object == JButton1)
				draw(event);
			else if (object == JButton2)
				sort(event);
			else if (object == JButton3){
					draw(event);
					sort(event);
			}
			else{
				for(int i=0;i<SIZE_OF_HAND;i++){
					 if (object == cardButtons[i])
						removeCard(event,i);
				}
			}
			display();
		}
	}

	private void draw(java.awt.event.ActionEvent event){
	    myHand.discardHand();
		myDeck.restore();
		myDeck.shuffle();
		restoreButtons();
		deal();

	}

	private void sort(java.awt.event.ActionEvent event){
		myHand.sort();
	}
	
	private void removeCard(java.awt.event.ActionEvent event, int id){
		int last = myHand.getNumberOfCards()-1;
		if(last>=id){
			myHand.removeCard(id);
			cardButtons[last].setVisible(false);
		}
	}
	private void restoreButtons(){
		for(int i=0;i< SIZE_OF_HAND;i++){
			cardButtons[i].setVisible(true);
	    }
	}
	
	private void deal(){
		Card card;
		for(int i=0;i< SIZE_OF_HAND;i++){
        	card = myDeck.dealCard();
        	myHand.addCard( card );
	    }
	}
	
	private void display(){
		int cards = myHand.getNumberOfCards();
		int discarded; /*Number of cards already discarded*/
		discarded = SIZE_OF_HAND - cards;
	   for (int i = 0; i <cards; i++ ) {
		    Card c = myHand.getCard( i );
		    cardButtons[i].setIcon( c.getCardImage() );
		    cardButtons[i].setBounds(12 + i*BETWEEN_CARDS_WIDTH + discarded*BETWEEN_CARDS_WIDTH/2,12,72,96);
		}
		scoreLbl.setText( "Total HCP: " + myHand.HCP());
	}
	
}

