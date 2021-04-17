package br.com.sbk.sbking.networking.kryonet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Registration;
import com.esotericsoftware.kryonet.EndPoint;

import org.objenesis.instantiator.ObjectInstantiator;

import br.com.sbk.sbking.core.Board;
import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.core.GameModeSummary;
import br.com.sbk.sbking.core.Hand;
import br.com.sbk.sbking.core.Player;
import br.com.sbk.sbking.core.Rank;
import br.com.sbk.sbking.core.Score;
import br.com.sbk.sbking.core.Suit;
import br.com.sbk.sbking.core.Trick;
import br.com.sbk.sbking.core.cardComparators.CardInsideHandComparator;
import br.com.sbk.sbking.core.cardComparators.CardInsideHandWithSuitComparator;
import br.com.sbk.sbking.core.cardComparators.RankComparator;
import br.com.sbk.sbking.core.rulesets.concrete.NegativeHeartsRuleset;
import br.com.sbk.sbking.core.rulesets.concrete.NegativeKingRuleset;
import br.com.sbk.sbking.core.rulesets.concrete.NegativeLastTwoRuleset;
import br.com.sbk.sbking.core.rulesets.concrete.NegativeMenRuleset;
import br.com.sbk.sbking.core.rulesets.concrete.NegativeTricksRuleset;
import br.com.sbk.sbking.core.rulesets.concrete.NegativeWomenRuleset;
import br.com.sbk.sbking.core.rulesets.concrete.NoRuleset;
import br.com.sbk.sbking.core.rulesets.concrete.PositiveNoTrumpsRuleset;
import br.com.sbk.sbking.core.rulesets.concrete.PositiveWithTrumpsRuleset;
import br.com.sbk.sbking.core.rulesets.implementations.DefaultSuitFollowable;
import br.com.sbk.sbking.core.rulesets.implementations.DontProhibitsHearts;
import br.com.sbk.sbking.core.rulesets.implementations.NoTrumpSuitWinnable;
import br.com.sbk.sbking.core.rulesets.implementations.ProhibitsHearts;
import br.com.sbk.sbking.core.rulesets.implementations.TrumpSuitWinnable;
import br.com.sbk.sbking.gui.models.KingGameScoreboard;
import br.com.sbk.sbking.networking.kryonet.messages.ClientToServer.ChooseGameModeOrStrainMessage;
import br.com.sbk.sbking.networking.kryonet.messages.ClientToServer.ChooseNegativeMessage;
import br.com.sbk.sbking.networking.kryonet.messages.ClientToServer.ChoosePositiveMessage;
import br.com.sbk.sbking.networking.kryonet.messages.ClientToServer.CreateTableMessage;
import br.com.sbk.sbking.networking.kryonet.messages.ClientToServer.MoveToSeatMessage;
import br.com.sbk.sbking.networking.kryonet.messages.ClientToServer.PlayCardMessage;
import br.com.sbk.sbking.networking.kryonet.messages.ClientToServer.SetNicknameMessage;
import br.com.sbk.sbking.networking.kryonet.messages.ClientToServer.UndoMessage;
import br.com.sbk.sbking.networking.kryonet.messages.ServerToClient.BoardMessage;
import br.com.sbk.sbking.networking.kryonet.messages.ServerToClient.DealMessage;
import br.com.sbk.sbking.networking.kryonet.messages.ServerToClient.FinishDealMessage;
import br.com.sbk.sbking.networking.kryonet.messages.ServerToClient.GameModeOrStrainChooserMessage;
import br.com.sbk.sbking.networking.kryonet.messages.ServerToClient.InitializeDealMessage;
import br.com.sbk.sbking.networking.kryonet.messages.ServerToClient.InvalidRulesetMessage;
import br.com.sbk.sbking.networking.kryonet.messages.ServerToClient.IsNotSpectatorMessage;
import br.com.sbk.sbking.networking.kryonet.messages.ServerToClient.IsSpectatorMessage;
import br.com.sbk.sbking.networking.kryonet.messages.ServerToClient.PositiveOrNegativeChooserMessage;
import br.com.sbk.sbking.networking.kryonet.messages.ServerToClient.PositiveOrNegativeMessage;
import br.com.sbk.sbking.networking.kryonet.messages.ServerToClient.TextMessage;
import br.com.sbk.sbking.networking.kryonet.messages.ServerToClient.ValidRulesetMessage;
import br.com.sbk.sbking.networking.kryonet.messages.ServerToClient.YourDirectionIsMessage;

public class KryonetUtils {
  // This registers objects that are going to be sent over the network.
  public static void register(EndPoint endPoint) {
    Kryo kryo = endPoint.getKryo();

    // Java classes
    kryo.register(ArrayList.class);
    kryo.register(HashMap.class);
    Registration uuidRegistration = kryo.register(UUID.class);
    uuidRegistration.setInstantiator(new ObjectInstantiator<UUID>() {
      public UUID newInstance() {
        return new UUID(0, 0);
      }
    });

    // Core classes
    kryo.register(Board.class);
    kryo.register(Card.class);
    kryo.register(Deal.class);
    kryo.register(Direction.class);
    kryo.register(GameModeSummary.class);
    kryo.register(Hand.class);
    kryo.register(KingGameScoreboard.class);
    kryo.register(Player.class);
    kryo.register(Rank.class);
    kryo.register(Score.class);
    kryo.register(Suit.class);
    kryo.register(Trick.class);
    // Concrete Rulesets
    kryo.register(NegativeHeartsRuleset.class);
    kryo.register(NegativeKingRuleset.class);
    kryo.register(NegativeLastTwoRuleset.class);
    kryo.register(NegativeMenRuleset.class);
    kryo.register(NegativeTricksRuleset.class);
    kryo.register(NegativeWomenRuleset.class);
    kryo.register(NoRuleset.class);
    kryo.register(PositiveNoTrumpsRuleset.class);
    kryo.register(PositiveWithTrumpsRuleset.class);
    // Rulesets implementations
    kryo.register(DefaultSuitFollowable.class);
    kryo.register(DontProhibitsHearts.class);
    kryo.register(NoTrumpSuitWinnable.class);
    kryo.register(ProhibitsHearts.class);
    kryo.register(TrumpSuitWinnable.class);
    // Card comparators
    kryo.register(CardInsideHandComparator.class);
    kryo.register(CardInsideHandWithSuitComparator.class);
    kryo.register(RankComparator.class);

    // Server to Client Message classes
    kryo.register(BoardMessage.class);
    kryo.register(DealMessage.class);
    kryo.register(FinishDealMessage.class);
    kryo.register(GameModeOrStrainChooserMessage.class);
    kryo.register(InitializeDealMessage.class);
    kryo.register(InvalidRulesetMessage.class);
    kryo.register(IsNotSpectatorMessage.class);
    kryo.register(IsSpectatorMessage.class);
    kryo.register(PositiveOrNegativeChooserMessage.class);
    kryo.register(PositiveOrNegativeMessage.class);
    kryo.register(TextMessage.class);
    kryo.register(UndoMessage.class);
    kryo.register(ValidRulesetMessage.class);
    kryo.register(YourDirectionIsMessage.class);

    // Client to Server Message classes
    kryo.register(ChooseGameModeOrStrainMessage.class);
    kryo.register(ChooseNegativeMessage.class);
    kryo.register(ChoosePositiveMessage.class);
    kryo.register(CreateTableMessage.class);
    kryo.register(MoveToSeatMessage.class);
    kryo.register(PlayCardMessage.class);
    kryo.register(SetNicknameMessage.class);
  }
}
