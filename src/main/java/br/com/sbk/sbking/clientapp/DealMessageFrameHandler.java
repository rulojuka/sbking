package br.com.sbk.sbking.clientapp;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import java.lang.reflect.Type;

import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;

import br.com.sbk.sbking.networking.client.SBKingClient;
import br.com.sbk.sbking.networking.websockets.TableMessageDTO;

public class DealMessageFrameHandler implements StompFrameHandler {

    private SBKingClient sbkingClient;

    public DealMessageFrameHandler(SBKingClient sbkingClient) {
        this.sbkingClient = sbkingClient;
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return TableMessageDTO.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        LOGGER.info("Received message: --TableMessage--");
        TableMessageDTO tableDealDTO = (TableMessageDTO) payload;
        String messageType = tableDealDTO.getMessage();
        if ("deal".equals(messageType)) {
            LOGGER.info("Received message: --TableMessage:deal--");
            this.sbkingClient.setCurrentDeal(tableDealDTO.getDeal());
        } else if ("finishDeal".equals(messageType)) {
            LOGGER.info("Received message: --TableMessage:finishDeal--");
            this.sbkingClient.finishDeal();
        }
    }

}
