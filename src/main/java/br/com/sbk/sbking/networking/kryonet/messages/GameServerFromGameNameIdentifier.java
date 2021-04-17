package br.com.sbk.sbking.networking.kryonet.messages;

import java.util.HashMap;
import java.util.Map;

import br.com.sbk.sbking.networking.server.gameServer.CagandoNoBequinhoGameServer;
import br.com.sbk.sbking.networking.server.gameServer.GameServer;
import br.com.sbk.sbking.networking.server.gameServer.KingGameServer;
import br.com.sbk.sbking.networking.server.gameServer.MinibridgeGameServer;
import br.com.sbk.sbking.networking.server.gameServer.PositiveKingGameServer;

public final class GameServerFromGameNameIdentifier {

  private static Map<String, Class<? extends GameServer>> gameServerClassesOfGameNames = new HashMap<>();

  private GameServerFromGameNameIdentifier() {
    throw new IllegalStateException("Utility class");
  }

  // Static initialization block to avoid doing this calculation every time
  // identify(..) is called.
  static {
    gameServerClassesOfGameNames.put("Cagando no Bequinho", CagandoNoBequinhoGameServer.class);
    gameServerClassesOfGameNames.put("King", KingGameServer.class);
    gameServerClassesOfGameNames.put("Minibridge", MinibridgeGameServer.class);
    gameServerClassesOfGameNames.put("Positive King", PositiveKingGameServer.class);
  }

  public static Class<? extends GameServer> identify(String gameName) {
    return gameServerClassesOfGameNames.get(gameName);
  }

}
