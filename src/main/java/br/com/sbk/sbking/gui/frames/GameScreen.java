package br.com.sbk.sbking.gui.frames;

import static br.com.sbk.sbking.gui.constants.FrameConstants.TABLE_COLOR;
import static br.com.sbk.sbking.gui.constants.FrameConstants.TABLE_HEIGHT;
import static br.com.sbk.sbking.gui.constants.FrameConstants.TABLE_WIDTH;

import javax.swing.JFrame;

import br.com.sbk.sbking.core.Game;
import br.com.sbk.sbking.core.rulesets.abstractClasses.Ruleset;
import br.com.sbk.sbking.gui.painters.GameModePainter;
import br.com.sbk.sbking.gui.painters.GameSelectPainter;

@SuppressWarnings("serial")
public class GameScreen extends JFrame {

	private Game game;
	private boolean gameSelected = false;
	private Ruleset ruleset;

	public GameScreen() {
		super();
		initializeJFrame();
		initializeContentPane();
		this.game = new Game();
	}

	private void initializeJFrame() {
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(TABLE_WIDTH, TABLE_HEIGHT);
	}

	private void initializeContentPane() {
		this.getContentPane().setLayout(null);
		this.getContentPane().setBackground(TABLE_COLOR);
	}

	public void run() {
		this.paintGameSelectScreen();
		while (!gameSelected) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.paintGameMode(this.ruleset);
	}

	private void paintGameSelectScreen() {
		cleanContentPane();
		GameSelectPainter gameSelectScreen = new GameSelectPainter(this);
		gameSelectScreen.paint(this.getContentPane());
	}

	private void paintGameMode(Ruleset ruleset) {
		cleanContentPane();
		GameModePainter gameModePainter = new GameModePainter(this, ruleset);
		gameModePainter.paint(this.getContentPane());
	}

	private void cleanContentPane() {
		this.getContentPane().removeAll();
	}

	public void selectGame(Ruleset ruleset) {
		this.ruleset = ruleset;
		this.gameSelected = true;
	}

}
