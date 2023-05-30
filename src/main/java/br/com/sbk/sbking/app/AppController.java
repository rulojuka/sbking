package br.com.sbk.sbking.app;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.networking.kryonet.messages.GameServerFromGameNameIdentifier;
import br.com.sbk.sbking.networking.server.SBKingServer;
import br.com.sbk.sbking.networking.server.gameserver.GameServer;

@RestController
class AppController {

    @Autowired
    private ServerComponent serverComponent;

    private SBKingServer getServer() {
        return this.serverComponent.getSbKingServer();
    }

    @PostMapping("/playcard")
    void playCard(@RequestBody RequestCard requestCard) {
        LOGGER.trace("playCard");
        this.getServer().play(requestCard.getCard(), requestCard.getUUID());
    }

    @PostMapping("/table")
    void createTable(@RequestBody RequestWithString requestWithString) {
        LOGGER.trace("createTable");
        Class<? extends GameServer> gameServerClass = GameServerFromGameNameIdentifier
                .identify((String) requestWithString.getContent());
        this.getServer().createTable(gameServerClass, requestWithString.getUUID());
    }

    @PostMapping("/table/join/{tableId}")
    void joinTable(@PathVariable String tableId, @RequestBody RequestOnlyIdentifier requestOnlyIdentifier) {
        LOGGER.trace("joinTable");
        this.getServer().joinTable(requestOnlyIdentifier.getUUID(), UUID.fromString(tableId));
    }

    @PostMapping("/table/leave") // Each player can only be in one table for now
    void leaveTable(@RequestBody RequestOnlyIdentifier requestOnlyIdentifier) {
        LOGGER.trace("leaveTable");
        this.getServer().leaveTable(requestOnlyIdentifier.getUUID());
    }

    @PostMapping("/moveToSeat/{directionAbbreviation}")
    void moveToSeat(@PathVariable String directionAbbreviation,
            @RequestBody RequestOnlyIdentifier requestOnlyIdentifier) {
        LOGGER.trace("moveToSeat");
        Direction direction = Direction.getFromAbbreviation(directionAbbreviation.charAt(0));
        this.getServer().moveToSeat(direction, requestOnlyIdentifier.getUUID());
    }

    @PutMapping("/player/nickname")
    void setNickname(@RequestBody RequestWithString requestWithString) {
        LOGGER.trace("setNickname");
        this.getServer().setNickname(
                requestWithString.getUUID(), requestWithString.getContent());
    }

    @PostMapping("/claim")
    void claim(@RequestBody RequestOnlyIdentifier requestOnlyIdentifier) {
        LOGGER.trace("claim");
        this.getServer().claim(requestOnlyIdentifier.getUUID());
    }

    @PostMapping("/claim/{accept}")
    void handleClaim(@PathVariable Boolean accept, @RequestBody RequestOnlyIdentifier requestOnlyIdentifier) {
        LOGGER.info("handleClaim");
        LOGGER.info(accept);
        this.getServer().claim(requestOnlyIdentifier.getUUID());
        if (accept) {
            this.getServer().acceptClaim(requestOnlyIdentifier.getUUID());
        } else {
            this.getServer().rejectClaim(requestOnlyIdentifier.getUUID());
        }
    }

    @PostMapping("/undo")
    void undo(@RequestBody RequestOnlyIdentifier requestOnlyIdentifier) {
        LOGGER.trace("undo");
        this.getServer().undo(requestOnlyIdentifier.getUUID());
    }

}
