package br.com.sbk.sbking.clientapp;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import java.lang.reflect.Type;

import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;

import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.networking.client.SBKingClient;
import br.com.sbk.sbking.networking.websockets.TableDealDTO;

public class DealFrameHandler implements StompFrameHandler {

    private SBKingClient sbkingClient;

    public DealFrameHandler(SBKingClient sbkingClient) {
        this.sbkingClient = sbkingClient;
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return TableDealDTO.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        LOGGER.info("Received message: --TableDealDTO--");
        TableDealDTO tableDealDTO = (TableDealDTO) payload;
        Deal currentDeal = tableDealDTO.getDeal();
        LOGGER.info("Table id: {}", tableDealDTO.getTableId());
        LOGGER.info("Setting current deal!");
        this.sbkingClient.setCurrentDeal(currentDeal);
    }

}
