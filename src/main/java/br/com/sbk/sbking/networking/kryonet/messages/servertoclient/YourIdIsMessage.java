package br.com.sbk.sbking.networking.kryonet.messages.servertoclient;

import br.com.sbk.sbking.networking.kryonet.messages.SBKingMessage;

public class YourIdIsMessage implements SBKingMessage {

    private String id;

    /**
     * @deprecated Kryo needs a no-arg constructor
     */
    @Deprecated
    @SuppressWarnings("unused")
    private YourIdIsMessage() {
    }

    public YourIdIsMessage(String id) {
        this.id = id;
    }

    @Override
    public String getContent() {
        return this.id;
    }

}
