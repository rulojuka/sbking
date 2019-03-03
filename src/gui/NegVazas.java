package gui;

import java.awt.Container;

import javax.swing.JFrame;

import core.Board;
import core.Dealer;

@SuppressWarnings("serial")
public class NegVazas extends JFrame {

	// Constants
	private final int WIDTH = 1024;
	private final int HEIGHT = 768;
	private final java.awt.Color TABLE_COLOR = new java.awt.Color(0, 100, 0); // Tablecloth green

	// Model

	private Board board;

	// GUI model

	// private BoardElements boardElements;

	public NegVazas() {
		super();
		this.init();
	}

	public void init() {
		initializeJFrame();
		initializeContentPane();
		initializeBoard();
		paintBoardElements();
	}

	private void initializeJFrame() {
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(WIDTH, HEIGHT);
	}

	private void initializeContentPane() {
		getContentPane().setLayout(null);
		getContentPane().setBackground(TABLE_COLOR);
	}

	private void initializeBoard() {
		Dealer dealer = new Dealer();
		board = dealer.deal();
	}

	private void paintBoardElements() {
		Container contentPane = this.getContentPane();
		contentPane.removeAll();
		
		new BoardElements(board, this.getContentPane(), new PlayCardActionListener());
		
		contentPane.validate();
		contentPane.repaint();
	}

	class PlayCardActionListener implements java.awt.event.ActionListener {
		public void actionPerformed(java.awt.event.ActionEvent event) {
			CardButton clickedCardButton = (CardButton) event.getSource();
			try {
				board.playCard(clickedCardButton.getCard());
				paintBoardElements();
			} catch (RuntimeException e) {
				throw e;
			}
		}
	}

}
