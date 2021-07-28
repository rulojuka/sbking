package br.com.sbk.sbking.gui.elements;

import static javax.swing.SwingConstants.LEFT;
import static javax.swing.SwingConstants.CENTER;

import java.awt.Container;
import java.awt.Point;

import javax.swing.JLabel;

import br.com.sbk.sbking.core.rulesets.abstractrulesets.Ruleset;
import br.com.sbk.sbking.core.rulesets.concrete.NoRuleset;
import br.com.sbk.sbking.gui.jelements.SBKingLabel;

import java.awt.Font;

public class RulesetElement {

    public RulesetElement(Ruleset ruleset, Container container, Point point) {
        JLabel rulesetLabel = createLabel(ruleset);
        rulesetLabel.setLocation(point);
        container.add(rulesetLabel);
    }

    private JLabel createLabel(Ruleset ruleset) {
        SBKingLabel label;
        if (ruleset instanceof NoRuleset) {
            label = new SBKingLabel("Choosing ruleset.");
            label.setFont(new Font("Verdana", Font.PLAIN, 32));
            label.setHorizontalAlignment(LEFT);
            label.setSize(300, 100);
        } else {
            label = new SBKingLabel(ruleset.getShortDescription());
            label.setFont(new Font("Verdana", Font.PLAIN, 64));
            label.setHorizontalAlignment(CENTER);
            label.setSize(300, 100);
        }

        return label;
    }
}
