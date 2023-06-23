package br.com.sbk.sbking.clientapp;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import java.lang.reflect.Type;

import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;

import br.com.sbk.sbking.networking.websockets.TableDealDTO;

public class DealFrameHandler implements StompFrameHandler {

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return TableDealDTO.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        LOGGER.info("Received message: --TableDealDTO--");
        TableDealDTO tableDealDTO = (TableDealDTO) payload;
        LOGGER.info("Table id: " + tableDealDTO.getTableId());

    }

}
