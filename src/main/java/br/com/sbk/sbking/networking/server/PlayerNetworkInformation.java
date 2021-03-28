package br.com.sbk.sbking.networking.server;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import java.io.IOException;
import java.net.Socket;

import br.com.sbk.sbking.core.Player;
import br.com.sbk.sbking.networking.core.serialization.Serializator;

public class PlayerNetworkInformation {
    private Socket socket;
    private Serializator serializator;
    private Player player;

    public PlayerNetworkInformation(Socket socket, Serializator serializator, Player player) {
        this.socket = socket;
        this.serializator = serializator;
        this.player = player;
    }

    public Socket getSocket() {
        return this.socket;
    }

    public Serializator getSerializator() {
        return this.serializator;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setNickname(String nickname) {
        this.player.setName(nickname);
    }

    public void close() {
        LOGGER.info("Closing socket.");
        try {
            this.socket.close();
        } catch (IOException e) {
            LOGGER.error(e);
        }
        LOGGER.info("Socket closed.");

        LOGGER.info("Closing serializator.");
        this.getSerializator().close();
        LOGGER.info("Serializator closed.");
        this.releaseResources();
    }

    private void releaseResources() {
        this.player = null;
        this.socket = null;
        this.serializator = null;
    }

}
