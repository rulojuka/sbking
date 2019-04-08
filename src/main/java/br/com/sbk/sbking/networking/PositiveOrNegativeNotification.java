package br.com.sbk.sbking.networking;

import br.com.sbk.sbking.gui.models.PositiveOrNegative;

public class PositiveOrNegativeNotification {

	private PositiveOrNegative positiveOrNegative;

	public void notifyAllWithPositiveOrNegative(PositiveOrNegative positiveOrNegative) {
		this.positiveOrNegative = positiveOrNegative;
		this.notifyAll();
	}
	
	public PositiveOrNegative getPositiveOrNegative() {
		return this.positiveOrNegative;
	}

}
