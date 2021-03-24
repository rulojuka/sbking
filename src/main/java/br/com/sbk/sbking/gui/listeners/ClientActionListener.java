package br.com.sbk.sbking.gui.listeners;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.gui.JElements.CardButton;
import br.com.sbk.sbking.gui.JElements.SitOrLeaveButton;
import br.com.sbk.sbking.networking.client.ClientToServerMessageSender;

public class ClientActionListener implements java.awt.event.ActionListener {

    private ClientToServerMessageSender networkMessageSender;

    public ClientActionListener(ClientToServerMessageSender networkCardPlayer) {
        super();
        this.networkMessageSender = networkCardPlayer;
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent event) {

        Object source = event.getSource();
        LOGGER.info("Performing network action: " + source);

        if (source instanceof CardButton) {
            CardButton clickedCardButton = (CardButton) source;
            Card card = clickedCardButton.getCard();
            try {
                networkMessageSender.play(card);
            } catch (RuntimeException e) {
                throw e;
            }
        } else if (source instanceof SitOrLeaveButton) {
            SitOrLeaveButton clickedSitOrLeaveButton = (SitOrLeaveButton) source;
            Direction direction = (Direction) clickedSitOrLeaveButton.getClientProperty("direction");
            networkMessageSender.sitOrLeave(direction);
            LOGGER.info("Pedindo para sentar ou sair em: " + direction.getCompleteName());
        }

    }
}
