package br.com.sbk.sbking.networking.websockets;

import java.util.UUID;

import br.com.sbk.sbking.core.Direction;

public class PlayerDTO {
    private UUID player;
    private UUID table;
    private boolean spectator;
    private Direction direction;
    private String gameName;

    public PlayerDTO() {
    }

    public PlayerDTO(UUID player, UUID table, boolean spectator, Direction direction, String gameName) {
        this.player = player;
        this.table = table;
        this.spectator = spectator;
        this.direction = direction;
        this.gameName = gameName;
    }

    public UUID getPlayer() {
        return player;
    }

    public void setPlayer(UUID player) {
        this.player = player;
    }

    public UUID getTable() {
        return table;
    }

    public void setTable(UUID table) {
        this.table = table;
    }

    public boolean getSpectator() {
        return spectator;
    }

    public void setSpectator(boolean spectator) {
        this.spectator = spectator;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }
}
