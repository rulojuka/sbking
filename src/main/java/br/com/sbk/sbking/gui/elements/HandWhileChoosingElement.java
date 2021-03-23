package br.com.sbk.sbking.gui.elements;

import java.awt.Container;
import java.awt.Point;

import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.core.Hand;
import br.com.sbk.sbking.core.Player;
import br.com.sbk.sbking.gui.constants.FrameConstants;

public class HandWhileChoosingElement {

    public HandWhileChoosingElement(Container contentPane, Hand myHand, Direction myDirection) {
        Point handLocationCenter = FrameConstants.pointOfDirection.get(myDirection);
        new HandElement(myHand, contentPane, null, handLocationCenter, new Player("Choosing: "), true, myDirection);
    }
}
