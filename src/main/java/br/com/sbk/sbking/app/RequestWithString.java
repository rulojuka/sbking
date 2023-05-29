package br.com.sbk.sbking.app;

import java.util.UUID;

public class RequestWithString {
    private String content;
    private String identifier;

    public void setContent(String content) {
        this.content = content;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getContent() {
        return this.content;
    }

    public UUID getUUID() {
        return UUID.fromString(this.identifier);
    }
}
