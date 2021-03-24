package br.com.sbk.sbking.gui.painters;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import java.awt.Container;
import java.awt.event.ActionListener;

import br.com.sbk.sbking.core.Board;
import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.rulesets.concrete.NoRuleset;
import br.com.sbk.sbking.gui.elements.AllDirectionBoardElements;
import br.com.sbk.sbking.gui.elements.ScoreSummaryElement;

public class SpectatorPainter implements Painter {

    private ActionListener actionListener;
    private Deal deal;

    public SpectatorPainter(ActionListener actionListener, Deal deal) {
        this.actionListener = actionListener;
        this.deal = deal;
    }

    public SpectatorPainter(ActionListener actionListener, Board board) {
        this.actionListener = actionListener;
        this.deal = new Deal(board, new NoRuleset());
    }

    @Override
    public void paint(Container contentPane) {
        LOGGER.info("Painting spectator that contains this trick: " + deal.getCurrentTrick());
        contentPane.removeAll();

        if (deal.isFinished()) {
            new ScoreSummaryElement(deal, contentPane);
        } else {
            new AllDirectionBoardElements(deal, contentPane, this.actionListener);
        }

        contentPane.validate();
        contentPane.repaint();
    }

}
