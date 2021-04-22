package br.com.sbk.sbking.gui.elements;

import java.awt.Container;
import java.awt.Point;
import java.awt.event.ActionListener;
import javax.swing.JButton;

import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.gui.JElements.ClaimButton;

public class ClaimElement {

    public ClaimElement(Direction claimer, Direction currentPlayer, Boolean isPartnershipGame, Container container,
            Point point, ActionListener actionListener) {
        JButton claimButton = new ClaimButton();
        claimButton.addActionListener(actionListener);
        if (claimer == null) {
            claimButton.setText("CLAIM");
            claimButton.setBounds(point.x, point.y, 100, 30);
            container.add(claimButton);
        } else {
            if (isPartnershipGame) {
                boolean claimerAndCurrentPlayerPartners = !(claimer.isNorthSouth() ^ currentPlayer.isNorthSouth());
                if (claimer != currentPlayer && !claimerAndCurrentPlayerPartners) {
                    claimButton.setText("ACCEPT CLAIM");
                    claimButton.setBounds(point.x - 100, point.y, 200, 30);
                    container.add(claimButton);
                }
            } else {
                if (claimer != currentPlayer) {
                    claimButton.setText("ACCEPT CLAIM");
                    claimButton.setBounds(point.x - 100, point.y, 200, 30);
                    container.add(claimButton);
                }
            }
        }
    }

}
