package br.com.sbk.sbking.gui.elements;

import static br.com.sbk.sbking.gui.constants.FrameConstants.halfHeight;
import static br.com.sbk.sbking.gui.constants.FrameConstants.halfWidth;

import java.awt.Container;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import br.com.sbk.sbking.gui.JElements.ConnectToServerButton;
import br.com.sbk.sbking.gui.JElements.SBKingLabel;

public class ConnectToServerElement {

    private List<JRadioButton> radioButtons;
    private List<String> texts;

    public void add(Container container, ActionListener actionListener) {

        ConnectToServerButton connectToServerButton = new ConnectToServerButton();
        connectToServerButton.addActionListener(actionListener);
        container.add(connectToServerButton); // This line needs to go before setting the button location

        int width = 160;
        int height = 15;

        JLabel hostnameLabel = new SBKingLabel("Choose server:");
        container.add(hostnameLabel);
        hostnameLabel.setSize(width, height);
        hostnameLabel.setLocation(halfWidth - 80, halfHeight - 100 - 100 - height - height);

        texts = new ArrayList<String>();
        texts.add("Local");
        texts.add("Dev");
        texts.add("Perez");
        texts.add("Taís");
        radioButtons = new ArrayList<JRadioButton>();
        SBKingRadioButtonGroupCreator sbKingRadioButtonGroupCreator = new SBKingRadioButtonGroupCreator();
        ButtonGroup buttonGroup = sbKingRadioButtonGroupCreator.create(texts, halfWidth - 80,
                halfHeight - 100 - 100 - height);
        for (Enumeration<AbstractButton> elements = buttonGroup.getElements(); elements.hasMoreElements();) {
            AbstractButton element = elements.nextElement();
            container.add(element);
            radioButtons.add((JRadioButton) element);
        }

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
