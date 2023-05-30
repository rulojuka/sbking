package br.com.sbk.sbking.app;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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

    private UUID getUUID(String uuidString) {
        return UUID.fromString(uuidString);
    }

    @PostMapping("/playcard")
    void playCard(@RequestHeader("PlayerUUID") String playerUUID, @RequestBody RequestCard requestCard) {
        LOGGER.trace("playCard");
        this.getServer().play(requestCard.getCard(), getUUID(playerUUID));
    }

    @PostMapping("/table")
    void createTable(@RequestHeader("PlayerUUID") String playerUUID, @RequestBody RequestWithString requestWithString) {
        LOGGER.trace("createTable");
        Class<? extends GameServer> gameServerClass = GameServerFromGameNameIdentifier
                .identify((String) requestWithString.getContent());
        this.getServer().createTable(gameServerClass, getUUID(playerUUID));
    }

    @PostMapping("/table/join/{tableId}")
    void joinTable(@RequestHeader("PlayerUUID") String playerUUID, @PathVariable String tableId) {
        LOGGER.trace("joinTable");
        this.getServer().joinTable(getUUID(playerUUID), UUID.fromString(tableId));
    }

    @PostMapping("/table/leave") // Each player can only be in one table for now
    void leaveTable(@RequestHeader("PlayerUUID") String playerUUID) {
        LOGGER.trace("leaveTable");
        this.getServer().leaveTable(getUUID(playerUUID));
    }

    @PostMapping("/moveToSeat/{directionAbbreviation}")
    void moveToSeat(@RequestHeader("PlayerUUID") String playerUUID, @PathVariable String directionAbbreviation) {
        LOGGER.trace("moveToSeat");
        Direction direction = Direction.getFromAbbreviation(directionAbbreviation.charAt(0));
        this.getServer().moveToSeat(direction, getUUID(playerUUID));
    }

    @PutMapping("/player/nickname")
    void setNickname(@RequestHeader("PlayerUUID") String playerUUID, @RequestBody RequestWithString requestWithString) {
        LOGGER.trace("setNickname");
        this.getServer().setNickname(
                getUUID(playerUUID), requestWithString.getContent());
    }

    @PostMapping("/claim")
    void claim(@RequestHeader("PlayerUUID") String playerUUID) {
        LOGGER.trace("claim");
        this.getServer().claim(getUUID(playerUUID));
    }

    @PostMapping("/claim/{accept}")
    void handleClaim(@RequestHeader("PlayerUUID") String playerUUID, @PathVariable Boolean accept) {
        LOGGER.info("handleClaim");
        LOGGER.info(accept);
        this.getServer().claim(getUUID(playerUUID));
        if (accept) {
            this.getServer().acceptClaim(getUUID(playerUUID));
        } else {
            this.getServer().rejectClaim(getUUID(playerUUID));
        }
    }

    @PostMapping("/undo")
    void undo(@RequestHeader("PlayerUUID") String playerUUID) {
        LOGGER.trace("undo");
        this.getServer().undo(getUUID(playerUUID));
    }

    @PostMapping("/choosePositiveOrNegative/{positiveOrNegative}")
    void choosePositiveOrNegative(@RequestHeader("PlayerUUID") String playerUUID,
            @PathVariable String positiveOrNegative) {
        LOGGER.trace("choosePositiveOrNegative");
        if ("+".equals(positiveOrNegative)) {
            this.getServer().choosePositive(getUUID(playerUUID));
        } else {
            this.getServer().chooseNegative(getUUID(playerUUID));
        }
    }

}
