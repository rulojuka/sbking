package br.com.sbk.sbking.gui.frames;

import static br.com.sbk.sbking.gui.constants.FrameConstants.TABLE_COLOR;
import static br.com.sbk.sbking.gui.constants.FrameConstants.TABLE_HEIGHT;
import static br.com.sbk.sbking.gui.constants.FrameConstants.TABLE_WIDTH;

import javax.swing.JFrame;

import br.com.sbk.sbking.gui.painters.ConnectToServerPainter;
import br.com.sbk.sbking.networking.SBKingClient;

@SuppressWarnings("serial")
public class NetworkClientScreen extends JFrame {

	private boolean connectedToServer = false;
	private SBKingClient sbKingClient;

	public NetworkClientScreen() {
		super();
		initializeJFrame();
		initializeContentPane();
	}

	private void initializeJFrame() {
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(TABLE_WIDTH, TABLE_HEIGHT);
	}

	private void initializeContentPane() {
		getContentPane().setLayout(null);
		getContentPane().setBackground(TABLE_COLOR);
	}

	public void run() {
		paintConnectToServerScreen();
		while (!connectedToServer) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("Acabou!");
	}

	private void paintConnectToServerScreen() {
		cleanContentPane();
		ConnectToServerPainter connectToServerPainter = new ConnectToServerPainter(this);
		connectToServerPainter.paint(this.getContentPane());

//		JButton connectToServerButton = new JButton();
//		this.getContentPane().add(connectToServerButton);
//
//		connectToServerButton.addActionListener(new GameSelectActionListener());
//		connectToServerButton.setBounds(TABLE_WIDTH / 2 - 100, TABLE_HEIGHT / 2 - 50, 200, 100);
//		connectToServerButton.setText("Connect to server");

	}

	private void cleanContentPane() {
		this.getContentPane().removeAll();
	}

	private void initializeWaitingForPlayerElement() {
		this.setVisible(false);
		this.dispose();
	}

	public void connectToServer() {
		sbKingClient = new SBKingClient();
		connectedToServer = true;
		sbKingClient.run();
	}

	class GameSelectActionListener implements java.awt.event.ActionListener {
		public void actionPerformed(java.awt.event.ActionEvent event) {
			SBKingClient sbKingClient = new SBKingClient();
			sbKingClient.run();
			initializeWaitingForPlayerElement();
		}

	}

}
