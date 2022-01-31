package br.com.sbk.sbking.networking.kryonet.messages;

import java.util.HashMap;
import java.util.Map;

import br.com.sbk.sbking.networking.server.gameserver.CagandoNoBequinhoGameServer;
import br.com.sbk.sbking.networking.server.gameserver.GameServer;
import br.com.sbk.sbking.networking.server.gameserver.KingGameServer;
import br.com.sbk.sbking.networking.server.gameserver.MinibridgeGameServer;
import br.com.sbk.sbking.networking.server.gameserver.PositiveKingGameServer;
import br.com.sbk.sbking.networking.server.gameserver.TenCardsMinibridgeGameServer;

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
    gameServerClassesOfGameNames.put("TenCards-Minibridge", TenCardsMinibridgeGameServer.class);
    gameServerClassesOfGameNames.put("Positive King", PositiveKingGameServer.class);
  }

  public static Class<? extends GameServer> identify(String gameName) {
    return gameServerClassesOfGameNames.get(gameName);
  }

}
