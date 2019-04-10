package br.com.sbk.sbking.gui.elements;

import static br.com.sbk.sbking.gui.constants.FrameConstants.EAST_X;
import static br.com.sbk.sbking.gui.constants.FrameConstants.EAST_Y;
import static br.com.sbk.sbking.gui.constants.FrameConstants.NORTH_X;
import static br.com.sbk.sbking.gui.constants.FrameConstants.NORTH_Y;
import static br.com.sbk.sbking.gui.constants.FrameConstants.SOUTH_X;
import static br.com.sbk.sbking.gui.constants.FrameConstants.SOUTH_Y;
import static br.com.sbk.sbking.gui.constants.FrameConstants.WEST_X;
import static br.com.sbk.sbking.gui.constants.FrameConstants.WEST_Y;
import static javax.swing.SwingConstants.CENTER;

import java.awt.Container;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JRadioButton;

import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.core.rulesets.NegativeRulesetsEnum;
import br.com.sbk.sbking.core.rulesets.abstractClasses.NegativeRuleset;
import br.com.sbk.sbking.networking.SBKingClient;

public class ChooseGameModeOrStrainElement {

	private Container container;
	private Direction direction;
	private JRadioButton currentButton;
	private List<JRadioButton> radioButtons;
	private SBKingClient sbKingClient;
	private int initial_y;
	private int initial_x;
	private int width;
	private int height;
	private boolean isPositive;

	public ChooseGameModeOrStrainElement(Container container, Direction direction, SBKingClient sbKingClient,
			boolean isPositive) {
		this.container = container;
		this.direction = direction;
		this.sbKingClient = sbKingClient;
		this.isPositive = isPositive;
	}

	public void add() {
		addLabel();
		addRadioButtons();
	}

	private void addRadioButtons() {
		width = 160;
		height = 20;

		if (Direction.NORTH == direction) {
			initial_x = NORTH_X;
			initial_y = NORTH_Y;
		} else if (Direction.EAST == direction) {
			initial_x = EAST_X;
			initial_y = EAST_Y;
		} else if (Direction.SOUTH == direction) {
			initial_x = SOUTH_X;
			initial_y = SOUTH_Y;
		} else {
			initial_x = WEST_X;
			initial_y = WEST_Y;
		}

		initial_y += 25;
		ButtonGroup bg = new ButtonGroup();

		if (this.isPositive) {

		} else {
			int y = initial_y;
			radioButtons = new ArrayList<JRadioButton>();
			for (NegativeRulesetsEnum negativeRulesetEnumElement : NegativeRulesetsEnum.values()) {
				NegativeRuleset current = negativeRulesetEnumElement.getNegativeRuleset();
				currentButton = new JRadioButton(current.getShortDescription());
				System.out.println("bounds:" + initial_x + "**" + y + "**" + width + "**" + height);
				currentButton.setBounds(initial_x, y, width, height);
				container.add(currentButton);
				radioButtons.add(currentButton);

				y += 20;

				bg.add(currentButton);

			}

		}

		JButton selectGameModeOrStrandButton = new JButton();
		selectGameModeOrStrandButton.addActionListener(new NegativeSelectActionListener());
		selectGameModeOrStrandButton.setBounds(initial_x + width + 10, initial_y, 80, height * 6);
		selectGameModeOrStrandButton.setText("Select");
		container.add(selectGameModeOrStrandButton);

	}

	class NegativeSelectActionListener implements java.awt.event.ActionListener {
		public void actionPerformed(java.awt.event.ActionEvent event) {

			JRadioButton selectedOnRadio = null;
			for (JRadioButton jRadioButton : radioButtons) {
				if (jRadioButton.isSelected()) {
					selectedOnRadio = jRadioButton;
					break;
				}
			}
			String text = selectedOnRadio.getText();
			NegativeRuleset negativeRuleset = null;
			for (NegativeRulesetsEnum negativeRulesetEnumElement : NegativeRulesetsEnum.values()) {
				NegativeRuleset current = negativeRulesetEnumElement.getNegativeRuleset();
				if (text.equals(current.getShortDescription())) {
					negativeRuleset = current;
					break;
				}
			}

			sbKingClient.sendNegativeRuleset(negativeRuleset);

		}
	}

	private void addLabel() {
		JLabel waitingLabel = new JLabel("<html>Please choose:</html>");
		waitingLabel.setHorizontalAlignment(CENTER);
		int width = 300;
		int height = 15;
		int x;
		int y;
		if (Direction.NORTH == direction) {
			x = NORTH_X;
			y = NORTH_Y;
		} else if (Direction.EAST == direction) {
			x = EAST_X;
			y = EAST_Y;
		} else if (Direction.SOUTH == direction) {
			x = SOUTH_X;
			y = SOUTH_Y;
		} else {
			x = WEST_X;
			y = WEST_Y;
		}
		waitingLabel.setSize(width, height);
		waitingLabel.setLocation(x, y);
		// rulesetLabel.setForeground(RULESET_ELEMENT_COLOR);
		container.add(waitingLabel);
	}

}
