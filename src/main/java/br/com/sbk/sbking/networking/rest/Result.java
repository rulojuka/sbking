package br.com.sbk.sbking.networking.rest;

public class Result {

    final int status;
    final String content;

    Result(final int status, final String content) {
        this.status = status;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public int getStatus() {
        return status;
    }

}
