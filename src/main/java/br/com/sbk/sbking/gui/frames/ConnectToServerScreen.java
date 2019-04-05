package br.com.sbk.sbking.gui.frames;

import java.awt.Container;

import javax.swing.JButton;
import javax.swing.JFrame;

import br.com.sbk.sbking.networking.SBKingClient;

@SuppressWarnings("serial")
public class ConnectToServerScreen extends JFrame {

	private static final java.awt.Color TABLE_COLOR = new java.awt.Color(0, 100, 0); // Tablecloth green
	private static final int WIDTH = 1024;
	private static final int HEIGHT = 768;

	public ConnectToServerScreen() {
		super();
		initializeJFrame();
		initializeContentPane();
		Container contentPane = this.getContentPane();

		initializeElements();

		contentPane.validate();
		contentPane.repaint();
	}

	private void initializeElements() {
		JButton connectToServerButton = new JButton();
		this.getContentPane().add(connectToServerButton);

		connectToServerButton.addActionListener(new GameSelectActionListener());
		connectToServerButton.setBounds(WIDTH / 2 - 100, HEIGHT / 2 - 50, 200, 100);
		connectToServerButton.setText("Connect to server");
	}

	private void initializeJFrame() {
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(WIDTH, HEIGHT);
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
