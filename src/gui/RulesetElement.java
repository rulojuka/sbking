package gui;

import static javax.swing.SwingConstants.LEFT;

import java.awt.Container;
import java.awt.Point;

import javax.swing.JLabel;

import core.rulesets.Ruleset;

public class RulesetElement {

	public RulesetElement(Ruleset ruleset, Container container, Point point) {

		JLabel rulesetLabel = new JLabel("Playing " + ruleset.getShortDescription() + ".");
		rulesetLabel.setHorizontalAlignment(LEFT);
		rulesetLabel.setSize(300, 15);
		rulesetLabel.setLocation(point);
		container.add(rulesetLabel);
	}
}
