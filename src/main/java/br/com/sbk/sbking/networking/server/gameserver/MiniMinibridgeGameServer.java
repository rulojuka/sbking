package br.com.sbk.sbking.networking.server.gameserver;

import java.util.Deque;

import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.boarddealer.Partial32CardDeck;

public class MiniMinibridgeGameServer extends MinibridgeGameServer {

  public MiniMinibridgeGameServer() {
    super();
    Deque<Card> deck = new Partial32CardDeck().getDeck();
    this.setGameWithCardDeck(deck);
  }

}
