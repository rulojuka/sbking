package br.com.sbk.sbking.core.exceptions;

@SuppressWarnings("serial")
public class SelectedPositiveOrNegativeInAnotherPlayersTurnException extends RuntimeException {

	public SelectedPositiveOrNegativeInAnotherPlayersTurnException() {
		super("You cannot choose positive or negative in another player's turn.");
	}

}
