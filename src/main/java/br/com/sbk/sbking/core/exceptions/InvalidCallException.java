package br.com.sbk.sbking.core.exceptions;

@SuppressWarnings("serial")
public class InvalidCallException extends RuntimeException {
    public InvalidCallException() {
        super("You cannot make this call.");
    }
}
