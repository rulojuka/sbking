package br.com.sbk.sbking.core.exceptions;

@SuppressWarnings("serial")
public class IllegalCardNameException extends IllegalArgumentException {

	public IllegalCardNameException() {
		super("This is not a valid card name.");
	}

}
