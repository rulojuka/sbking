package br.com.sbk.sbking.gui.frames;

import java.awt.Container;

import javax.swing.JFrame;

import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Dealer;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.core.rulesets.abstractClasses.Ruleset;
import br.com.sbk.sbking.gui.JElements.CardButton;
import br.com.sbk.sbking.gui.elements.BoardElements;
import br.com.sbk.sbking.gui.elements.ScoreSummaryElement;

@SuppressWarnings("serial")
public class GameMode extends JFrame {

	// Constants
	private static final int WIDTH = 1024;
	private static final int HEIGHT = 768;
	private static final java.awt.Color TABLE_COLOR = new java.awt.Color(0, 100, 0); // Tablecloth green

	// Model

	private Deal deal;

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
		Dealer dealer = new Dealer(Direction.NORTH);
		deal = dealer.deal(ruleset);
	}

	private void paintBoardElements() {
		Container contentPane = this.getContentPane();
		contentPane.removeAll();

		if (deal.isFinished()) {
			new ScoreSummaryElement(deal, this.getContentPane());
		} else {
			new BoardElements(deal, this.getContentPane(), new PlayCardActionListener());
		}

		contentPane.validate();
		contentPane.repaint();
	}

	class PlayCardActionListener implements java.awt.event.ActionListener {
		public void actionPerformed(java.awt.event.ActionEvent event) {
			CardButton clickedCardButton = (CardButton) event.getSource();
			try {
				deal.playCard(clickedCardButton.getCard());
				paintBoardElements();
			} catch (RuntimeException e) {
				throw e;
			}
		}
	}

}
