package br.com.sbk.sbking.networking.kryonet;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import br.com.sbk.sbking.core.Player;

public class IdentifierToPlayerMap {

  private Map<UUID, Player> map = new HashMap<UUID, Player>();

  public void put(UUID identifier, Player player) {
    this.map.put(identifier, player);
  }

  public void updateNickname(UUID identifier, String newNickname) {
    Player player = this.map.get(identifier);
    if (player != null) {
      player.setNickname(newNickname);
    }
  }

  public Player get(UUID identifier) {
    return this.map.get(identifier);
  }

}
