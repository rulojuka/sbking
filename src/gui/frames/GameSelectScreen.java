package gui.frames;

import java.awt.Container;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JRadioButton;

import core.Suit;
import core.rulesets.NegativeHeartsRuleset;
import core.rulesets.NegativeKingRuleset;
import core.rulesets.NegativeLastTwoRuleset;
import core.rulesets.NegativeMenRuleset;
import core.rulesets.NegativeTricksRuleset;
import core.rulesets.NegativeWomenRuleset;
import core.rulesets.PositiveNoTrumpsRuleset;
import core.rulesets.PositiveWithTrumpRuleset;
import core.rulesets.Ruleset;

@SuppressWarnings("serial")
public class GameSelectScreen extends JFrame {

	// Constants
	private final int WIDTH = 1024;
	private final int HEIGHT = 768;
	private final java.awt.Color TABLE_COLOR = new java.awt.Color(0, 100, 0); // Tablecloth green
	private List<JRadioButton> gameButtons = new ArrayList<JRadioButton>();
	private JRadioButton gameButton;
	private Ruleset negativeTricks = new NegativeTricksRuleset();
	private Ruleset negativeHearts = new NegativeHeartsRuleset();
	private Ruleset negativeMen = new NegativeMenRuleset();
	private Ruleset negativeWomen = new NegativeWomenRuleset();
	private Ruleset negativeLastTwo = new NegativeLastTwoRuleset();
	private Ruleset negativeKing = new NegativeKingRuleset();
	private Ruleset positiveNoTrumps = new PositiveNoTrumpsRuleset();
	private Ruleset positiveClubs = new PositiveWithTrumpRuleset(Suit.CLUBS);
	private Ruleset positiveDiamonds = new PositiveWithTrumpRuleset(Suit.DIAMONDS);
	private Ruleset positiveHearts = new PositiveWithTrumpRuleset(Suit.HEARTS);
	private Ruleset positiveSpades = new PositiveWithTrumpRuleset(Suit.SPADES);
	private List<Ruleset> rulesets;

	public GameSelectScreen() {
		super();
		initializeJFrame();
		initializeContentPane();
		Container contentPane = this.getContentPane();

		initializeSelectCombobox();

		contentPane.validate();
		contentPane.repaint();
	}

	private void initializeSelectCombobox() {

		rulesets = new ArrayList<Ruleset>();
		rulesets.add(negativeTricks);
		rulesets.add(negativeHearts);
		rulesets.add(negativeMen);
		rulesets.add(negativeWomen);
		rulesets.add(negativeLastTwo);
		rulesets.add(negativeKing);
		rulesets.add(positiveNoTrumps);
		rulesets.add(positiveClubs);
		rulesets.add(positiveDiamonds);
		rulesets.add(positiveHearts);
		rulesets.add(positiveSpades);

		int initial_y = 50;
		int initial_x = 75;
		int width = 170;
		int height = 20;

		int y = initial_y;

		for (Ruleset ruleset : rulesets) {
			gameButton = new JRadioButton(ruleset.getShortDescription());
			gameButton.setBounds(initial_x, y, width, height);
			gameButtons.add(gameButton);
			y += 20;
		}
		gameButtons.get(0).setSelected(true);

		ButtonGroup bg = new ButtonGroup();
		for (JRadioButton currentButton : gameButtons) {
			bg.add(currentButton);
			this.getContentPane().add(currentButton);
		}

		JButton playGameButton = new JButton();
		this.getContentPane().add(playGameButton);

		playGameButton.addActionListener(new GameSelectActionListener());
		playGameButton.setBounds(initial_x + width + 10, initial_y, width / 2, height * rulesets.size());
		playGameButton.setText("Play");
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

	class GameSelectActionListener implements java.awt.event.ActionListener {
		public void actionPerformed(java.awt.event.ActionEvent event) {
			Ruleset ruleset = negativeTricks;
			JRadioButton selectedButton = null;

			for (JRadioButton jRadioButton : gameButtons) {
				if (jRadioButton.isSelected())
					selectedButton = jRadioButton;
			}
			for (Ruleset currentRuleset : rulesets) {
				if (currentRuleset.getShortDescription().equals(selectedButton.getText())) {
					ruleset = currentRuleset;
				}
			}
			new GameMode(ruleset);
		}
	}

}
