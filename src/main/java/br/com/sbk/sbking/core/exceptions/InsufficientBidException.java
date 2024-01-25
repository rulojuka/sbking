package br.com.sbk.sbking.core.exceptions;

@SuppressWarnings("serial")
public class InsufficientBidException extends RuntimeException {
    public InsufficientBidException() {
        super("Your bid must supersede the last bid.");
    }
}
