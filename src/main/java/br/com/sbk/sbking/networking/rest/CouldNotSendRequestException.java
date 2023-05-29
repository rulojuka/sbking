package br.com.sbk.sbking.networking.rest;

public class CouldNotSendRequestException extends RuntimeException {
    public CouldNotSendRequestException(Exception e) {
        super(e);
    }
}
