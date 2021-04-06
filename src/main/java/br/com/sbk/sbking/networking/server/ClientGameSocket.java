package br.com.sbk.sbking.networking.server;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.core.Player;

public class ClientGameSocket {

    private PlayerNetworkInformation playerNetworkInformation;
    private Table table;
    private Direction direction;

    public boolean isSpectator() {
        return direction == null;
    }

    public ClientGameSocket(PlayerNetworkInformation playerNetworkInformation, Direction direction, Table table) {
        this.playerNetworkInformation = playerNetworkInformation;
        this.table = table;
        this.direction = direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void unsetDirection() {
        this.direction = null;
    }

    public void setup() {
        this.waitForClientSetup();
        if (this.isSpectator()) {
            this.table.getSBKingServer().sendIsSpectatorTo(playerNetworkInformation.getPlayer().getIdentifier());
        } else {
            this.table.getSBKingServer().sendIsNotSpectatorTo(playerNetworkInformation.getPlayer().getIdentifier());
            this.waitForClientSetup();
            this.table.getSBKingServer().sendDirectionTo(direction, this.getPlayer().getIdentifier());
        }
        this.waitForClientSetup();
    }

    private void waitForClientSetup() {
        LOGGER.info("Sleeping for 300ms waiting for client to setup itself");
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            LOGGER.debug(e);
        }
    }

    public Player getPlayer() {
        return this.playerNetworkInformation.getPlayer();
    }

    public Direction getDirection() {
        return direction;
    }

}
