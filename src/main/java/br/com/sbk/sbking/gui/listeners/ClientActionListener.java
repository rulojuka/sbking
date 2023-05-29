package br.com.sbk.sbking.gui.listeners;

import java.util.UUID;

import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.gui.jelements.AcceptClaimButton;
import br.com.sbk.sbking.gui.jelements.CardButton;
import br.com.sbk.sbking.gui.jelements.ClaimButton;
import br.com.sbk.sbking.gui.jelements.JoinTableButton;
import br.com.sbk.sbking.gui.jelements.LeaveTableButton;
import br.com.sbk.sbking.gui.jelements.RejectClaimButton;
import br.com.sbk.sbking.gui.jelements.SitOrLeaveButton;
import br.com.sbk.sbking.gui.jelements.UndoButton;
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
                client.playHttp(card);
            } catch (RuntimeException e) {
                throw e;
            }
        } else if (source instanceof SitOrLeaveButton) {
            SitOrLeaveButton clickedSitOrLeaveButton = (SitOrLeaveButton) source;
            Direction direction = (Direction) clickedSitOrLeaveButton.getClientProperty("direction");
            client.sitOrLeave(direction);
        } else if (source instanceof UndoButton) {
            client.undo();
        } else if (source instanceof ClaimButton) {
            client.claim();
        } else if (source instanceof AcceptClaimButton) {
            client.acceptClaim();
        } else if (source instanceof RejectClaimButton) {
            client.rejectClaim();
        } else if (source instanceof JoinTableButton) {
            JoinTableButton button = (JoinTableButton) source;
            UUID tableId = (UUID) button.getClientProperty("tableId");
            client.joinTable(tableId);
        } else if (source instanceof LeaveTableButton) {
            client.leaveTable();
        }
    }

}
