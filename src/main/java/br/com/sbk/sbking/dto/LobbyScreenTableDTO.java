package br.com.sbk.sbking.dto;

import java.util.Map;
import java.util.UUID;

import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.core.Player;
import br.com.sbk.sbking.networking.server.Table;

public class LobbyScreenTableDTO {

    private UUID id;
    private Map<Direction, Player> playersDirections;
    private String gameName;
    private int numberOfSpectators;

    /**
     * @deprecated Kryo needs a no-arg constructor
     */
    private LobbyScreenTableDTO() {
    }

    public LobbyScreenTableDTO(Table table) {
        this.id = table.getId();
        this.playersDirections = table.getPlayersDirections();
        this.gameName = table.getGameName();
        this.numberOfSpectators = table.getNumberOfSpectators();
    }

    public UUID getId() {
        return id;
    }

    public Map<Direction, Player> getPlayersDirection() {
        return this.playersDirections;
    }

    public String getGameName() {
        return this.gameName;
    }

    public int getNumberOfSpectators() {
        return this.numberOfSpectators;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof LobbyScreenTableDTO) {
            LobbyScreenTableDTO other = (LobbyScreenTableDTO) obj;
            return this.getId().equals(other.getId());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.getId().hashCode();
    }

}
