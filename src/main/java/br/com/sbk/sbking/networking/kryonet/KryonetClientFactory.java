package br.com.sbk.sbking.networking.kryonet;

import br.com.sbk.sbking.networking.client.SBKingClient;

public class KryonetClientFactory {
  public static KryonetSBKingClient getRegisteredClient(SBKingClient sbkingClient) {
    KryonetSBKingClient client = new KryonetSBKingClient(sbkingClient);
    client.start();

    // For consistency, the classes to be sent over the network are
    // registered by the same method for both the client and server.
    KryonetUtils.register(client);

    client.addListener(KryonetClientListenerFactory.getClientListener(client));
    return client;
  }

}
