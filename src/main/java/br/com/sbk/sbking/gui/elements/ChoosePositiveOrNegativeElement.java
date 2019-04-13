package br.com.sbk.sbking.gui.elements;

import static br.com.sbk.sbking.gui.constants.FrameConstants.HALF_HEIGHT;
import static br.com.sbk.sbking.gui.constants.FrameConstants.HALF_WIDTH;

import java.awt.Container;
import java.awt.Point;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JRadioButton;

import br.com.sbk.sbking.networking.client.SBKingClient;

public class ChoosePositiveOrNegativeElement {

	private Container container;
	private JRadioButton positiveButton;
	private JRadioButton negativeButton;
	private SBKingClient sbKingClient;
	private int numberOfElements;
	private int elementWidth;
	private int elementHeight;
	private int xSpacing;
	private int yLabelOffset;
	
	private static int BUTTON_WIDTH = 80;

	public ChoosePositiveOrNegativeElement(Container container, SBKingClient sbKingClient) {
		this.container = container;
		this.sbKingClient = sbKingClient;
		this.numberOfElements = 2;
		this.elementWidth = 90;
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

		positiveButton = new JRadioButton("Positive");
		positiveButton.setBounds(x, y, elementWidth, elementHeight);
		container.add(positiveButton);
		bg.add(positiveButton);

		y += elementHeight;

		negativeButton = new JRadioButton("Negative");
		negativeButton.setBounds(x, y, elementWidth, elementHeight);
		container.add(negativeButton);

		bg.add(negativeButton);

		JButton selectPositiveOrNegativeButton = new JButton();

		Point selectButtonPosition = new Point(buttonsPosition);
		selectButtonPosition.translate(elementWidth + xSpacing, 0);
		selectPositiveOrNegativeButton.addActionListener(new PositiveOrNegativeSelectActionListener());
		selectPositiveOrNegativeButton.setLocation(selectButtonPosition);
		selectPositiveOrNegativeButton.setSize(BUTTON_WIDTH, elementHeight * numberOfElements);
		selectPositiveOrNegativeButton.setText("Select");
		container.add(selectPositiveOrNegativeButton);
	}

	class PositiveOrNegativeSelectActionListener implements java.awt.event.ActionListener {
		public void actionPerformed(java.awt.event.ActionEvent event) {
			if (positiveButton.isSelected()) {
				sbKingClient.sendPositive();
			} else {
				sbKingClient.sendNegative();
			}
		}
	}

	private void addLabel(Point labelPosition) {
		int labelWidth = 120;
		JLabel waitingLabel = new JLabel("Please choose:");
		waitingLabel.setSize(labelWidth, elementHeight);
		waitingLabel.setLocation(labelPosition);
		container.add(waitingLabel);
	}

}
