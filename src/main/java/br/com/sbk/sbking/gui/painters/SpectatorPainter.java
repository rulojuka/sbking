package br.com.sbk.sbking.gui.painters;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import java.awt.Container;
import java.awt.event.ActionListener;
import java.util.List;

import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.gui.elements.AllDirectionBoardElements;
import br.com.sbk.sbking.gui.elements.ScoreSummaryElement;

public class SpectatorPainter implements Painter {

    private ActionListener actionListener;
    private Deal deal;
    private List<String> spectators;

    public SpectatorPainter(ActionListener actionListener, Deal deal, List<String> spectators) {
        this.actionListener = actionListener;
        this.deal = deal;
        this.spectators = spectators;
    }

    @Override
    public void paint(Container contentPane) {
        LOGGER.trace("Painting spectator that contains this trick: " + deal.getCurrentTrick());
        contentPane.removeAll();

        if (deal.isFinished()) {
            new ScoreSummaryElement(deal, contentPane);
        } else {
            new AllDirectionBoardElements(deal, contentPane, this.actionListener, this.spectators);
        }

        contentPane.validate();
        contentPane.repaint();
    }

}
