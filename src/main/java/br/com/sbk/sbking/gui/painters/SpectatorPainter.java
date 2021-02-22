package br.com.sbk.sbking.gui.painters;

import java.awt.Container;
import java.awt.event.ActionListener;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.gui.elements.AllDirectionBoardElements;
import br.com.sbk.sbking.gui.elements.ScoreSummaryElement;

public class SpectatorPainter implements Painter {

	final static Logger logger = LogManager.getLogger(SpectatorPainter.class);

	private ActionListener actionListener;
	private Deal deal;

	public SpectatorPainter(ActionListener actionListener, Deal deal) {
		this.actionListener = actionListener;
		this.deal = deal;
	}

	@Override
	public void paint(Container contentPane) {
		logger.info("Painting spectator that contains this trick: " + deal.getCurrentTrick());
		contentPane.removeAll();

		if (deal.isFinished()) {
			new ScoreSummaryElement(deal, contentPane);
		} else {
			new AllDirectionBoardElements(deal, contentPane, actionListener);
		}

		contentPane.validate();
		contentPane.repaint();
	}

}
