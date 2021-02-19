package br.com.sbk.sbking.gui.painters;

import java.awt.Component;
import java.awt.Container;

import javax.swing.JTextField;

import br.com.sbk.sbking.gui.JElements.SBKingButton;
import br.com.sbk.sbking.gui.elements.ConnectToServerElement;
import br.com.sbk.sbking.gui.frames.NetworkClientScreen;

public class ConnectToServerPainter implements Painter {

	private NetworkClientScreen networkClientScreen;

	public ConnectToServerPainter(NetworkClientScreen networkClientScreen) {
		this.networkClientScreen = networkClientScreen;
	}

	@Override
	public void paint(Container contentPane) {
		ConnectToServerElement.add(contentPane, new ConnectToScreenActionListener());

		contentPane.validate();
		contentPane.repaint();
	}

	class ConnectToScreenActionListener implements java.awt.event.ActionListener {
		public void actionPerformed(java.awt.event.ActionEvent event) {
			SBKingButton button = (SBKingButton) event.getSource();
			Component[] components = button.getParent().getComponents();
			for (Component component : components) {
				if( "nicknameTextField".equals(component.getName())){
					JTextField nicknameTextField = (JTextField) component;
					System.out.println("Achou o nickname: " + nicknameTextField.getText());
					networkClientScreen.connectToServer(nicknameTextField.getText());
				}
			}
			
		}
	}

}
