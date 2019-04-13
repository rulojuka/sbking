package br.com.sbk.sbking.gui.elements;

import static br.com.sbk.sbking.gui.constants.FrameConstants.HALF_HEIGHT;
import static br.com.sbk.sbking.gui.constants.FrameConstants.HALF_WIDTH;

import java.awt.Container;
import java.awt.event.ActionListener;

import br.com.sbk.sbking.gui.JElements.ConnectToServerButton;

public class ConnectToServerElement {

	public static void add(Container container, ActionListener actionListener) {

		ConnectToServerButton connectToServerButton = new ConnectToServerButton();
		connectToServerButton.addActionListener(actionListener);
		container.add(connectToServerButton); // This line needs to go before setting the button location

		int x_position = HALF_WIDTH - connectToServerButton.getWidth() / 2;
		int y_position = HALF_HEIGHT - connectToServerButton.getHeight() / 2;
		connectToServerButton.setLocation(x_position, y_position); // This line needs to go after adding the button to
																	// the container
	}

}
