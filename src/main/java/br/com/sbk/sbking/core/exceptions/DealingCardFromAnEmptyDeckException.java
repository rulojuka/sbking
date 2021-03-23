package br.com.sbk.sbking.core.exceptions;

@SuppressWarnings("serial")
public class DealingCardFromAnEmptyDeckException extends RuntimeException {

    public DealingCardFromAnEmptyDeckException() {
        super("You cannot deal a card from am empty deck.");
    }

}
