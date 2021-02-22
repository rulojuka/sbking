package br.com.sbk.sbking.gui.frames;

import static br.com.sbk.sbking.gui.constants.FrameConstants.TABLE_COLOR;
import static br.com.sbk.sbking.gui.constants.FrameConstants.TABLE_HEIGHT;
import static br.com.sbk.sbking.gui.constants.FrameConstants.TABLE_WIDTH;

import java.awt.event.ActionListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JFrame;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

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

		while(sbKingClient.isSpectator()){
			// logger.info("It is a spectator. Painting the spectator screen");
			paintSpectatorScreen(sbKingClient.getDeal(), sbKingClient.getPlayCardActionListener());
			sleepFor(100);
		}

		logger.info("Starting to paint WaitingForPlayersScreen");
		paintWaitingForPlayersScreen();
		logger.info("Finished painting WaitingForPlayersScreen");

		logger.info("Waiting for sbKingClient.isEveryoneConnected() to be true");
		while (!sbKingClient.isEveryoneConnected()) {
			sleepFor(100);
		}

		while (true) {

			logger.info(
					"Waiting for sbKingClient.isPositiveOrNegativeChooserSet() or sbKingClient.isGameFinished() to be true");
			while (!sbKingClient.isPositiveOrNegativeChooserSet() && !sbKingClient.isGameFinished()) {
				sleepFor(100);
			}

			if (sbKingClient.isGameFinished()) {
				logger.info("Game is finished. Exiting main loop");
				break;
			}

			logger.info("Starting to paint WaitingForChoosingScreen");
			paintWaitingForChoosingPositiveOrNegativeScreen(sbKingClient.getDirection(),
					sbKingClient.getPositiveOrNegativeChooser());
			logger.info("Finished painting WaitingForChoosingScreen");

			logger.info("Waiting for sbKingClient.isPositiveOrNegativeSelected() to be true");
			while (!sbKingClient.isPositiveOrNegativeSelected()) {
				sleepFor(100);
			}
			boolean isPositive = this.sbKingClient.isPositive();
			logger.info("Received PositiveOrNegative from server.");

			logger.info("Waiting for sbKingClient.isGameModeOrStrainChooserSet() to be true");
			while (!sbKingClient.isGameModeOrStrainChooserSet()) {
				sleepFor(100);
			}

			logger.info("Starting to paint WaitingForChoosingGameModeOrStrainScreen");
			paintWaitingForChoosingGameModeOrStrainScreen(sbKingClient.getDirection(),
					sbKingClient.getGameModeOrStrainChooser(), isPositive);
			logger.info("Finished painting WaitingForChoosingGameModeOrStrainScreen");

			logger.info("Waiting for server to valid chosen Ruleset");
			while (!sbKingClient.isRulesetValidSet()) {
				sleepFor(100);
			}
			if (!sbKingClient.isRulesetValid()) {
				logger.info("Chosen Ruleset is invalid, sleeping for 2 seconds while client cleans itself");
				sleepFor(2000);
				logger.info("Returning to beginning of loop");
				continue;
			}

			while (!sbKingClient.isDealFinished() && !sbKingClient.newDealAvailable()) {
				sleepFor(100);
				if (sbKingClient.newDealAvailable()) {
					Deal currentDeal = sbKingClient.getDeal();

					logger.info("Starting to paint Deal");
					paintDeal(currentDeal, sbKingClient.getDirection(), sbKingClient.getPlayCardActionListener());
					logger.info("Finished painting Deal");
				}

			}

			logger.info("Sleeping for 2000 ms to wait for FINISHGAME");
			sleepFor(2000);

		}

		logger.info("Starting to paint paintFinalScoreboardScreen");
		paintFinalScoreboardScreen();
		logger.info("Finished painting paintFinalScoreboardScreen");

		logger.info("Final scoreboard painted. Waiting for 10 seconds before exiting.");
		sleepFor(10000);
		logger.info("Game finished!");
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

	private void paintWaitingForPlayersScreen() {
		Painter waitingForPlayersPainter = new WaitingForPlayersPainter(sbKingClient.getDirection());
		paintPainter(waitingForPlayersPainter);
	}

	private void paintWaitingForChoosingPositiveOrNegativeScreen(Direction myDirection, Direction chooserDirection) {
		Painter waitingForChoosingPainter = new WaitingForChoosingPositiveOrNegativePainter(myDirection,
				chooserDirection, this.sbKingClient, this.sbKingClient.getCurrentGameScoreboard());
		paintPainter(waitingForChoosingPainter);

	}

	private void paintWaitingForChoosingGameModeOrStrainScreen(Direction direction, Direction chooser,
			boolean isPositive) {
		Painter waitingForChoosingGameModeOrStrainPainter = new WaitingForChoosingGameModeOrStrainPainter(direction,
				chooser, isPositive, this.sbKingClient, this.sbKingClient.getCurrentGameScoreboard());
		paintPainter(waitingForChoosingGameModeOrStrainPainter);
	}

	private void paintDeal(Deal deal, Direction direction, ActionListener playCardActionListener) {
		Painter dealPainter = new DealPainter(playCardActionListener, direction, deal);
		paintPainter(dealPainter);
	}

	private void paintFinalScoreboardScreen() {
		Painter finalScoreboardPainter = new FinalScoreboardPainter(this.sbKingClient.getCurrentGameScoreboard());
		paintPainter(finalScoreboardPainter);
	}

	private void paintSpectatorScreen(Deal deal, ActionListener playCardActionListener) {
		if(deal != null){
			Painter spectatorPainter = new SpectatorPainter(playCardActionListener, deal);
			paintPainter(spectatorPainter);
		}
	}

	public void connectToServer(String nickname, String hostname) {
		this.sbKingClient = new SBKingClient(nickname, hostname);
		this.connectedToServer = true;
		pool.execute(this.sbKingClient);
	}

}
