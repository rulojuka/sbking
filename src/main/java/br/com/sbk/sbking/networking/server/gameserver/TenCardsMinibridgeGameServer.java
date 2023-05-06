package br.com.sbk.sbking.networking.server.gameserver;

import br.com.sbk.sbking.core.boarddealer.Partial40CardDeck;

public class TenCardsMinibridgeGameServer extends MinibridgeGameServer {

  public TenCardsMinibridgeGameServer() {
    this.setGameWithCardDeck(new Partial40CardDeck().getDeck());
  }

}
