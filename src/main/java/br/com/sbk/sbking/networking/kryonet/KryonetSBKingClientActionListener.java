package br.com.sbk.sbking.networking.kryonet;

public class KryonetSBKingClientActionListener {

  private KryonetSBKingClient client;

  public KryonetSBKingClientActionListener(KryonetSBKingClient client) {
    this.client = client;
  }

  public void undo() {
    client.sendUndo();
  }

  public void claim() {
    client.sendClaim();
  }

  public void acceptClaim() {
    client.sendAcceptClaim();
  }

  public void rejectClaim() {
    client.sendRejectClaim();
  }

}
