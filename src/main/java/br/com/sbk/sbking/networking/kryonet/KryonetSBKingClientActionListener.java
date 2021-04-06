package br.com.sbk.sbking.networking.kryonet;

import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Direction;

public class KryonetSBKingClientActionListener {

  private KryonetSBKingClient client;

  public KryonetSBKingClientActionListener(KryonetSBKingClient client) {
    this.client = client;
  }

  public void play(Card card) {
    client.sendCard(card);
  }

  public void sitOrLeave(Direction direction) {
    client.sitOrLeave(direction);
  }

  public void undo() {
    client.sendUndo();
  }

}
