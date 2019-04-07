package br.com.sbk.sbking.gui.painters;

import java.awt.Container;

import br.com.sbk.sbking.gui.elements.ConnectToServerElement;
import br.com.sbk.sbking.gui.frames.NetworkClientScreen;

public class ConnectToServerPainter {

	private NetworkClientScreen networkClientScreen;

	public ConnectToServerPainter(NetworkClientScreen networkClientScreen) {
		this.networkClientScreen = networkClientScreen;
	}

	public void paint(Container contentPane) {
		ConnectToServerElement.add(contentPane, new ConnectToScreenActionListener());

		contentPane.validate();
		contentPane.repaint();
	}

	class ConnectToScreenActionListener implements java.awt.event.ActionListener {
		public void actionPerformed(java.awt.event.ActionEvent event) {
			networkClientScreen.connectToServer();
		}
	}

}
