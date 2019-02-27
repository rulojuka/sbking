package gui;

import core.*;
import javax.swing.*;
import java.util.*;

public class NegVazas extends JFrame {

	// Constants

	private final int NUMBER_OF_HANDS = 4;
	private final int SIZE_OF_HAND = 13;
	private final int BETWEEN_CARDS_WIDTH = 26; /* 26 is good. 12 pixels to hide pictures */
	private final int WIDTH = 1024;
	private final int HEIGHT = 768;
	private final int B_X = 10;
	private final int B_Y = 600;
	private final int NORTH_X = 1024 / 2 - BETWEEN_CARDS_WIDTH * 7; /* Ideal would be 6,5 (half of the cards) */
	private final int SOUTH_X = NORTH_X;
	private final int EAST_X = NORTH_X + 300;
	private final int WEST_X = NORTH_X - 300;
	private final int NORTH_Y = 120;
	private final int SOUTH_Y = 470;
	private final int EAST_Y = 300;
	private final int WEST_Y = EAST_Y;
	private final int CARD_WIDTH = 72;
	private final int CARD_HEIGHT = 96;
	private final java.awt.Color TABLE_COLOR = new java.awt.Color(0, 100, 0);
	private final java.awt.Color TURN_LIGHT_COLOR = new java.awt.Color(255, 0, 0);

	// Models

	private Board board;

	// Card and turn light positioning arrays

	public int[] initial_x = new int[4];
	public int[] initial_y = new int[4];
	public int[] light_x = new int[4];
	public int[] light_y = new int[4];

	// Swing items

	private JButton[] trickCardsButtons = new JButton[4];
	private JButton[] trickButtons = new JButton[4];
	private JButton[][] buttons = new JButton[NUMBER_OF_HANDS][SIZE_OF_HAND]; /* Don't know how not to use this. */
	private JButton[][] handButtons = new JButton[NUMBER_OF_HANDS][SIZE_OF_HAND];

	javax.swing.JButton drawNewHandButton = new javax.swing.JButton();
	private int turn;
	javax.swing.JButton turnLight = new javax.swing.JButton();
	javax.swing.JButton scoreboardButton = new javax.swing.JButton();

