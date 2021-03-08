package br.com.sbk.sbking.gui.elements;

import java.awt.Container;
import java.awt.Point;

import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.core.Hand;
import br.com.sbk.sbking.core.Player;
import br.com.sbk.sbking.gui.constants.FrameConstants;

public class HandWhileChoosingElement {

	public HandWhileChoosingElement(Container contentPane, Deal deal, Direction direction) {
		Hand myHand = deal.getHandOf(direction);
		Point handLocationCenter = FrameConstants.pointOfDirection.get(direction);
		new HandElement(myHand, contentPane, null, handLocationCenter, new Player("Choosing: "), true, direction);
	}
}
