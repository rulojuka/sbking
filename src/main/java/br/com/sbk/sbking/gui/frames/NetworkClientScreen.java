package br.com.sbk.sbking.gui.frames;

import static br.com.sbk.sbking.gui.constants.FrameConstants.TABLE_COLOR;
import static br.com.sbk.sbking.gui.constants.FrameConstants.TABLE_HEIGHT;
import static br.com.sbk.sbking.gui.constants.FrameConstants.TABLE_WIDTH;

import java.awt.event.ActionListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFrame;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.sbk.sbking.core.Board;
import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.gui.painters.ConnectToServerPainter;
import br.com.sbk.sbking.gui.painters.DealPainter;
import br.com.sbk.sbking.gui.painters.FinalScoreboardPainter;
import br.com.sbk.sbking.gui.painters.Painter;
import br.com.sbk.sbking.gui.painters.SpectatorPainter;
import br.com.sbk.sbking.gui.painters.WaitingForChoosingGameModeOrStrainPainter;
import br.com.sbk.sbking.gui.painters.WaitingForChoosingPositiveOrNegativePainter;
import br.com.sbk.sbking.gui.painters.WaitingForPlayersPainter;
import br.com.sbk.sbking.networking.client.SBKingClient;

@SuppressWarnings("serial")
public class NetworkClientScreen extends JFrame {

	final static Logger logger = LogManager.getLogger(NetworkClientScreen.class);

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
			sleepFor(100);
		}

		logger.info("Waiting for sbKingClient.isDirectionSet() to be true");
		while (!sbKingClient.isDirectionOrSpectatorSet()) {
			sleepFor(100);
		}

		while (true) {
			if(sbKingClient.isSpectator()){
				if(sbKingClient.getBoardHasChanged() || sbKingClient.getDealHasChanged()){
					logger.info("Deal has changed. Painting deal.");
					logger.info("It is a spectator.");
					Deal currentDeal = sbKingClient.getDeal();
					Board currentBoard = sbKingClient.getCurrentBoard();
					if (currentDeal == null) {
						paintSpectatorScreen(currentBoard, sbKingClient.getPlayCardActionListener());
					} else {
						paintSpectatorScreen(currentDeal, sbKingClient.getPlayCardActionListener());
					}
				}
			}
			else{
				if (sbKingClient.getDealHasChanged()) {
					logger.info("Deal has changed. Painting deal.");
					logger.info("It is a player.");
					Deal currentDeal = sbKingClient.getDeal();

					logger.info("Starting to paint Deal");
					paintDeal(currentDeal, sbKingClient.getDirection(), sbKingClient.getPlayCardActionListener());
					logger.info("Finished painting Deal");
				}

			}
				sleepFor(300);
		}
	}

	private void sleepFor(int miliseconds) {
		try {
			Thread.sleep(miliseconds);
		} catch (InterruptedException e) {
			logger.debug(e);
		}
	}

	private void paintPainter(Painter painter) {
		this.getContentPane().removeAll();
		painter.paint(this.getContentPane());
	}

	private void paintConnectToServerScreen() {
		Painter connectToServerPainter = new ConnectToServerPainter(this);
		paintPainter(connectToServerPainter);
	}

	private void paintDeal(Deal deal, Direction direction, ActionListener playCardActionListener) {
		Painter dealPainter = new DealPainter(playCardActionListener, direction, deal);
		paintPainter(dealPainter);
	}

	private void paintSpectatorScreen(Deal deal, ActionListener playCardActionListener) {
		if (deal == null) {
			logger.error("Deal should not be null here.");
		} else {
			Painter spectatorPainter = new SpectatorPainter(playCardActionListener, deal);
			paintPainter(spectatorPainter);
		}
	}

	private void paintSpectatorScreen(Board board, ActionListener playCardActionListener) {
		if (board == null) {
			logger.error("Board should not be null here.");
		} else {
			Painter spectatorPainter = new SpectatorPainter(playCardActionListener, board);
			paintPainter(spectatorPainter);
		}
	}

	public void connectToServer(String nickname, String hostname) {
		hostname = hostname.trim();
		if (isValidIP(hostname)) {
			this.sbKingClient = new SBKingClient(nickname, hostname);
			this.connectedToServer = true;
			pool.execute(this.sbKingClient);
		} else {
			logger.error("Invalid IP");
		}
	}

	private boolean isValidIP(String ipAddr) {
		Pattern ptn = Pattern.compile("^(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})$");
		Matcher mtch = ptn.matcher(ipAddr);
		return mtch.find();
	}

}
