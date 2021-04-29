package br.com.sbk.sbking.gui.elements;

import java.awt.event.ActionListener;
import java.awt.Container;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.swing.JLabel;

import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.core.Player;
import br.com.sbk.sbking.dto.LobbyScreenTableDTO;
import br.com.sbk.sbking.gui.jelements.JoinTableButton;
import br.com.sbk.sbking.gui.jelements.SBKingLabel;

public class TableCardElement {

  public TableCardElement(Container container, LobbyScreenTableDTO table, Point position,
      ActionListener actionListener) {
    List<String> allLines = new ArrayList<String>();
    Map<Direction, Player> playersDirection = table.getPlayersDirection();

    String title = table.getGameName();
    allLines.add(title);

    for (Direction direction : Direction.values()) {
      String playerName;
      Player player = playersDirection.get(direction);
      if (player == null) {
        playerName = "EMPTY";
      } else {
        playerName = player.getNickname();
      }
      String playerLine = direction.getAbbreviation() + ": " + playerName;
      allLines.add(playerLine);
    }

    String footer = table.getNumberOfSpectators() + " spectators";
    allLines.add(footer);

    addAllLabels(container, position, allLines);
    addJoinButton(container, position, table.getId(), actionListener);
  }

  private void addAllLabels(Container container, Point position, List<String> allLines) {
    int labelWidth = 160;
    int elementHeight = 20;
    for (String string : allLines) {
      JLabel waitingLabel = new SBKingLabel(string);
      waitingLabel.setSize(labelWidth, elementHeight);
      waitingLabel.setLocation(position);
      position.translate(0, elementHeight);
      container.add(waitingLabel);
    }
  }

  private void addJoinButton(Container container, Point position, UUID tableId, ActionListener actionListener) {
    JoinTableButton joinTableButton = new JoinTableButton(tableId);
    joinTableButton.setLocation(position);
    joinTableButton.addActionListener(actionListener);
    container.add(joinTableButton);
  }

}
