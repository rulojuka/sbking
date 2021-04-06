package br.com.sbk.sbking.networking.server;

import br.com.sbk.sbking.core.Player;

public class PlayerNetworkInformation {
    private Player player;

    public PlayerNetworkInformation(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setNickname(String nickname) {
        this.player.setNickname(nickname);
    }

    public void close() {
        this.releaseResources();
    }

    private void releaseResources() {
        this.player = null;
    }

}
