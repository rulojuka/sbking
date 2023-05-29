package br.com.sbk.sbking.core.exceptions;

@SuppressWarnings("serial")
public class DirectionDoesNotExist extends RuntimeException {

    public DirectionDoesNotExist() {
        super("This direction does not exist. Please use the correct abbreviation.");
    }

}
