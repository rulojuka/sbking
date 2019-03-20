package br.com.sbk.sbking.gui.elements;

import static javax.swing.SwingConstants.LEFT;

import java.awt.Container;
import java.awt.Point;

import javax.swing.JLabel;

import br.com.sbk.sbking.core.rulesets.abstractClasses.Ruleset;

public class RulesetElement {

	private java.awt.Color RULESET_ELEMENT_COLOR = new java.awt.Color(255, 0, 0);

	public RulesetElement(Ruleset ruleset, Container container, Point point) {

		JLabel rulesetLabel = new JLabel("Playing " + ruleset.getShortDescription() + ".");
		rulesetLabel.setHorizontalAlignment(LEFT);
		rulesetLabel.setSize(300, 15);
		rulesetLabel.setLocation(point);
		rulesetLabel.setForeground(RULESET_ELEMENT_COLOR);
		container.add(rulesetLabel);
	}
}
