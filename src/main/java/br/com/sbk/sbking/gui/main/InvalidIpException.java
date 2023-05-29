package br.com.sbk.sbking.gui.main;

public class InvalidIpException extends RuntimeException {

    public InvalidIpException() {
        super("The provided IP is invalid.");
    }

}
