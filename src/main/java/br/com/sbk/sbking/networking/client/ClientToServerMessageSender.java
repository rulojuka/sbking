package br.com.sbk.sbking.networking.client;

import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.networking.core.serialization.Serializator;

public class ClientToServerMessageSender {

    private Serializator serializator;

    public ClientToServerMessageSender(Serializator serializator) {
        this.serializator = serializator;
    }

    public void play(Card card) {
        this.serializator.tryToSerialize(card);
    }

    public void sitOrLeave(Direction direction) {
        this.serializator.tryToSerialize(direction);
    }

}
