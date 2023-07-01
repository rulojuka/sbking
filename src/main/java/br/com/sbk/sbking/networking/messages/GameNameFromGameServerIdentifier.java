
package br.com.sbk.sbking.networking.messages;

import java.util.HashMap;
import java.util.Map;

import br.com.sbk.sbking.networking.server.gameserver.CagandoNoBequinhoGameServer;
import br.com.sbk.sbking.networking.server.gameserver.GameServer;
import br.com.sbk.sbking.networking.server.gameserver.KingGameServer;
import br.com.sbk.sbking.networking.server.gameserver.MiniMinibridgeGameServer;
import br.com.sbk.sbking.networking.server.gameserver.MinibridgeGameServer;
import br.com.sbk.sbking.networking.server.gameserver.PositiveKingGameServer;
import br.com.sbk.sbking.networking.server.gameserver.TenCardsMinibridgeGameServer;

public final class GameNameFromGameServerIdentifier {

  private static Map<Class<? extends GameServer>, String> gameServerClassesOfGameNames = new HashMap<>();

  private GameNameFromGameServerIdentifier() {
    throw new IllegalStateException("Utility class");
  }

  // Static initialization block to avoid doing this calculation every time
  // identify(..) is called.
  static {
    gameServerClassesOfGameNames.put(CagandoNoBequinhoGameServer.class, "Cagando no Bequinho");
    gameServerClassesOfGameNames.put(KingGameServer.class, "King");
    gameServerClassesOfGameNames.put(MinibridgeGameServer.class, "Minibridge");
    gameServerClassesOfGameNames.put(MiniMinibridgeGameServer.class, "Mini-Minibridge");
    gameServerClassesOfGameNames.put(TenCardsMinibridgeGameServer.class, "TenCards-Minibridge");
    gameServerClassesOfGameNames.put(PositiveKingGameServer.class, "Positive King");
  }

  public static String identify(Class<? extends GameServer> gameServerClass) {
    return gameServerClassesOfGameNames.get(gameServerClass);
  }

}
