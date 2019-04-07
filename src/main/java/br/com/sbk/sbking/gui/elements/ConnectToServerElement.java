package br.com.sbk.sbking.gui.elements;

import static br.com.sbk.sbking.gui.constants.FrameConstants.TABLE_HEIGHT;
import static br.com.sbk.sbking.gui.constants.FrameConstants.TABLE_WIDTH;

import java.awt.Container;
import java.awt.event.ActionListener;

import br.com.sbk.sbking.gui.JElements.ConnectToServerButton;

public class ConnectToServerElement {

	public static void add(Container container, ActionListener actionListener) {

		ConnectToServerButton connectToServerButton = new ConnectToServerButton();
		connectToServerButton.addActionListener(actionListener);
		container.add(connectToServerButton); // This line needs to go before setting the button location

		int x_position = TABLE_WIDTH / 2 - connectToServerButton.getWidth() / 2;
		int y_position = TABLE_HEIGHT / 2 - connectToServerButton.getHeight() / 2;
		connectToServerButton.setLocation(x_position, y_position); // This line needs to go after adding the button to
																	// the container
	}

}
