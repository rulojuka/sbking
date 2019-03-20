package br.com.sbk.sbking.core.exceptions;

@SuppressWarnings("serial")
public class PlayedHeartsWhenProhibitedException extends RuntimeException {

	public PlayedHeartsWhenProhibitedException() {
		super("You cannot start a trick with hearts when there are non-hearts still in hand in this ruleset.");
	}

}
