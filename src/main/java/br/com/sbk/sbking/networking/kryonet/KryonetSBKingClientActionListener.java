package br.com.sbk.sbking.networking.kryonet;

public class KryonetSBKingClientActionListener {

  private KryonetSBKingClient client;

  public KryonetSBKingClientActionListener(KryonetSBKingClient client) {
    this.client = client;
  }

  public void undo() {
    client.sendUndo();
  }

}
