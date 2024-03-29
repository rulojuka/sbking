package br.com.sbk.sbking.gui.painters;

import java.awt.Component;
import java.awt.Container;

import javax.swing.JTextField;

import br.com.sbk.sbking.gui.elements.ConnectToServerElement;
import br.com.sbk.sbking.gui.jelements.SBKingButton;
import br.com.sbk.sbking.gui.screens.WelcomeScreen;

public class SetNicknameScreenPainter implements Painter {

    private WelcomeScreen welcomeScreen;

    public SetNicknameScreenPainter(WelcomeScreen welcomeScreen) {
        this.welcomeScreen = welcomeScreen;
    }

    @Override
    public void paint(Container contentPane) {
        ConnectToServerElement connectToServerElement = new ConnectToServerElement();
        connectToServerElement.add(contentPane, new ConnectToScreenActionListener());

        contentPane.validate();
        contentPane.repaint();
    }

    class ConnectToScreenActionListener implements java.awt.event.ActionListener {
        public void actionPerformed(java.awt.event.ActionEvent event) {
            SBKingButton button = (SBKingButton) event.getSource();
            Component[] components = button.getParent().getComponents();

            String nickname = "";
            for (Component component : components) {
                if ("nicknameTextField".equals(component.getName())) {
                    JTextField nicknameTextField = (JTextField) component;
                    nickname = nicknameTextField.getText();
                }
            }
            welcomeScreen.setAndSendNickname(nickname);
        }
    }

}
