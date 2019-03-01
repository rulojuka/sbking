package gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import core.Board;
import core.Card;
import core.Dealer;
import core.Deck;
import core.Direction;
import core.Hand;

@SuppressWarnings("serial")
public class NegVazas extends JFrame {

	// Constants

	private final int NUMBER_OF_HANDS = 4;
	private final int SIZE_OF_HAND = 13;
	private final int BETWEEN_CARDS_WIDTH = 26; /* 26 is good. 12 pixels to hide pictures */
	private final int WIDTH = 1024;
	private final int HEIGHT = 768;
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
	
	
	public int[] light_x = new int[4];
	public int[] light_y = new int[4];

	// Swing items

	private List<CardButton> trickCardButtons = new ArrayList<CardButton>();
	private List<CardButton> northHandButtons = new ArrayList<CardButton>();
	private List<CardButton> eastHandButtons = new ArrayList<CardButton>();
	private List<CardButton> southHandButtons = new ArrayList<CardButton>();
	private List<CardButton> westHandButtons = new ArrayList<CardButton>();

	javax.swing.JButton drawNewHandButton = new javax.swing.JButton();
	private int turn;
	javax.swing.JButton turnLight = new javax.swing.JButton();
	javax.swing.JButton scoreboardButton = new javax.swing.JButton();
	private DeckOfCardButtons deckOfCardButtons;

	public static void main(String args[]) {
		NegVazas negVazas = new NegVazas();
		negVazas.init();
		negVazas.setVisible(true);
		negVazas.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void init() {

		setInitialTurnLightPositioning();

		initializeContentPane();

		createDeckOfCardButtons();
		displayDeckOfCardButtons();


		/* Trick */
//		int center_x = WIDTH / 2;
//		int center_y = HEIGHT / 2;
//		int[] trick_x = new int[4];
//		int[] trick_y = new int[4];
//		trick_x[0] = trick_x[2] = center_x - CARD_WIDTH / 2;
//		trick_x[1] = center_x + 30 - CARD_WIDTH / 2;
//		trick_x[3] = center_x - 30 - CARD_WIDTH / 2;
//
//		trick_y[1] = trick_y[3] = center_y - CARD_HEIGHT / 2;
//		trick_y[0] = center_y - 30 - CARD_HEIGHT / 2;
//		trick_y[2] = center_y + 30 - CARD_HEIGHT / 2;
//
//		for (int i = 0; i < 4; i++)
//			trick_y[i] -= 40;
//
//		for (int i = 0; i < 4; i++) {
//			trickCardsButtons[i] = new JButton();
//
//			trickCardsButtons[i].setFocusPainted(false);
//			trickCardsButtons[i].setRolloverEnabled(false); /* Does not bring up the focused button */
//			trickCardsButtons[i].setBorderPainted(false); /* Does not paint the border */
//			trickCardsButtons[i].setContentAreaFilled(false); /* Paint always in the same order??? */
//
//			trickCardsButtons[i].setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
//			trickCardsButtons[i].setToolTipText("This is a card.");
//			getContentPane().add(trickCardsButtons[i]);
//			trickCardsButtons[i].setBounds(trick_x[i], trick_y[i], CARD_WIDTH, CARD_HEIGHT);
//			// JLabel array mapping
//			trickButtons[i] = trickCardsButtons[i]; /*
//													 * Really didn't understand the need of this line but break without
//													 * it
//													 */
//		}

		initializeBoard();

		turn = board.getCurrentPlayer().toInt();
//		turnLight.setBounds(initial_x[turn] - 10, initial_y[turn] - 10,
//				(SIZE_OF_HAND - 1) * BETWEEN_CARDS_WIDTH + CARD_WIDTH + 10 + 10, CARD_HEIGHT + 20);
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

		displayHands();

		/* Listeners */

//		DrawAndSortActionListener drawNewHandActionListener = new DrawAndSortActionListener();
//		PlayCardActionListener playCardActionListener = new PlayCardActionListener();
//		drawNewHandButton.addActionListener(drawNewHandActionListener);
//		for (int k = 0; k < NUMBER_OF_HANDS; k++) {
//			for (int i = 0; i < SIZE_OF_HAND; i++) {
//				handButtons[k][i].addActionListener(playCardActionListener);
//			}
//		}
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
	}

	private void restoreButtons() {
//		for (int k = 0; k < NUMBER_OF_HANDS; k++)
//			for (int i = 0; i < SIZE_OF_HAND; i++) {
//				handButtons[k][i].setVisible(true);
//			}
//		for (int i = 0; i < 4; i++) {
//			trickButtons[i].setVisible(false);
//		}
//		scoreboardButton.setText("NS:" + board.getNorthSouthTricks() + " EW:" + board.getEastWestTricks());
	}

	private void refreshScreen() {
		displayHands();
		displayTrick();
		displayTurnLight();
	}

	private void createDeckOfCardButtons() {	
		Deck deck = new Deck();
		deck.shuffle();
		this.deckOfCardButtons = new DeckOfCardButtons(deck, new PlayCardActionListener());
	}

	private void displayDeckOfCardButtons() {
		this.deckOfCardButtons.addAllCardButtonsToPane(this.getContentPane());
		
	}
	private void displayHands() {
		int numberOfCards;
		int discarded; /* Number of cards already discarded */
		Hand currentHand;
		for (int k = 0; k < NUMBER_OF_HANDS; k++) {
			if (k == 0) {
				currentHand = board.getNorthHand();
			} else if (k == 1) {
				currentHand = board.getEastHand();
			} else if (k == 2) {
				currentHand = board.getSouthHand();
			} else if (k == 3) {
				currentHand = board.getWestHand();
			} else {
				throw new RuntimeException("Invalid hand");
			}

			numberOfCards = currentHand.getNumberOfCards();
			discarded = SIZE_OF_HAND - numberOfCards;
//			for (int i = 0; i < numberOfCards; i++) {
//				Card c = currentHand.getCard(i);
//				handButtons[k][i].setIcon(c.getCardImage());
//				handButtons[k][i].setBounds(
//						initial_x[k] + i * BETWEEN_CARDS_WIDTH + discarded * BETWEEN_CARDS_WIDTH / 2, initial_y[k],
//						CARD_WIDTH, CARD_HEIGHT);
//			}

		}
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
//			trickButtons[i].setVisible(false);
		}
		for (int i = 0; i < cards; i++) {
			Card c = board.getCurrentTrick().getCard(i);
//			trickButtons[(diff + i) % 4].setIcon(c.getCardImage());
//			trickButtons[(diff + i) % 4].setVisible(true);
		}
	}

	private void displayTurnLight() {
//		Hand handOfCurrentPlayer = board.getHandOfCurrentPlayer();
//		int cards = handOfCurrentPlayer.getNumberOfCards();
//		int discarded = SIZE_OF_HAND - cards;
//		turnLight.setBounds(initial_x[turn] - 10 + discarded * BETWEEN_CARDS_WIDTH / 2, initial_y[turn] - 10,
//				(cards - 1) * BETWEEN_CARDS_WIDTH + CARD_WIDTH + 10 + 10, CARD_HEIGHT + 20);
	}

	class PlayCardActionListener implements java.awt.event.ActionListener {
		public void actionPerformed(java.awt.event.ActionEvent event) {
			CardButton clickedCardButton = (CardButton) event.getSource();
			clickedCardButton.flip();
		}
	}

}
