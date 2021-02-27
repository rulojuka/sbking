package br.com.sbk.sbking.gui.elements;

import static br.com.sbk.sbking.gui.constants.FrameConstants.HALF_HEIGHT;
import static br.com.sbk.sbking.gui.constants.FrameConstants.HALF_WIDTH;

import java.awt.Container;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JTextField;

import br.com.sbk.sbking.gui.JElements.ConnectToServerButton;
import br.com.sbk.sbking.gui.JElements.SBKingLabel;

public class ConnectToServerElement {

	public static void add(Container container, ActionListener actionListener) {

		ConnectToServerButton connectToServerButton = new ConnectToServerButton();
		connectToServerButton.addActionListener(actionListener);
		container.add(connectToServerButton); // This line needs to go before setting the button location

		int width = 300;
		int height = 15;
		JLabel hostnameLabel = new SBKingLabel("Enter IP or leave blank to play locally:");
		container.add(hostnameLabel);
		hostnameLabel.setSize(width, height);
		hostnameLabel.setLocation(HALF_WIDTH - hostnameLabel.getWidth() / 2, HALF_HEIGHT - 100 - 100 - height);

		JTextField hostnameTextField = new JTextField("");
		container.add(hostnameTextField);
		hostnameTextField.setSize(width, height);
		hostnameTextField.setLocation(HALF_WIDTH - hostnameTextField.getWidth() / 2, HALF_HEIGHT - 100 - 100);
		hostnameTextField.setName("hostnameTextField");

		JLabel nicknameLabel = new SBKingLabel("Enter nickname:");
		container.add(nicknameLabel);
		nicknameLabel.setSize(width, height);
		nicknameLabel.setLocation(HALF_WIDTH - nicknameLabel.getWidth() / 2, HALF_HEIGHT - 100 - height);

		JTextField nicknameTextField = new JTextField("");
		container.add(nicknameTextField);
		nicknameTextField.setSize(width, height);
		nicknameTextField.setLocation(HALF_WIDTH - nicknameTextField.getWidth() / 2, HALF_HEIGHT - 100);
		nicknameTextField.setName("nicknameTextField");

		int x_position = HALF_WIDTH - connectToServerButton.getWidth() / 2;
		int y_position = HALF_HEIGHT - connectToServerButton.getHeight() / 2;
		connectToServerButton.setLocation(x_position, y_position); // This line needs to go after adding the button to
		// the container
	}

}
