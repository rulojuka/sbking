package br.com.sbk.sbking.gui.elements;

import static br.com.sbk.sbking.gui.constants.FrameConstants.HALF_HEIGHT;
import static br.com.sbk.sbking.gui.constants.FrameConstants.HALF_WIDTH;
import static javax.swing.SwingConstants.CENTER;

import java.awt.Container;
import java.awt.Point;

import javax.swing.JLabel;

import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.gui.JElements.SBKingLabel;

public class YouArePlayerElement {

	private static final int X_OFFSET = 0;
	private static final int Y_OFFSET = -150;

	public static void add(Direction direction, Container container) {
		int width = 100;
		int height = 15;
		Point tableCenter = new Point(HALF_WIDTH, HALF_HEIGHT);
		Point currentPlayerPosition = new Point(tableCenter);
		currentPlayerPosition.translate(X_OFFSET - width / 2, Y_OFFSET);

		JLabel playerLabel = new SBKingLabel("You are " + direction.getCompleteName());
		playerLabel.setHorizontalAlignment(CENTER);
		playerLabel.setSize(width, height);
		playerLabel.setLocation(currentPlayerPosition);
		container.add(playerLabel);
	}

}
