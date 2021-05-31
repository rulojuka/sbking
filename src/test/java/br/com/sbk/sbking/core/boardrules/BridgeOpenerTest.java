package br.com.sbk.sbking.core.boardrules;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.sbk.sbking.core.Board;
import br.com.sbk.sbking.core.BridgeContract;
import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.core.Hand;
import br.com.sbk.sbking.core.HandEvaluations;
import br.com.sbk.sbking.core.Rank;
import br.com.sbk.sbking.core.Strain;
import br.com.sbk.sbking.core.Suit;
import br.com.sbk.sbking.core.boarddealer.ShuffledBoardDealer;
import br.com.sbk.sbking.core.boardrules.bridgeopenings.DealerHasFourWeakOpeningBoardRule;
import br.com.sbk.sbking.core.boardrules.bridgeopenings.DealerHasOneMajorOpeningBoardRule;
import br.com.sbk.sbking.core.boardrules.bridgeopenings.DealerHasOneMinorOpeningBoardRule;
import br.com.sbk.sbking.core.boardrules.bridgeopenings.DealerHasOneNoTrumpOpeningBoardRule;
import br.com.sbk.sbking.core.boardrules.bridgeopenings.DealerHasThreeWeakOpeningBoardRule;
import br.com.sbk.sbking.core.boardrules.bridgeopenings.DealerHasTwoClubsOpeningBoardRule;
import br.com.sbk.sbking.core.boardrules.bridgeopenings.DealerHasTwoNoTrumpOpeningBoardRule;
import br.com.sbk.sbking.core.boardrules.bridgeopenings.DealerHasTwoWeakOpeningBoardRule;
import br.com.sbk.sbking.core.boardrules.bridgeopenings.DefaultBridgeOpenerFactory;

public class BridgeOpenerTest {

  private BridgeOpener subject;

  @Mock
  private Hand hand;
  @Mock
  private HandEvaluations handEvaluations;

  private void configureParameterizedMocks(int hcp, boolean isBalanced, boolean hasFiveCardMajorSuit) {
    when(handEvaluations.getHCP()).thenReturn(hcp);
    when(handEvaluations.isBalanced()).thenReturn(isBalanced);
    when(handEvaluations.hasFiveOrMoreCardsInAMajorSuit()).thenReturn(hasFiveCardMajorSuit);
  }

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    configureParameterizedMocks(16, true, false);
    when(hand.getHandEvaluations()).thenReturn(handEvaluations);
    ArrayList<BoardRule> boardRules = new ArrayList<BoardRule>();
    boardRules.add(new DealerHasOneNoTrumpOpeningBoardRule());
    subject = new BridgeOpener(boardRules);
  }

  @Test
  public void mockedTest() {
    BridgeContract opening = subject.getOpening(hand);
    assertEquals(Strain.NOTRUMPS, opening.getStrain());
    assertEquals(1, opening.getLevel());
    assertEquals(false, opening.getDoubled());
    assertEquals(false, opening.getRedoubled());
  }

  public void randomStatisticsGenerator() {
    ShuffledBoardDealer shuffledBoardDealer = new ShuffledBoardDealer();
    Direction dealer = Direction.NORTH;
    BridgeOpener bridgeOpener = DefaultBridgeOpenerFactory.getBridgeOpener();
    BridgeContract opening;

    Map<String, Long> counting = new HashMap<String, Long>();
    Long total = 100000L;

    for (int i = 0; i < total; i++) {
      Board dealBoard = shuffledBoardDealer.dealBoard(dealer);
      opening = bridgeOpener.getOpening(dealBoard.getHandOf(dealer));
      String contract = opening.getLevel() + " " + opening.getStrain().getName();

      if (opening.getLevel() == 7) {
        contract = "PASS";
      }
      Long current = counting.get(contract);
      if (current == null) {
        counting.put(contract, 1L);
      } else {
        counting.put(contract, current + 1);
      }
    }

    Long passes = counting.get("PASS");

    System.out.println("PASS: " + passes);

    counting.entrySet().stream().sorted(new Comparator<Map.Entry<String, Long>>() {
      @Override
      public int compare(Entry<String, Long> entry1, Entry<String, Long> entry2) {
        return entry2.getValue().compareTo(entry1.getValue());
      }
    }).forEach(entry -> {
      if (!entry.getKey().equals("PASS")) {
        System.out.println(entry.getKey() + " " + this.getPercentage(entry.getValue(), total - passes));
      }
    });
  }

  private String getPercentage(Long number, Long total) {
    double value = (double) number / (double) total;
    double percent = value * 100.0;
    return "" + String.format("%.2f", percent) + "%";
  }

  @Test
  public void brokenTest() {
    Direction dealer = Direction.NORTH;

    Hand hand2 = new Hand();

    hand2.addCard(new Card(Suit.SPADES, Rank.ACE));
    hand2.addCard(new Card(Suit.SPADES, Rank.JACK));
    hand2.addCard(new Card(Suit.SPADES, Rank.TEN));
    hand2.addCard(new Card(Suit.SPADES, Rank.NINE));
    hand2.addCard(new Card(Suit.SPADES, Rank.SIX));
    hand2.addCard(new Card(Suit.SPADES, Rank.FOUR));

    hand2.addCard(new Card(Suit.HEARTS, Rank.JACK));

    hand2.addCard(new Card(Suit.CLUBS, Rank.NINE));
    hand2.addCard(new Card(Suit.CLUBS, Rank.SEVEN));
    hand2.addCard(new Card(Suit.CLUBS, Rank.TWO));

    hand2.addCard(new Card(Suit.DIAMONDS, Rank.KING));
    hand2.addCard(new Card(Suit.DIAMONDS, Rank.JACK));
    hand2.addCard(new Card(Suit.DIAMONDS, Rank.FIVE));

    Map<Direction, Hand> hands = new HashMap<Direction, Hand>();
    hands.put(dealer, hand2);
    hands.put(dealer.next(1), new Hand());
    hands.put(dealer.next(2), new Hand());
    hands.put(dealer.next(3), new Hand());

    Board dealBoard = new Board(hands, dealer);
    ArrayList<BoardRule> boardRules = new ArrayList<BoardRule>();

    boardRules.add(new DealerHasOneNoTrumpOpeningBoardRule());
    boardRules.add(new DealerHasTwoNoTrumpOpeningBoardRule());
    boardRules.add(new DealerHasFourWeakOpeningBoardRule());
    boardRules.add(new DealerHasThreeWeakOpeningBoardRule());
    boardRules.add(new DealerHasTwoWeakOpeningBoardRule());
    boardRules.add(new DealerHasTwoClubsOpeningBoardRule());
    boardRules.add(new DealerHasOneMajorOpeningBoardRule());
    boardRules.add(new DealerHasOneMinorOpeningBoardRule());

    BridgeOpener bridgeOpener = new BridgeOpener(boardRules);
    bridgeOpener.getOpening(dealBoard.getHandOf(dealer));

    BridgeContract opening = subject.getOpening(hand);

    assertEquals(Strain.NOTRUMPS, opening.getStrain());
    assertEquals(1, opening.getLevel());
    assertEquals(false, opening.getDoubled());
    assertEquals(false, opening.getRedoubled());
  }

}
