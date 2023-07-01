package br.com.sbk.sbking.clientapp;

import static br.com.sbk.sbking.core.MessageTypes.DEAL_MESSAGE;
import static br.com.sbk.sbking.core.MessageTypes.FINISH_DEAL_MESSAGE;
import static br.com.sbk.sbking.core.MessageTypes.GAME_MODE_OR_STRAIN_CHOOSER_MESSAGE;
import static br.com.sbk.sbking.core.MessageTypes.INITIALIZE_DEAL_MESSAGE;
import static br.com.sbk.sbking.core.MessageTypes.INVALID_RULESET_MESSAGE;
import static br.com.sbk.sbking.core.MessageTypes.POSITIVE_OR_NEGATIVE_CHOOSER_MESSAGE;
import static br.com.sbk.sbking.core.MessageTypes.POSITIVE_OR_NEGATIVE_MESSAGE;
import static br.com.sbk.sbking.core.MessageTypes.VALID_RULESET_MESSAGE;
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
        if (DEAL_MESSAGE.equals(messageType)) {
            LOGGER.trace("Received message: --TableMessage:{}--", DEAL_MESSAGE);
            this.sbkingClient.setCurrentDeal(tableDealDTO.getDeal());
        } else if (FINISH_DEAL_MESSAGE.equals(messageType)) {
            LOGGER.trace("Received message: --TableMessage:{}--", FINISH_DEAL_MESSAGE);
            this.sbkingClient.finishDeal();
        } else if (INITIALIZE_DEAL_MESSAGE.equals(messageType)) {
            LOGGER.trace("Received message: --TableMessage:{}--", INITIALIZE_DEAL_MESSAGE);
            this.sbkingClient.initializeDeal();
        } else if (INVALID_RULESET_MESSAGE.equals(messageType)) {
            LOGGER.trace("Received message: --TableMessage:{}--", INVALID_RULESET_MESSAGE);
            this.sbkingClient.setRulesetValid(false);
        } else if (VALID_RULESET_MESSAGE.equals(messageType)) {
            LOGGER.trace("Received message: --TableMessage:{}--", VALID_RULESET_MESSAGE);
            this.sbkingClient.setRulesetValid(true);
        } else if (POSITIVE_OR_NEGATIVE_MESSAGE.equals(messageType)) {
            LOGGER.trace("Received message: --TableMessage:{}--", POSITIVE_OR_NEGATIVE_MESSAGE);
            this.sbkingClient.setPositiveOrNegative(tableDealDTO.getContent());
        } else if (POSITIVE_OR_NEGATIVE_CHOOSER_MESSAGE.equals(messageType)) {
            LOGGER.trace("Received message: --TableMessage:{}--", POSITIVE_OR_NEGATIVE_CHOOSER_MESSAGE);
            this.sbkingClient.setPositiveOrNegativeChooser(tableDealDTO.getDirection());
        } else if (GAME_MODE_OR_STRAIN_CHOOSER_MESSAGE.equals(messageType)) {
            LOGGER.trace("Received message: --TableMessage:{}--", GAME_MODE_OR_STRAIN_CHOOSER_MESSAGE);
            this.sbkingClient.setGameModeOrStrainChooser(tableDealDTO.getDirection());
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
