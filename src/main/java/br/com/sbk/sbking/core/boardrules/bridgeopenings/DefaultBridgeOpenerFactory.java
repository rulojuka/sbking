package br.com.sbk.sbking.core.boardrules.bridgeopenings;

import java.util.ArrayList;

import br.com.sbk.sbking.core.boardrules.BoardRule;
import br.com.sbk.sbking.core.boardrules.BridgeOpener;

public class DefaultBridgeOpenerFactory {

  private static ArrayList<BoardRule> boardRules;

  static {
    boardRules = new ArrayList<BoardRule>();

    boardRules.add(new DealerHasTwoClubsOpeningBoardRule());

    boardRules.add(new DealerHasOneNoTrumpOpeningBoardRule());
    boardRules.add(new DealerHasTwoNoTrumpOpeningBoardRule());

    boardRules.add(new DealerHasFourWeakOpeningBoardRule());
    boardRules.add(new DealerHasThreeWeakOpeningBoardRule());
    boardRules.add(new DealerHasTwoWeakOpeningBoardRule());

    boardRules.add(new DealerHasOneMajorOpeningBoardRule());
    boardRules.add(new DealerHasOneMinorOpeningBoardRule());

  }

  public static BridgeOpener getBridgeOpener() {
    return new BridgeOpener(boardRules);
  }

}