	public static void main(String args[]) {
		NegVazas negVazas = new NegVazas();
		negVazas.init();
		negVazas.setVisible(true);
		negVazas.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void init() {

		setInitialHandPositioning();

		setInitialTurnLightPositioning();

		initializeContentPane();

		/* Cards */
		for (int k = 0; k < NUMBER_OF_HANDS; k++) {
			for (int i = SIZE_OF_HAND - 1; i >= 0; i--) { /* Only way I figured to paint in order */
				buttons[k][i] = new JButton();

				buttons[k][i].setFocusPainted(false);
				buttons[k][i].setRolloverEnabled(false); /* Does not bring up the focused button */
				buttons[k][i].setBorderPainted(false); /* Does not paint the border */
				buttons[k][i].setContentAreaFilled(false); /* Paint always in the same order??? */

				buttons[k][i].setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
				buttons[k][i].setToolTipText("This is a card.");
				getContentPane().add(buttons[k][i]);
				buttons[k][i].setBounds(initial_x[k] + i * BETWEEN_CARDS_WIDTH, initial_y[k], CARD_WIDTH, CARD_HEIGHT);
				// JLabel array mapping
				handButtons[k][i] = buttons[k][i]; /*
													 * Really didn't understand the need of this line but break without
													 * it
													 */
			}
		}

		/* Trick */
		int center_x = WIDTH / 2;
		int center_y = HEIGHT / 2;
		int[] trick_x = new int[4];
		int[] trick_y = new int[4];
		trick_x[0] = trick_x[2] = center_x - CARD_WIDTH / 2;
		trick_x[1] = center_x + 30 - CARD_WIDTH / 2;
		trick_x[3] = center_x - 30 - CARD_WIDTH / 2;

		trick_y[1] = trick_y[3] = center_y - CARD_HEIGHT / 2;
		trick_y[0] = center_y - 30 - CARD_HEIGHT / 2;
		trick_y[2] = center_y + 30 - CARD_HEIGHT / 2;

		for (int i = 0; i < 4; i++)
			trick_y[i] -= 40;

		for (int i = 0; i < 4; i++) {
			trickCardsButtons[i] = new JButton();

			trickCardsButtons[i].setFocusPainted(false);
			trickCardsButtons[i].setRolloverEnabled(false); /* Does not bring up the focused button */
			trickCardsButtons[i].setBorderPainted(false); /* Does not paint the border */
			trickCardsButtons[i].setContentAreaFilled(false); /* Paint always in the same order??? */

			trickCardsButtons[i].setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
			trickCardsButtons[i].setToolTipText("This is a card.");
			getContentPane().add(trickCardsButtons[i]);
			trickCardsButtons[i].setBounds(trick_x[i], trick_y[i], CARD_WIDTH, CARD_HEIGHT);
			// JLabel array mapping
			trickButtons[i] = trickCardsButtons[i]; /*
													 * Really didn't understand the need of this line but break without
													 * it
													 */
		}

		initializeBoard();
		
		turn = board.getCurrentPlayer().toInt();
		turnLight.setBounds(initial_x[turn] - 10, initial_y[turn] - 10,
				(SIZE_OF_HAND - 1) * BETWEEN_CARDS_WIDTH + CARD_WIDTH + 10 + 10, CARD_HEIGHT + 20);
		// turn_light.setBorderPainted(true);
		turnLight.setRolloverEnabled(false);
		turnLight.setBackground(TURN_LIGHT_COLOR);
		turnLight.setOpaque(true);
		turnLight.setContentAreaFilled(true);
		getContentPane().add(turnLight);

		/* Other buttons */
		drawNewHandButton.setText("Draw new hand");
		drawNewHandButton.setActionCommand("Draw new hand");
		getContentPane().add(drawNewHandButton);
		drawNewHandButton.setBounds(WIDTH / 2 - 130, HEIGHT - 80, 260, 30);
		/* Scoreboard button */
		scoreboardButton.setText("NS:" + board.getNorthSouthTricks() + " EW:" + board.getEastWestTricks());
		getContentPane().add(scoreboardButton);
		scoreboardButton.setBounds(WIDTH - 150, 10, 120, 120);

		
		display();

		/* Listeners */

		DrawAndSortActionListener drawNewHandActionListener = new DrawAndSortActionListener();
		PlayCardActionListener playCardActionListener = new PlayCardActionListener();
		drawNewHandButton.addActionListener(drawNewHandActionListener);
		for (int k = 0; k < NUMBER_OF_HANDS; k++) {
			for (int i = 0; i < SIZE_OF_HAND; i++) {
				handButtons[k][i].addActionListener(playCardActionListener);
			}
		}
	}

	private void setInitialHandPositioning() {
		initial_y[0] = NORTH_Y;
		initial_y[1] = EAST_Y;
		initial_y[2] = SOUTH_Y;
		initial_y[3] = WEST_Y;
		initial_x[0] = NORTH_X;
		initial_x[1] = EAST_X;
		initial_x[2] = SOUTH_X;
		initial_x[3] = WEST_X;
	}

	private void setInitialTurnLightPositioning() {
		light_y[0] = NORTH_Y;
		light_y[1] = EAST_Y;
		light_y[2] = SOUTH_Y;
		light_y[3] = WEST_Y;
		light_x[0] = NORTH_X;
		light_x[1] = EAST_X;
		light_x[2] = SOUTH_X;
		light_x[3] = WEST_X;
	}

	private void initializeContentPane() {
		// This line prevents the "Swing: checked access to system event queue" message
		// seen in some browsers.
		getRootPane().putClientProperty("defeatSystemEventQueueCheck", Boolean.TRUE);
		getContentPane().setLayout(null);
		getContentPane().setBackground(TABLE_COLOR);
		setSize(WIDTH, HEIGHT);
	}

	private void initializeBoard() {
		Dealer dealer = new Dealer();
		board = dealer.deal();
		Hand currentHand = board.getNorthHand();
	}

	private boolean isValid(Card c, Hand h) {
		return board.isValid(c, h);
	}

	private void draw() {
		initializeBoard();
		restoreButtons();
	}

	private void playCard(Card card) {
		board.playCard(card);
	}

	private void restoreButtons() {
		for (int k = 0; k < NUMBER_OF_HANDS; k++)
			for (int i = 0; i < SIZE_OF_HAND; i++) {
				handButtons[k][i].setVisible(true);
			}
		for (int i = 0; i < 4; i++) {
			trickButtons[i].setVisible(false);
		}
		scoreboardButton.setText("NS:" + board.getNorthSouthTricks() + " EW:" + board.getEastWestTricks());
	}

	private void display() {
		int numberOfCards;
		int discarded; /* Number of cards already discarded */
		Hand currentHand;
		for (int k = 0; k < NUMBER_OF_HANDS; k++) {
			if(k==0) {
				currentHand = board.getNorthHand();
			}
			else if(k==1) {
				currentHand = board.getEastHand();
			}
			else if(k==2) {
				currentHand = board.getSouthHand();
			}
			else if(k==3) {
				currentHand = board.getWestHand();
			}else {
				throw new RuntimeException("Invalid hand");
			}
			numberOfCards = currentHand.getNumberOfCards();
			discarded = SIZE_OF_HAND - numberOfCards;
			for (int i = 0; i < numberOfCards; i++) {
				Card c = currentHand.getCard(i);
				handButtons[k][i].setIcon(c.getCardImage());
				handButtons[k][i].setBounds(
						initial_x[k] + i * BETWEEN_CARDS_WIDTH + discarded * BETWEEN_CARDS_WIDTH / 2, initial_y[k],
						CARD_WIDTH, CARD_HEIGHT);
			}
		}
		displayTrick();
	}

	private void displayTrick() {
		int cards = board.getCurrentTrick().getNumberOfCards();
		Direction leader = board.getCurrentTrick().getLeader();
		int diff = 0;
		if (leader != null) {
			diff = leader.index() - Direction.NORTH.index();
			diff += 4;
			while (diff > 4)
				diff -= 4;
		}
		for (int i = 0; i < 4; i++) {
			trickButtons[i].setVisible(false);
		}
		for (int i = 0; i < cards; i++) {
			Card c = board.getCurrentTrick().getCard(i);
			trickButtons[(diff + i) % 4].setIcon(c.getCardImage());
			trickButtons[(diff + i) % 4].setVisible(true);
			// System.out.println(myTrick.getCard(i).toString());
		}
//	  System.out.println();
	}

	private void moveTurnLight() {
		turnLight.setBounds(light_x[turn] - 10, light_y[turn] - 10, 20, 20);

	}

	private void displayTurn() {
//		int cards = hands[turn].getNumberOfCards();
//		int discarded = SIZE_OF_HAND - cards;
//		turnLight.setBounds(initial_x[turn] - 10 + discarded * BETWEEN_CARDS_WIDTH / 2, initial_y[turn] - 10,
//				(cards - 1) * BETWEEN_CARDS_WIDTH + CARD_WIDTH + 10 + 10, CARD_HEIGHT + 20);
	}

	class PlayCardActionListener implements java.awt.event.ActionListener {
		public void actionPerformed(java.awt.event.ActionEvent event) {
			Object object = event.getSource();
			for (int k = 0; k < NUMBER_OF_HANDS; k++) {
				for (int i = 0; i < SIZE_OF_HAND; i++) {
					if (k == turn && object == handButtons[k][i]) {
						//Card c = hands[k].getCard(i);
						//playCard(c);

					}
				}
			}
			moveTurnLight();
			displayTurn();
			display();
		}
	}

	class DrawAndSortActionListener implements java.awt.event.ActionListener {
		public void actionPerformed(java.awt.event.ActionEvent event) {
			draw();
			displayTurn();
			display();
		}
	}

}
