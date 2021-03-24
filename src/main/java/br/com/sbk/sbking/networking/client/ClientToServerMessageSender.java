package br.com.sbk.sbking.networking.client;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

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
        LOGGER.info("Serializator: " + this.serializator);
        LOGGER.info("Serializing sitOrLeave " + direction.getCompleteName());
        this.serializator.tryToSerialize(direction);
    }

}
