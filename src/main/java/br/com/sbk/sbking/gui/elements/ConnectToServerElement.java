package br.com.sbk.sbking.gui.elements;

import java.awt.Container;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JTextField;

import br.com.sbk.sbking.gui.constants.FrameConstants;
import br.com.sbk.sbking.gui.jelements.ConnectToServerButton;
import br.com.sbk.sbking.gui.jelements.SBKingLabel;

public class ConnectToServerElement {

    public void add(Container container, ActionListener actionListener) {

        int halfWidth = FrameConstants.getHalfWidth();
        int halfHeight = FrameConstants.getHalfHeight();

        ConnectToServerButton connectToServerButton = new ConnectToServerButton();
        connectToServerButton.addActionListener(actionListener);
        container.add(connectToServerButton); // This line needs to go before setting the button location

        int width = 160;
        int height = 15;

        JLabel nicknameLabel = new SBKingLabel("Enter nickname:");
        container.add(nicknameLabel);
        nicknameLabel.setSize(width, height);
        nicknameLabel.setLocation(halfWidth - nicknameLabel.getWidth() / 2, halfHeight - 100 - height);

        JTextField nicknameTextField = new JTextField("");
        container.add(nicknameTextField);
        nicknameTextField.setSize(width, height);
        nicknameTextField.setLocation(halfWidth - nicknameTextField.getWidth() / 2, halfHeight - 100);
        nicknameTextField.setName("nicknameTextField");

        int xPosition = halfWidth - connectToServerButton.getWidth() / 2;
        int yPosition = halfHeight - connectToServerButton.getHeight() / 2;
        connectToServerButton.setLocation(xPosition, yPosition); // This line needs to go after adding the button to
        // the container
    }

}
