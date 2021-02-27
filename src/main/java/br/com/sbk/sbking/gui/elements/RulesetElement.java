package br.com.sbk.sbking.gui.elements;

import static javax.swing.SwingConstants.LEFT;

import java.awt.Container;
import java.awt.Point;

import javax.swing.JLabel;

import br.com.sbk.sbking.core.rulesets.abstractClasses.Ruleset;
import br.com.sbk.sbking.gui.JElements.SBKingLabel;

public class RulesetElement {

	public RulesetElement(Ruleset ruleset, Container container, Point point) {

		JLabel rulesetLabel = new SBKingLabel("Playing " + ruleset.getShortDescription() + ".");
		rulesetLabel.setHorizontalAlignment(LEFT);
		rulesetLabel.setSize(300, 15);
		rulesetLabel.setLocation(point);
		container.add(rulesetLabel);
	}
}
