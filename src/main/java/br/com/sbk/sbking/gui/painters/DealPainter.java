package br.com.sbk.sbking.gui.painters;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import java.awt.Container;
import java.awt.event.ActionListener;

import br.com.sbk.sbking.core.Board;
import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.core.rulesets.concrete.NoRuleset;
import br.com.sbk.sbking.gui.elements.SpecificDirectionBoardElements;

public class DealPainter implements Painter {

    protected Direction direction;
    protected ActionListener actionListener;
    protected Deal deal;

    public DealPainter(ActionListener actionListener, Direction direction, Deal deal) {
        this.actionListener = actionListener;
        this.direction = direction;
        this.deal = deal;
    }

    public DealPainter(ActionListener actionListener, Direction direction, Board board) {
        this.actionListener = actionListener;
        this.direction = direction;
        this.deal = new Deal(board, new NoRuleset(), null, null);
    }

    @Override
    public void paint(Container contentPane) {
        LOGGER.info("Painting deal that contains this trick: " + deal.getCurrentTrick());
        contentPane.removeAll();

        // FIXME This should be uncommented when there is a good way to show the last
        // card for a second before showing the scoresummary
        // if (deal.isFinished()) {
        // new ScoreSummaryElement(deal, contentPane);
        // } else {
        new SpecificDirectionBoardElements(this.direction, deal, contentPane, actionListener);
        // }

        contentPane.validate();
        contentPane.repaint();
    }

}
