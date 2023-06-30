package br.com.sbk.sbking.networking.kryonet;

import java.util.UUID;

import com.esotericsoftware.kryonet.Connection;

import br.com.sbk.sbking.networking.server.SBKingServer;

public final class KryonetServerFactory {

  private KryonetServerFactory() {
    throw new IllegalStateException("Utility class");
  }

  public static KryonetSBKingServer getRegisteredServer(SBKingServer sbkingServer) {
    KryonetSBKingServer server;
    server = new KryonetSBKingServer(sbkingServer) {
      protected Connection newConnection() {
        // By providing our own connection implementation, we can store per
        // connection state without a connection ID to state look up.
        UUID uuid = UUID.randomUUID();
        return new ConnectionWithIdentifier(uuid);
      }
    };

    // For consistency, the classes to be sent over the network are
    // registered by the same method for both the client and server.
    KryonetUtils.register(server);
    return server;
  }

}
