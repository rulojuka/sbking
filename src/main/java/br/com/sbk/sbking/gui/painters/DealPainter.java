package br.com.sbk.sbking.gui.painters;

import java.awt.Container;
import java.awt.event.ActionListener;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.gui.elements.ScoreSummaryElement;
import br.com.sbk.sbking.gui.elements.SpecificDirectionBoardElements;

public class DealPainter implements Painter {

	final static Logger logger = LogManager.getLogger(DealPainter.class);

	private Direction direction;
	private ActionListener actionListener;
	private Deal deal;

	public DealPainter(ActionListener actionListener, Direction direction, Deal deal) {
		this.actionListener = actionListener;
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
			new SpecificDirectionBoardElements(this.direction, deal, contentPane, actionListener);
		}

		contentPane.validate();
		contentPane.repaint();
	}

}
