package br.com.sbk.sbking.gui.frames;

import java.awt.Container;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JRadioButton;

import br.com.sbk.sbking.core.Suit;
import br.com.sbk.sbking.core.rulesets.NegativeHeartsRuleset;
import br.com.sbk.sbking.core.rulesets.NegativeKingRuleset;
import br.com.sbk.sbking.core.rulesets.NegativeLastTwoRuleset;
import br.com.sbk.sbking.core.rulesets.NegativeMenRuleset;
import br.com.sbk.sbking.core.rulesets.NegativeTricksRuleset;
import br.com.sbk.sbking.core.rulesets.NegativeWomenRuleset;
import br.com.sbk.sbking.core.rulesets.PositiveNoTrumpsRuleset;
import br.com.sbk.sbking.core.rulesets.PositiveWithTrumpsRuleset;
import br.com.sbk.sbking.core.rulesets.abstractClasses.DefaultSuitFollowRuleset;

@SuppressWarnings("serial")
public class GameSelectScreen extends JFrame {

	// Constants
	private final int WIDTH = 1024;
	private final int HEIGHT = 768;
	private final java.awt.Color TABLE_COLOR = new java.awt.Color(0, 100, 0); // Tablecloth green
	private List<JRadioButton> gameButtons = new ArrayList<JRadioButton>();
	private JRadioButton gameButton;
	private DefaultSuitFollowRuleset negativeTricks = new NegativeTricksRuleset();
	private DefaultSuitFollowRuleset negativeHearts = new NegativeHeartsRuleset();
	private DefaultSuitFollowRuleset negativeMen = new NegativeMenRuleset();
	private DefaultSuitFollowRuleset negativeWomen = new NegativeWomenRuleset();
	private DefaultSuitFollowRuleset negativeLastTwo = new NegativeLastTwoRuleset();
	private DefaultSuitFollowRuleset negativeKing = new NegativeKingRuleset();
	private DefaultSuitFollowRuleset positiveNoTrumps = new PositiveNoTrumpsRuleset();
	private DefaultSuitFollowRuleset positiveClubs = new PositiveWithTrumpsRuleset(Suit.CLUBS);
	private DefaultSuitFollowRuleset positiveDiamonds = new PositiveWithTrumpsRuleset(Suit.DIAMONDS);
	private DefaultSuitFollowRuleset positiveHearts = new PositiveWithTrumpsRuleset(Suit.HEARTS);
	private DefaultSuitFollowRuleset positiveSpades = new PositiveWithTrumpsRuleset(Suit.SPADES);
	private List<DefaultSuitFollowRuleset> rulesets;

	public GameSelectScreen() {
		super();
		initializeJFrame();
		initializeContentPane();
		Container contentPane = this.getContentPane();

		initializeSelectCombobox();

		contentPane.validate();
		contentPane.repaint();
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

	private void initializeSelectCombobox() {

		rulesets = new ArrayList<DefaultSuitFollowRuleset>();
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

		for (DefaultSuitFollowRuleset ruleset : rulesets) {
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

	class GameSelectActionListener implements java.awt.event.ActionListener {
		public void actionPerformed(java.awt.event.ActionEvent event) {
			DefaultSuitFollowRuleset ruleset = negativeTricks;
			JRadioButton selectedButton = null;

			for (JRadioButton jRadioButton : gameButtons) {
				if (jRadioButton.isSelected())
					selectedButton = jRadioButton;
			}
			for (DefaultSuitFollowRuleset currentRuleset : rulesets) {
				if (currentRuleset.getShortDescription().equals(selectedButton.getText())) {
					ruleset = currentRuleset;
				}
			}
			new GameMode(ruleset);
		}
	}

}
