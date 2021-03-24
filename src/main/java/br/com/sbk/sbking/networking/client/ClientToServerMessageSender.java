package br.com.sbk.sbking.networking.client;

import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.networking.core.serialization.Serializator;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class ClientToServerMessageSender {

    private static final Logger LOGGER = LogManager.getLogger(ClientToServerMessageSender.class);

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
