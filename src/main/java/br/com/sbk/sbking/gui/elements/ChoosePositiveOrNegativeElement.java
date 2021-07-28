package br.com.sbk.sbking.gui.elements;

import static br.com.sbk.sbking.gui.constants.FrameConstants.halfHeight;
import static br.com.sbk.sbking.gui.constants.FrameConstants.halfWidth;

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

import br.com.sbk.sbking.gui.jelements.SBKingLabel;
import br.com.sbk.sbking.gui.models.TextWithColor;
import br.com.sbk.sbking.networking.client.SBKingClient;

public class ChoosePositiveOrNegativeElement {

    private Container container;
    private List<JRadioButton> radioButtons;
    private SBKingClient sbKingClient;
    private int numberOfElements;
    private int elementWidth;
    private int elementHeight;
    private int xSpacing;
    private int yLabelOffset;
    List<TextWithColor> texts;

    private static final int BUTTON_WIDTH = 80;

    public ChoosePositiveOrNegativeElement(Container container, SBKingClient sbKingClient) {
        this.container = container;
        this.sbKingClient = sbKingClient;
        this.numberOfElements = 2;
        this.elementWidth = 160;
        this.texts = new ArrayList<TextWithColor>();
        texts.add(new TextWithColor("Positive"));
        texts.add(new TextWithColor("Negative"));
    }

    public void add() {
        int initialX = halfWidth;
        int initialY = halfHeight;

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

        radioButtons = new ArrayList<JRadioButton>();
        SBKingRadioButtonGroupCreator sbKingRadioButtonGroupCreator = new SBKingRadioButtonGroupCreator();
        ButtonGroup buttonGroup = sbKingRadioButtonGroupCreator.create(texts, x, y, 160);
        for (Enumeration<AbstractButton> elements = buttonGroup.getElements(); elements.hasMoreElements();) {
            AbstractButton element = elements.nextElement();
            container.add(element);
            radioButtons.add((JRadioButton) element);
        }

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
            JRadioButton selectedOnRadio = null;
            for (JRadioButton jRadioButton : radioButtons) {
                if (jRadioButton.isSelected()) {
                    selectedOnRadio = jRadioButton;
                    break;
                }
            }
            if (selectedOnRadio != null) {
                if (selectedOnRadio.getText().equals(texts.get(0))) {
                    sbKingClient.sendPositive();
                } else if (selectedOnRadio.getText().equals(texts.get(1))) {
                    sbKingClient.sendNegative();
                }
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
