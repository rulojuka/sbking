package br.com.sbk.sbking.networking.kryonet;

import com.esotericsoftware.kryonet.Client;

import br.com.sbk.sbking.networking.client.SBKingClient;

public class KryonetSBKingClient extends Client {

  private SBKingClient sbkingClient;

  public SBKingClient getSbkingClient() {
    return sbkingClient;
  }

  public void setSbkingClient(SBKingClient sbkingClient) {
    this.sbkingClient = sbkingClient;
  }

}
