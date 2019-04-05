package br.com.sbk.sbking.gui.painters;

import java.awt.Container;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JRadioButton;

import br.com.sbk.sbking.core.Suit;
import br.com.sbk.sbking.core.rulesets.abstractClasses.Ruleset;
import br.com.sbk.sbking.core.rulesets.concrete.NegativeHeartsRuleset;
import br.com.sbk.sbking.core.rulesets.concrete.NegativeKingRuleset;
import br.com.sbk.sbking.core.rulesets.concrete.NegativeLastTwoRuleset;
import br.com.sbk.sbking.core.rulesets.concrete.NegativeMenRuleset;
import br.com.sbk.sbking.core.rulesets.concrete.NegativeTricksRuleset;
import br.com.sbk.sbking.core.rulesets.concrete.NegativeWomenRuleset;
import br.com.sbk.sbking.core.rulesets.concrete.PositiveNoTrumpsRuleset;
import br.com.sbk.sbking.core.rulesets.concrete.PositiveWithTrumpsRuleset;
import br.com.sbk.sbking.gui.frames.GameScreen;

public class GameSelectPainter {

	private List<JRadioButton> gameButtons = new ArrayList<JRadioButton>();
	private JRadioButton gameButton;
	private Ruleset negativeTricks = new NegativeTricksRuleset();
	private Ruleset negativeHearts = new NegativeHeartsRuleset();
	private Ruleset negativeMen = new NegativeMenRuleset();
	private Ruleset negativeWomen = new NegativeWomenRuleset();
	private Ruleset negativeLastTwo = new NegativeLastTwoRuleset();
	private Ruleset negativeKing = new NegativeKingRuleset();
	private Ruleset positiveNoTrumps = new PositiveNoTrumpsRuleset();
	private Ruleset positiveClubs = new PositiveWithTrumpsRuleset(Suit.CLUBS);
	private Ruleset positiveDiamonds = new PositiveWithTrumpsRuleset(Suit.DIAMONDS);
	private Ruleset positiveHearts = new PositiveWithTrumpsRuleset(Suit.HEARTS);
	private Ruleset positiveSpades = new PositiveWithTrumpsRuleset(Suit.SPADES);
	private List<Ruleset> rulesets;
	private GameScreen gameScreen;

	public GameSelectPainter(GameScreen gameScreen) {
		this.gameScreen = gameScreen;
	}

	public void paint(Container contentPane) {

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
			contentPane.add(currentButton);
		}

		JButton playGameButton = new JButton();
		contentPane.add(playGameButton);

		playGameButton.addActionListener(new GameSelectActionListener());
		playGameButton.setBounds(initial_x + width + 10, initial_y, width / 2, height * rulesets.size());
		playGameButton.setText("Play");

		contentPane.validate();
		contentPane.repaint();
	}

	private void callGameModePainter(Ruleset ruleset) {
		this.gameScreen.selectGame(ruleset);
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
			callGameModePainter(ruleset);
		}
	}

}
