package br.com.sbk.sbking.gui.elements;

import java.awt.Container;
import java.awt.Point;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import br.com.sbk.sbking.gui.constants.FrameConstants;

public class SpectatorsElement {

    public SpectatorsElement(Container container, Point point, List<String> spectators) {

        String textToShow = "";
        int width = 250;
        int height = 200;
        int xOffset = width / 2;
        int yOffset = height / 2;

        if (spectators != null) {
            StringBuilder spectatorNames = new StringBuilder();
            for (String name : spectators) {
                spectatorNames.append(name);
                spectatorNames.append("\n");
            }
            textToShow = spectatorNames.toString();
        }

        JTextArea spectatorsArea = new JTextArea(textToShow);
        spectatorsArea.setEnabled(false);
        spectatorsArea.setEditable(false);
        spectatorsArea.setBackground(FrameConstants.TABLE_COLOR);
        spectatorsArea.setForeground(FrameConstants.TEXT_COLOR);

        JScrollPane scrollPane = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setViewportView(spectatorsArea);
        scrollPane.setBounds(point.x - xOffset, point.y - yOffset, 250, 200);
        scrollPane.setBackground(FrameConstants.TABLE_COLOR);
        scrollPane.setForeground(FrameConstants.TEXT_COLOR);
        scrollPane.setBorder(BorderFactory
                .createCompoundBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Spectators"),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)), scrollPane.getBorder()));

        container.add(scrollPane);
    }
}
