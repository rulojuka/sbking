package br.com.sbk.sbking.gui.painters;

import java.awt.Container;

import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.CompleteDealDealer;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.core.rulesets.abstractClasses.Ruleset;
import br.com.sbk.sbking.gui.JElements.CardButton;
import br.com.sbk.sbking.gui.elements.BoardElements;
import br.com.sbk.sbking.gui.elements.ScoreSummaryElement;
import br.com.sbk.sbking.gui.frames.GameScreen;

public class GameModePainter {

	private Deal deal;
	private Container lastContentPane;

	public GameModePainter(GameScreen gameScreen, Ruleset ruleset) {
		CompleteDealDealer dealer = new CompleteDealDealer(Direction.NORTH);
		deal = dealer.deal(ruleset);
	}

	public void paint(Container contentPane) {
		this.lastContentPane = contentPane;
		contentPane.removeAll();

		if (deal.isFinished()) {
			new ScoreSummaryElement(deal, contentPane);
		} else {
			new BoardElements(deal, contentPane, new PlayCardActionListener());
		}

		contentPane.validate();
		contentPane.repaint();
	}

	private void repaint() {
		paint(this.lastContentPane);
	}

	class PlayCardActionListener implements java.awt.event.ActionListener {
		public void actionPerformed(java.awt.event.ActionEvent event) {
			CardButton clickedCardButton = (CardButton) event.getSource();
			try {
				deal.playCard(clickedCardButton.getCard());
				repaint();
			} catch (RuntimeException e) {
				throw e;
			}
		}
	}

}
