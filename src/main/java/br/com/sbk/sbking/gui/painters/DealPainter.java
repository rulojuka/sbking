package br.com.sbk.sbking.gui.painters;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import java.awt.Container;
import java.awt.event.ActionListener;
import java.util.List;

import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.gui.elements.SpecificDirectionBoardElements;

public class DealPainter implements Painter {

    protected Direction direction;
    protected ActionListener actionListener;
    protected Deal deal;
    protected List<String> spectators;

    public DealPainter(ActionListener actionListener, Direction direction, Deal deal, List<String> spectators) {
        this.actionListener = actionListener;
        this.direction = direction;
        this.deal = deal;
        this.spectators = spectators;
    }

    @Override
    public void paint(Container contentPane) {
        LOGGER.trace("Painting deal that contains this trick: " + deal.getCurrentTrick());
        contentPane.removeAll();

        // FIXME This should be uncommented when there is a good way to show the last
        // card for a second before showing the scoresummary
        // if (deal.isFinished()) {
        // new ScoreSummaryElement(deal, contentPane);
        // } else {
        new SpecificDirectionBoardElements(this.direction, deal, contentPane, actionListener, this.spectators);
        // }

        contentPane.validate();
        contentPane.repaint();
    }

}
