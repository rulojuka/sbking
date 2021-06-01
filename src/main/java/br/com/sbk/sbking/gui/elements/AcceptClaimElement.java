package br.com.sbk.sbking.gui.elements;

import java.awt.Container;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JButton;

import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.gui.jelements.AcceptClaimButton;

public class AcceptClaimElement {

    public AcceptClaimElement(Direction claimer, Direction currentDirection, Boolean isPartnershipGame,
            Map<Direction, Boolean> acceptedClaimMap, Container container, Point point, ActionListener actionListener) {
        JButton acceptClaimButton = new AcceptClaimButton();
        acceptClaimButton.addActionListener(actionListener);
        if (claimer != null && !currentDirection.equals(claimer) && !acceptedClaimMap.get(currentDirection)) {
            if (isPartnershipGame) {
                boolean isClaimerPartner = currentDirection.equals(claimer.next(2));
                if (!isClaimerPartner) {
                    acceptClaimButton.setText("ACCEPT CLAIM");
                    acceptClaimButton.setBounds(point.x - 100, point.y - 50, 200, 30);
                    container.add(acceptClaimButton);
                }
            } else {
                acceptClaimButton.setText("ACCEPT CLAIM");
                acceptClaimButton.setBounds(point.x - 100, point.y - 50, 200, 30);
                container.add(acceptClaimButton);
            }
        }
    }
}
