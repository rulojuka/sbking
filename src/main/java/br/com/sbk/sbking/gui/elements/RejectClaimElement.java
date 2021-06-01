package br.com.sbk.sbking.gui.elements;

import java.awt.Container;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JButton;

import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.gui.jelements.RejectClaimButton;

public class RejectClaimElement {

    public RejectClaimElement(Direction claimer, Direction currentDirection, Boolean isPartnershipGame,
            Map<Direction, Boolean> acceptedClaimMap, Container container, Point point, ActionListener actionListener) {
        JButton rejectClaimButton = new RejectClaimButton();
        rejectClaimButton.addActionListener(actionListener);
        if (claimer != null && !currentDirection.equals(claimer) && !acceptedClaimMap.get(currentDirection)) {
            if (isPartnershipGame) {
                boolean isClaimerPartner = currentDirection.equals(claimer.next(2));
                if (!isClaimerPartner) {
                    rejectClaimButton.setText("REJECT CLAIM");
                    rejectClaimButton.setBounds(point.x - 100, point.y, 200, 30);
                    container.add(rejectClaimButton);
                }
            } else {
                rejectClaimButton.setText("REJECT CLAIM");
                rejectClaimButton.setBounds(point.x - 100, point.y, 200, 30);
                container.add(rejectClaimButton);
            }
        }
    }
}
