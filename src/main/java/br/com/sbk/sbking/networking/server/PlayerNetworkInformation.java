package br.com.sbk.sbking.networking.server;

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

}
