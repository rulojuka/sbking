package br.com.sbk.sbking.networking.kryonet;

import br.com.sbk.sbking.core.Direction;

public class KryonetSBKingClientActionListener {

  private KryonetSBKingClient client;

  public KryonetSBKingClientActionListener(KryonetSBKingClient client) {
    this.client = client;
  }

  public void sitOrLeave(Direction direction) {
    client.sitOrLeave(direction);
  }

  public void undo() {
    client.sendUndo();
  }

  public void claim() {
    client.sendClaim();
  }

  public void leaveTable() {
    client.sendLeaveTable();
  }

  public void acceptClaim() {
    client.sendAcceptClaim();
  }

  public void rejectClaim() {
    client.sendRejectClaim();
  }

}
