package br.com.sbk.sbking.networking;

import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Direction;

public class CardPlayNotification {

	private Card card;
	private Direction direction;

	public void notifyAllWithCardAndDirection(Card card, Direction direction) {
		this.card = card;
		this.direction = direction;
		this.notifyAll();
	}

	public Card getCard() {
		return this.card;
	}

	public Direction getDirection() {
		return this.direction;
	}

}
