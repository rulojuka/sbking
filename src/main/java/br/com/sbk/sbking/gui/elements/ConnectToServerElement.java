package br.com.sbk.sbking.gui.elements;

import static br.com.sbk.sbking.gui.constants.FrameConstants.TABLE_HEIGHT;
import static br.com.sbk.sbking.gui.constants.FrameConstants.TABLE_WIDTH;

import java.awt.Container;
import java.awt.event.ActionListener;

import br.com.sbk.sbking.gui.JElements.ConnectToServerButton;

public class ConnectToServerElement {

	private static final int X_POSITION = TABLE_WIDTH / 2 - 100;
	private static final int Y_POSITION = TABLE_HEIGHT / 2 - 50;

	public static void add(Container container, ActionListener actionListener) {

		ConnectToServerButton connectToServerButton = new ConnectToServerButton();
		connectToServerButton.addActionListener(actionListener);
		container.add(connectToServerButton); // This line needs to go before setting the button location
		connectToServerButton.setLocation(X_POSITION, Y_POSITION); // This line needs to go after adding the button to
																	// the container
	}

}
