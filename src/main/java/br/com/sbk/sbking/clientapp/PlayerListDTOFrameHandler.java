package br.com.sbk.sbking.clientapp;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import java.lang.reflect.Type;
import java.util.Optional;
import java.util.UUID;

import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;

import br.com.sbk.sbking.networking.client.SBKingClient;
import br.com.sbk.sbking.networking.websockets.PlayerDTO;
import br.com.sbk.sbking.networking.websockets.PlayerListDTO;

public class PlayerListDTOFrameHandler implements StompFrameHandler {

    private SBKingClient sbkingClient;

    public PlayerListDTOFrameHandler(SBKingClient sbkingClient) {
        this.sbkingClient = sbkingClient;
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return PlayerListDTO.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        LOGGER.info("Received message: --PlayerListDTO--");
        PlayerListDTO playerListDTO = (PlayerListDTO) payload;

        UUID id = this.sbkingClient.getId();
        Optional<PlayerDTO> playerFromList = playerListDTO.getList().stream().filter(p -> id.equals(p.getPlayer()))
                .findAny();
        if (playerFromList.isPresent()) {
            PlayerDTO myself = playerFromList.get();
            LOGGER.info("My information:"
                    + "\nUUID:" + myself.getPlayer().toString()
                    + "\nTable:" + myself.getTable()
                    + "\nDirection:" + myself.getDirection()
                    + "\nIsSpectator:" + myself.getSpectator()
                    + "\nGameName:" + myself.getGameName());

            this.sbkingClient.setCurrentTable(myself.getTable());
            this.sbkingClient.setSpectator(myself.getSpectator());
            this.sbkingClient.initializeDirection(myself.getDirection());
            this.sbkingClient.setGameName(myself.getGameName());
        }
    }

}
