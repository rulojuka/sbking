package br.com.sbk.sbking.gui.elements;

import static javax.swing.SwingConstants.CENTER;
import static javax.swing.SwingConstants.LEFT;

import java.awt.Container;
import java.awt.Font;
import java.awt.Point;

import javax.swing.JLabel;

import br.com.sbk.sbking.core.rulesets.abstractrulesets.Ruleset;
import br.com.sbk.sbking.core.rulesets.concrete.NoRuleset;
import br.com.sbk.sbking.core.rulesets.concrete.PositiveWithTrumpsRuleset;
import br.com.sbk.sbking.gui.constants.FrameConstants;
import br.com.sbk.sbking.gui.jelements.SBKingLabel;

public class RulesetElement {

    private static final int DEFAULT_FONT_SIZE = 40;
    private static double scaleFactor;

    public RulesetElement(Ruleset ruleset, Container container, Point point) {
        JLabel rulesetLabel = createLabel(ruleset);
        rulesetLabel.setLocation(point);
        container.add(rulesetLabel);
    }

    private JLabel createLabel(Ruleset ruleset) {
        SBKingLabel label;
        scaleFactor = FrameConstants.getScreenScale();
        int fontSize = (int) (scaleFactor * DEFAULT_FONT_SIZE);
        if (ruleset instanceof NoRuleset) {
            label = new SBKingLabel("Choosing ruleset.");
            label.setFont(new Font("Verdana", Font.PLAIN, fontSize));
            label.setHorizontalAlignment(LEFT);
            label.setSize(300, 100);
        } else {
            label = new SBKingLabel(ruleset.getShortDescription());
            label.setFont(new Font("Verdana", Font.PLAIN, 2 * fontSize));
            if (ruleset instanceof PositiveWithTrumpsRuleset) {
                PositiveWithTrumpsRuleset positiveWithTrumpsRuleset = (PositiveWithTrumpsRuleset) ruleset;
                label.setForeground(positiveWithTrumpsRuleset.getTrumpSuit().getColor());
            }
            label.setHorizontalAlignment(CENTER);
            label.setSize(300, 100);
        }

        return label;
    }
}
