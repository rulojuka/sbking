package br.com.sbk.sbking.gui.elements;

import static br.com.sbk.sbking.gui.constants.FrameConstants.HALF_HEIGHT;
import static br.com.sbk.sbking.gui.constants.FrameConstants.HALF_WIDTH;

import java.awt.Container;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JRadioButton;

import br.com.sbk.sbking.core.Strain;
import br.com.sbk.sbking.core.rulesets.NegativeRulesetsEnum;
import br.com.sbk.sbking.gui.JElements.SBKingLabel;
import br.com.sbk.sbking.networking.client.SBKingClient;

public class ChooseGameModeOrStrainElement {

	private Container container;
	private JRadioButton currentButton;
	private List<JRadioButton> radioButtons;
	private SBKingClient sbKingClient;
	private boolean isPositive;

	private int numberOfElements;
	private int elementWidth;
	private int elementHeight;
	private int xSpacing;
	private int yLabelOffset;

	private static int BUTTON_WIDTH = 80;

	public ChooseGameModeOrStrainElement(Container container, SBKingClient sbKingClient, boolean isPositive) {
		this.container = container;
		this.sbKingClient = sbKingClient;
		this.isPositive = isPositive;
		if (isPositive) {
			this.numberOfElements = Strain.values().length;
		} else {
			this.numberOfElements = NegativeRulesetsEnum.values().length;
		}
		this.elementWidth = 160;
	}

	public void add() {
		int initialX = HALF_WIDTH;
		int initialY = HALF_HEIGHT;

		elementHeight = 20;
		xSpacing = 5;
		yLabelOffset = -elementHeight;

		int totalWidth = elementWidth + xSpacing + BUTTON_WIDTH;
		int totalHeight = elementHeight * numberOfElements;
		initialX -= totalWidth / 2;
		initialY -= totalHeight / 2;

		Point buttonPosition = new Point(initialX, initialY);
		this.addRadioButtons(buttonPosition);
		Point labelPosition = new Point(initialX, initialY);
		labelPosition.translate(0, yLabelOffset);
		this.addLabel(labelPosition);
	}

	private void addRadioButtons(Point buttonsPosition) {
		int y = buttonsPosition.y;
		int x = buttonsPosition.x;

		ButtonGroup bg = new ButtonGroup();

		List<String> texts = new ArrayList<String>();
		if (this.isPositive) {
			for (Strain strain : Strain.values()) {
				texts.add(strain.getPositiveRuleset().getShortDescription());
			}
		} else {
			for (NegativeRulesetsEnum negativeRulesetEnumElement : NegativeRulesetsEnum.values()) {
				texts.add(negativeRulesetEnumElement.getNegativeRuleset().getShortDescription());
			}
		}

		radioButtons = new ArrayList<JRadioButton>();
		for (String text : texts) {
			currentButton = new JRadioButton(text);
			currentButton.setBounds(x, y, elementWidth, elementHeight);
			container.add(currentButton);
			radioButtons.add(currentButton);

			y += elementHeight;

			bg.add(currentButton);

		}

		JButton selectGameModeOrStrandButton = new JButton();
		Point selectButtonPosition = new Point(buttonsPosition);
		selectButtonPosition.translate(elementWidth + xSpacing, 0);
		selectGameModeOrStrandButton.addActionListener(new GameModeOrStrainSelectActionListener());
		selectGameModeOrStrandButton.setLocation(selectButtonPosition);
		selectGameModeOrStrandButton.setSize(BUTTON_WIDTH, elementHeight * numberOfElements);
		selectGameModeOrStrandButton.setText("Select");
		container.add(selectGameModeOrStrandButton);
	}

	class GameModeOrStrainSelectActionListener implements java.awt.event.ActionListener {
		public void actionPerformed(java.awt.event.ActionEvent event) {
			JRadioButton selectedOnRadio = null;
			for (JRadioButton jRadioButton : radioButtons) {
				if (jRadioButton.isSelected()) {
					selectedOnRadio = jRadioButton;
					break;
				}
			}
			sbKingClient.sendGameModeOrStrain(selectedOnRadio.getText());
		}
	}

	private void addLabel(Point labelPosition) {
		int labelWidth = 120;
		JLabel waitingLabel = new SBKingLabel("Please choose:");
		waitingLabel.setSize(labelWidth, elementHeight);
		waitingLabel.setLocation(labelPosition);
		container.add(waitingLabel);
	}

}
