package br.com.sbk.sbking.gui.frames;

import static br.com.sbk.sbking.gui.constants.FrameConstants.TABLE_COLOR;
import static br.com.sbk.sbking.gui.constants.FrameConstants.TABLE_HEIGHT;
import static br.com.sbk.sbking.gui.constants.FrameConstants.TABLE_WIDTH;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JFrame;

import org.apache.log4j.Logger;

import br.com.sbk.sbking.gui.painters.ConnectToServerPainter;
import br.com.sbk.sbking.gui.painters.WaitingForChoosingPainter;
import br.com.sbk.sbking.gui.painters.WaitingForPlayersPainter;
import br.com.sbk.sbking.networking.SBKingClient;

@SuppressWarnings("serial")
public class NetworkClientScreen extends JFrame {

	final static Logger logger = Logger.getLogger(NetworkClientScreen.class);

	private boolean connectedToServer = false;
	private SBKingClient sbKingClient;

	private ExecutorService pool;

	public NetworkClientScreen() {
		super();
		initializeJFrame();
		initializeContentPane();
		pool = Executors.newFixedThreadPool(4);
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
		logger.info("Starting to paint ConnectToServerScreen");
		paintConnectToServerScreen();
		logger.info("Finished painting ConnectToServerScreen");

		logger.info("Waiting for connectedToServer to be true");
		while (!connectedToServer) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		logger.info("Waiting for sbKingClient.isDirectionSet() to be true");
		while (!sbKingClient.isDirectionSet()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		logger.info("Starting to paint WaitingForPlayersScreen");
		paintWaitingForPlayersScreen();
		logger.info("Finished painting WaitingForPlayersScreen");

		logger.info("Waiting for sbKingClient.isEveryoneConnected() to be true");
		while (!sbKingClient.isEveryoneConnected()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//while(true) { //FIXME Should last 10 deals - a complete king game
			logger.info("Starting to paint WaitingForChoosingScreen");
			paintWaitingForChoosingScreen();
			logger.info("Finished painting WaitingForChoosingScreen");
		//}
		
		
		logger.info("Acabou!");
	}

	private void paintConnectToServerScreen() {
		cleanContentPane();
		ConnectToServerPainter connectToServerPainter = new ConnectToServerPainter(this);
		connectToServerPainter.paint(this.getContentPane());
	}

	private void paintWaitingForPlayersScreen() {
		cleanContentPane();
		WaitingForPlayersPainter waitingForPlayersPainter = new WaitingForPlayersPainter(sbKingClient.getDirection());
		waitingForPlayersPainter.paint(this.getContentPane());
	}
	
	private void paintWaitingForChoosingScreen() {
		cleanContentPane();
		WaitingForChoosingPainter waitingForChoosingPainter = new WaitingForChoosingPainter();
		waitingForChoosingPainter.paint(this.getContentPane());
	}

	private void cleanContentPane() {
		this.getContentPane().removeAll();
	}

	private void initializeWaitingForPlayerElement() {
		logger.info("Entrou initializeWaitingForPlayerElement");
//		this.setVisible(false);
//		this.dispose();
	}

	public void connectToServer() {
		this.sbKingClient = new SBKingClient();
		this.connectedToServer = true;
		pool.execute(this.sbKingClient);
	}

	class GameSelectActionListener implements java.awt.event.ActionListener {
		public void actionPerformed(java.awt.event.ActionEvent event) {
			SBKingClient sbKingClient = new SBKingClient();
			sbKingClient.run();
			initializeWaitingForPlayerElement();
		}

	}

}
