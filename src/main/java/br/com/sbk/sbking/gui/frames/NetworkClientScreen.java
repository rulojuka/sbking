package br.com.sbk.sbking.gui.frames;

import static br.com.sbk.sbking.gui.constants.FrameConstants.TABLE_COLOR;
import static br.com.sbk.sbking.gui.constants.FrameConstants.TABLE_HEIGHT;
import static br.com.sbk.sbking.gui.constants.FrameConstants.TABLE_WIDTH;

import java.awt.Container;

import javax.swing.JButton;
import javax.swing.JFrame;

import br.com.sbk.sbking.gui.elements.ConnectToServerElement;
import br.com.sbk.sbking.networking.SBKingClient;

@SuppressWarnings("serial")
public class NetworkClientScreen extends JFrame {

	private Container contentPane;

	public NetworkClientScreen() {
		super();
		initializeJFrame();
		initializeContentPane();
		this.contentPane = this.getContentPane();

		initializeElements();

		this.contentPane.validate();
		this.contentPane.repaint();
	}

	private void initializeJFrame() {
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(TABLE_WIDTH, TABLE_HEIGHT);
	}

	private void initializeElements() {
		ConnectToServerElement.add(this.contentPane, new GameSelectActionListener());
		JButton connectToServerButton = new JButton();
		this.getContentPane().add(connectToServerButton);

		connectToServerButton.addActionListener(new GameSelectActionListener());
		connectToServerButton.setBounds(TABLE_WIDTH / 2 - 100, TABLE_HEIGHT / 2 - 50, 200, 100);
		connectToServerButton.setText("Connect to server");
	}

	private void initializeContentPane() {
		getContentPane().setLayout(null);
		getContentPane().setBackground(TABLE_COLOR);
	}

	private void close() {
		this.setVisible(false);
		this.dispose();
	}

	class GameSelectActionListener implements java.awt.event.ActionListener {
		public void actionPerformed(java.awt.event.ActionEvent event) {
			close();
			SBKingClient sbKingClient = new SBKingClient();
			sbKingClient.run();
		}
	}

}
