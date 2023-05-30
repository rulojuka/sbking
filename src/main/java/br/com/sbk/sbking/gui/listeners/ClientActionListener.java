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
import br.com.sbk.sbking.networking.rest.RestHTTPClient;

public class ClientActionListener implements java.awt.event.ActionListener {

    private RestHTTPClient restClient;

    public ClientActionListener(RestHTTPClient restClient) {
        super();
        this.restClient = restClient;
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent event) {
        Object source = event.getSource();

        if (source instanceof CardButton) {
            CardButton clickedCardButton = (CardButton) source;
            Card card = clickedCardButton.getCard();
            try {
                restClient.play(card);
            } catch (RuntimeException e) {
                throw e;
            }
        } else if (source instanceof SitOrLeaveButton) {
            SitOrLeaveButton clickedSitOrLeaveButton = (SitOrLeaveButton) source;
            Direction direction = (Direction) clickedSitOrLeaveButton.getClientProperty("direction");
            restClient.moveToSeat(direction);
        } else if (source instanceof UndoButton) {
            restClient.undo();
        } else if (source instanceof ClaimButton) {
            restClient.claim();
        } else if (source instanceof AcceptClaimButton) {
            restClient.handleClaim(true);
        } else if (source instanceof RejectClaimButton) {
            restClient.handleClaim(false);
        } else if (source instanceof JoinTableButton) {
            JoinTableButton button = (JoinTableButton) source;
            UUID tableId = (UUID) button.getClientProperty("tableId");
            restClient.sendJoinTableMessage(tableId);
        } else if (source instanceof LeaveTableButton) {
            restClient.leaveTable();
        }
    }

}
