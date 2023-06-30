package br.com.sbk.sbking.clientapp;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import java.lang.reflect.Type;

import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;

import br.com.sbk.sbking.networking.client.SBKingClient;
import br.com.sbk.sbking.networking.websockets.TableMessageDTO;

public class TableMessageFrameHandler implements StompFrameHandler {

    private SBKingClient sbkingClient;

    public TableMessageFrameHandler(SBKingClient sbkingClient) {
        this.sbkingClient = sbkingClient;
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return TableMessageDTO.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        TableMessageDTO tableDealDTO = (TableMessageDTO) payload;
        String messageType = tableDealDTO.getMessage();
        if ("deal".equals(messageType)) {
            LOGGER.info("Received message: --TableMessage:deal--");
            this.sbkingClient.setCurrentDeal(tableDealDTO.getDeal());
        } else if ("finishDeal".equals(messageType)) {
            LOGGER.info("Received message: --TableMessage:finishDeal--");
            this.sbkingClient.finishDeal();
        } else if ("initializeDeal".equals(messageType)) {
            LOGGER.info("Received message: --TableMessage:initializeDeal--");
            this.sbkingClient.initializeDeal();
        } else if ("invalidRuleset".equals(messageType)) {
            LOGGER.info("Received message: --TableMessage:invalidRuleset--");
            this.sbkingClient.setRulesetValid(false);
        } else if ("validRuleset".equals(messageType)) {
            LOGGER.info("Received message: --TableMessage:validRuleset--");
            this.sbkingClient.setRulesetValid(true);
        } else if ("positiveOrNegative".equals(messageType)) {
            LOGGER.info("Received message: --TableMessage:positiveOrNegative--");
            this.sbkingClient.setPositiveOrNegative(tableDealDTO.getContent());
        } else if ("positiveOrNegativeChooser".equals(messageType)) {
            LOGGER.info("Received message: --TableMessage:positiveOrNegativeChooser--");
            this.sbkingClient.setPositiveOrNegativeChooser(tableDealDTO.getDirection());
        } else {
            LOGGER.error("Could not understand message.");
            LOGGER.error(headers);
            LOGGER.error("Message: {}", tableDealDTO.getMessage());
            LOGGER.error("Table: {}", tableDealDTO.getTableId());
            LOGGER.error("Deal: {}", tableDealDTO.getDeal());
            LOGGER.error("Content: {}", tableDealDTO.getContent());
            LOGGER.error("Direction: {}", tableDealDTO.getDirection());
        }
    }

}
