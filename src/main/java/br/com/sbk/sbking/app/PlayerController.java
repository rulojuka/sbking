package br.com.sbk.sbking.app;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import br.com.sbk.sbking.networking.websockets.PlayerListDTO;
import br.com.sbk.sbking.networking.websockets.TableDealDTO;

@Controller
public class PlayerController {

    @Autowired
    private SimpMessagingTemplate template;

    public void getPlayers(PlayerListDTO playerList) {
        LOGGER.info("Sending list of players to subscribers");
        this.template.convertAndSend("/topic/players", playerList);
    }

    public void getDeal(TableDealDTO tableDealDTO) {
        LOGGER.info("Sending deal to table:" + tableDealDTO.getTableId());
        String destination = "/topic/deal/" + tableDealDTO.getTableId();
        this.template.convertAndSend(destination, tableDealDTO);
    }

}
