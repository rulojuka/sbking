package gui.frames;

import java.awt.Container;

import javax.swing.JFrame;

import core.Board;
import core.Dealer;
import core.rulesets.Ruleset;
import gui.JElements.CardButton;
import gui.elements.BoardElements;
import gui.elements.ScoreSummaryElement;

@SuppressWarnings("serial")
public class GameMode extends JFrame {

	// Constants
	private final int WIDTH = 1024;
	private final int HEIGHT = 768;
	private final java.awt.Color TABLE_COLOR = new java.awt.Color(0, 100, 0); // Tablecloth green

	// Model

	private Board board;

	// GUI model

	// private BoardElements boardElements;

	public GameMode(Ruleset ruleset) {
		super();
		initializeJFrame();
		initializeContentPane();
		initializeBoard(ruleset);
		paintBoardElements();
	}

	private void initializeJFrame() {
		this.setVisible(true);
		this.setSize(WIDTH, HEIGHT);
	}

	private void initializeContentPane() {
		getContentPane().setLayout(null);
		getContentPane().setBackground(TABLE_COLOR);
	}

	private void initializeBoard(Ruleset ruleset) {
		Dealer dealer = new Dealer();
		board = dealer.deal(ruleset);
	}

	private void paintBoardElements() {
		Container contentPane = this.getContentPane();
		contentPane.removeAll();
		
		if(board.isFinished()) {
			new ScoreSummaryElement(board, this.getContentPane());
		}else {
			new BoardElements(board, this.getContentPane(), new PlayCardActionListener());
		}


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
