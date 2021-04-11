package br.com.sbk.sbking.networking.kryonet;

public class KryonetClientFactory {
  public static KryonetSBKingClient getRegisteredClient() {
    KryonetSBKingClient client = new KryonetSBKingClient();
    client.start();

    // For consistency, the classes to be sent over the network are
    // registered by the same method for both the client and server.
    KryonetUtils.register(client);

    return client;
  }

}
