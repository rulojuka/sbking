package br.com.sbk.sbking.clientapp;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import br.com.sbk.sbking.networking.client.SBKingClient;

public class MyStompSessionHandler extends StompSessionHandlerAdapter {

    private SBKingClient sbKingClient;

    public MyStompSessionHandler(SBKingClient sbKingClient) {
        super();
        this.sbKingClient = sbKingClient;
    }

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        LOGGER.info("New session established: {}", session.getSessionId());
        session.subscribe("/topic/players", new PlayerListDTOFrameHandler(sbKingClient));
        LOGGER.info("Subscribed to /topic/players");
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload,
            Throwable exception) {
        LOGGER.error("Got an exception", exception);
    }

}
