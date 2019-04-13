package br.com.sbk.sbking.gui.elements;

import static javax.swing.SwingConstants.CENTER;

import java.awt.Container;

import javax.swing.JLabel;

import br.com.sbk.sbking.core.Direction;

public class WaitingForChooserElement {

	public static void add(Container container, Direction direction, String text) {
		String labelTextPrefix = "<html>Waiting for " + direction.toString() + " to choose<br/>";
		String labelTextSuffix = "</html>";
		String completeLabelText = labelTextPrefix + text + labelTextSuffix;

		JLabel waitingLabel = new JLabel(completeLabelText);
		waitingLabel.setHorizontalAlignment(CENTER);
		int width = 300;
		int height = 30;
		int x = br.com.sbk.sbking.gui.constants.FrameConstants.HALF_WIDTH;
		int y = br.com.sbk.sbking.gui.constants.FrameConstants.HALF_HEIGHT;
		waitingLabel.setSize(width, height);
		waitingLabel.setLocation(x - width / 2, y - height / 2);
		container.add(waitingLabel);
	}

}
