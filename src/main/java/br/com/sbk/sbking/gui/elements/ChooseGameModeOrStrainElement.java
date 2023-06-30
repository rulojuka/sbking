package br.com.sbk.sbking.gui.elements;

import java.awt.Container;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JRadioButton;

import br.com.sbk.sbking.core.Strain;
import br.com.sbk.sbking.core.rulesets.NegativeRulesetsEnum;
import br.com.sbk.sbking.core.rulesets.concrete.PositiveWithTrumpsRuleset;
import br.com.sbk.sbking.gui.constants.FrameConstants;
import br.com.sbk.sbking.gui.jelements.SBKingLabel;
import br.com.sbk.sbking.gui.models.TextWithColorAndFont;
import br.com.sbk.sbking.networking.client.SBKingClient;

public class ChooseGameModeOrStrainElement {

    private Container container;
    private List<JRadioButton> radioButtons;
    private SBKingClient sbKingClient;
    private boolean isPositive;

    private int numberOfElements;
    private int elementWidth;
    private int elementHeight;
    private int xSpacing;
    private int yLabelOffset;

    private static final int BUTTON_WIDTH = 80;

    public ChooseGameModeOrStrainElement(Container container, SBKingClient sbKingClient, boolean isPositive) {
        this.container = container;
        this.sbKingClient = sbKingClient;
        this.isPositive = isPositive;
        if (isPositive) {
            this.numberOfElements = Strain.values().length;
        } else {
            this.numberOfElements = NegativeRulesetsEnum.values().length;
        }
        this.elementWidth = 60;
    }

    public void add() {
        int initialX = FrameConstants.getHalfWidth();
        int initialY = FrameConstants.getHalfHeight();

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
        this.radioButtons = new ArrayList<JRadioButton>();
        int y = buttonsPosition.y;
        int x = buttonsPosition.x;

        List<TextWithColorAndFont> texts = new ArrayList<TextWithColorAndFont>();
        if (this.isPositive) {
            for (Strain strain : Strain.values()) {
                String newText = strain.getPositiveRuleset().getShortDescription();
                if (strain.getPositiveRuleset() instanceof PositiveWithTrumpsRuleset) {
                    PositiveWithTrumpsRuleset positiveWithTrumpsRuleset = (PositiveWithTrumpsRuleset) strain
                            .getPositiveRuleset();
                    java.awt.Color textColor = positiveWithTrumpsRuleset.getTrumpSuit().getColor();
                    texts.add(new TextWithColorAndFont(newText, textColor, 22, strain.getName()));
                } else {
                    texts.add(new TextWithColorAndFont(newText, strain.getName()));
                }
            }
        } else {
            for (NegativeRulesetsEnum negativeRulesetEnumElement : NegativeRulesetsEnum.values()) {
                String newText = negativeRulesetEnumElement.getNegativeRuleset().getShortDescription();
                texts.add(new TextWithColorAndFont(newText, newText));
            }
        }

        SBKingRadioButtonGroupCreator sbKingRadioButtonGroupCreator = new SBKingRadioButtonGroupCreator();
        ButtonGroup buttonGroup = sbKingRadioButtonGroupCreator.create(texts, x, y, this.elementWidth);
        for (Enumeration<AbstractButton> elements = buttonGroup.getElements(); elements.hasMoreElements();) {
            AbstractButton element = elements.nextElement();
            container.add(element);
            radioButtons.add((JRadioButton) element);
        }

        JButton selectGameModeOrStrainButton = new JButton();
        Point selectButtonPosition = new Point(buttonsPosition);
        selectButtonPosition.translate(elementWidth + xSpacing, 0);
        selectGameModeOrStrainButton.addActionListener(new GameModeOrStrainSelectActionListener());
        selectGameModeOrStrainButton.setLocation(selectButtonPosition);
        selectGameModeOrStrainButton.setSize(BUTTON_WIDTH, elementHeight * numberOfElements);
        selectGameModeOrStrainButton.setText("Select");
        container.add(selectGameModeOrStrainButton);
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
            if (selectedOnRadio != null) {
                sbKingClient.sendChooseGameModeOrStrain(selectedOnRadio.getToolTipText());
            }
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
