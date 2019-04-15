package br.com.sbk.sbking.gui.painters;

import java.awt.Container;

import org.apache.log4j.Logger;

import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.gui.JElements.CardButton;
import br.com.sbk.sbking.gui.elements.ScoreSummaryElement;
import br.com.sbk.sbking.gui.elements.SpecificDirectionBoardElements;
import br.com.sbk.sbking.networking.client.NetworkCardPlayer;

public class DealPainter implements Painter {

	final static Logger logger = Logger.getLogger(DealPainter.class);

	private Direction direction;
	private NetworkCardPlayer networkCardPlayer;
	private Deal deal;

	public DealPainter(NetworkCardPlayer networkCardPlayer, Direction direction, Deal deal) {
		this.networkCardPlayer = networkCardPlayer;
		this.direction = direction;
		this.deal = deal;
	}

	@Override
	public void paint(Container contentPane) {
		logger.info("Painting deal that contains this trick: " + deal.getCurrentTrick());
		contentPane.removeAll();

		if (deal.isFinished()) {
			new ScoreSummaryElement(deal, contentPane);
		} else {
			new SpecificDirectionBoardElements(this.direction, deal, contentPane, new PlayCardActionListener());
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
