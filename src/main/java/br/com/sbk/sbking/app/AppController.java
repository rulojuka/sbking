package br.com.sbk.sbking.app;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.sbk.sbking.networking.kryonet.messages.GameServerFromGameNameIdentifier;
import br.com.sbk.sbking.networking.server.gameserver.GameServer;

@RestController
class AppController {

    @Autowired
    private ServerComponent serverComponent;

    @PostMapping("/playcard")
    void playCard(@RequestBody RequestCard requestCard) {
        serverComponent.getSbKingServer().play(requestCard.getCard(), requestCard.getUUID());
    }

    @PostMapping("/table")
    void createTable(@RequestBody RequestWithString requestWithString) {
        LOGGER.info("createTable");
        LOGGER.info(requestWithString.getContent());
        Class<? extends GameServer> gameServerClass = GameServerFromGameNameIdentifier
                .identify((String) requestWithString.getContent());
        serverComponent.getSbKingServer().createTable(gameServerClass, requestWithString.getUUID());
    }

}
