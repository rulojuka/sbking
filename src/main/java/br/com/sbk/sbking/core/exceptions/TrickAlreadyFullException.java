package br.com.sbk.sbking.core.exceptions;

@SuppressWarnings("serial")
public class TrickAlreadyFullException extends RuntimeException {
	public TrickAlreadyFullException() {
		super("You cannot play a card to a trick that is already full.");
	}
}
