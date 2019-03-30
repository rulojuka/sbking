package br.com.sbk.sbking.networking;

import java.awt.Container;

import javax.swing.JFrame;

import org.apache.log4j.Logger;

import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.gui.JElements.CardButton;
import br.com.sbk.sbking.gui.elements.ScoreSummaryElement;
import br.com.sbk.sbking.gui.elements.SpecificDirectionBoardElements;

@SuppressWarnings("serial")
public class NetworkGameMode extends JFrame {

	// Constants
	private static final int WIDTH = 1024;
	private static final int HEIGHT = 768;
	private static final java.awt.Color TABLE_COLOR = new java.awt.Color(0, 100, 0); // Tablecloth green

	final static Logger logger = Logger.getLogger(NetworkGameMode.class);

	// Model

	private Direction direction;
	private NetworkCardPlayer networkCardPlayer;

	// GUI model

	public NetworkGameMode(NetworkCardPlayer networkCardPlayer, Direction direction) {
		super();
		this.networkCardPlayer = networkCardPlayer;
		this.direction = direction;
		initializeJFrame();
		initializeContentPane();
	}

	private void initializeJFrame() {
		this.setVisible(true);
		this.setSize(WIDTH, HEIGHT);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void initializeContentPane() {
		getContentPane().setLayout(null);
		getContentPane().setBackground(TABLE_COLOR);
	}

	public void paintBoardElements(Deal deal) {
		logger.info("Painting deal that contains this trick: " + deal.getCurrentTrick());
		Container contentPane = this.getContentPane();
		contentPane.removeAll();

		if (deal.isFinished()) {
			new ScoreSummaryElement(deal, this.getContentPane());
		} else {
			new SpecificDirectionBoardElements(this.direction, deal, this.getContentPane(),
					new PlayCardActionListener());
		}

		contentPane.validate();
		contentPane.repaint();
	}

	class PlayCardActionListener implements java.awt.event.ActionListener {
		public void actionPerformed(java.awt.event.ActionEvent event) {
			CardButton clickedCardButton = (CardButton) event.getSource();
			Card card = clickedCardButton.getCard();
			try {
				logger.info("Clicked on " + card);
				networkCardPlayer.play(card);
			} catch (RuntimeException e) {
				throw e;
			}
		}
	}

}
