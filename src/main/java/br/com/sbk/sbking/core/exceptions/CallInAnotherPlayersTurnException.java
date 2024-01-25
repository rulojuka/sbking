package br.com.sbk.sbking.core.exceptions;

@SuppressWarnings("serial")
public class CallInAnotherPlayersTurnException extends RuntimeException {
    public CallInAnotherPlayersTurnException() {
        super("You cannot call in another player's turn.");
    }
}


