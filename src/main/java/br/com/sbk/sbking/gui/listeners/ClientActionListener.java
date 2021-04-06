package br.com.sbk.sbking.gui.listeners;

import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.gui.JElements.CardButton;
import br.com.sbk.sbking.gui.JElements.UndoButton;
import br.com.sbk.sbking.gui.JElements.SitOrLeaveButton;
import br.com.sbk.sbking.networking.kryonet.KryonetSBKingClientActionListener;

public class ClientActionListener implements java.awt.event.ActionListener {

    private KryonetSBKingClientActionListener client;

    public ClientActionListener(KryonetSBKingClientActionListener client) {
        super();
        this.client = client;
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent event) {
        Object source = event.getSource();

        if (source instanceof CardButton) {
            CardButton clickedCardButton = (CardButton) source;
            Card card = clickedCardButton.getCard();
            try {
                client.play(card);
            } catch (RuntimeException e) {
                throw e;
            }
        } else if (source instanceof SitOrLeaveButton) {
            SitOrLeaveButton clickedSitOrLeaveButton = (SitOrLeaveButton) source;
            Direction direction = (Direction) clickedSitOrLeaveButton.getClientProperty("direction");
            client.sitOrLeave(direction);
        } else if (source instanceof UndoButton) {
            client.undo();
        }
    }

}
