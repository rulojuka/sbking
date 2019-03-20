package br.com.sbk.sbking.core.exceptions;

@SuppressWarnings("serial")
public class PlayedCardInAnotherPlayersTurnException extends RuntimeException {

	public PlayedCardInAnotherPlayersTurnException() {
		super("You cannot play a card in another player's turn.");
	}

}
