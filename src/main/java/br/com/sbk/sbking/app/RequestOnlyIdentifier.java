package br.com.sbk.sbking.app;

import java.util.UUID;

public class RequestOnlyIdentifier {
    private String identifier;

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public UUID getUUID() {
        return UUID.fromString(this.identifier);
    }
}
