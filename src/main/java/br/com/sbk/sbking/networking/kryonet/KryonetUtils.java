package br.com.sbk.sbking.networking.kryonet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.objenesis.instantiator.ObjectInstantiator;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Registration;
import com.esotericsoftware.kryonet.EndPoint;

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
import br.com.sbk.sbking.core.comparators.CardInsideHandComparator;
import br.com.sbk.sbking.core.comparators.CardInsideHandWithSuitComparator;
import br.com.sbk.sbking.core.comparators.RankComparator;
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
import br.com.sbk.sbking.dto.LobbyScreenTableDTO;
import br.com.sbk.sbking.gui.models.KingGameScoreboard;
import br.com.sbk.sbking.networking.kryonet.messages.clienttoserver.GetTablesMessage;
import br.com.sbk.sbking.networking.kryonet.messages.servertoclient.DealMessage;
import br.com.sbk.sbking.networking.kryonet.messages.servertoclient.FinishDealMessage;
import br.com.sbk.sbking.networking.kryonet.messages.servertoclient.GameModeOrStrainChooserMessage;
import br.com.sbk.sbking.networking.kryonet.messages.servertoclient.GetTablesResponseMessage;
import br.com.sbk.sbking.networking.kryonet.messages.servertoclient.InitializeDealMessage;
import br.com.sbk.sbking.networking.kryonet.messages.servertoclient.InvalidRulesetMessage;
import br.com.sbk.sbking.networking.kryonet.messages.servertoclient.IsNotSpectatorMessage;
import br.com.sbk.sbking.networking.kryonet.messages.servertoclient.IsSpectatorMessage;
import br.com.sbk.sbking.networking.kryonet.messages.servertoclient.PositiveOrNegativeChooserMessage;
import br.com.sbk.sbking.networking.kryonet.messages.servertoclient.PositiveOrNegativeMessage;
import br.com.sbk.sbking.networking.kryonet.messages.servertoclient.TextMessage;
import br.com.sbk.sbking.networking.kryonet.messages.servertoclient.ValidRulesetMessage;
import br.com.sbk.sbking.networking.kryonet.messages.servertoclient.YourDirectionIsMessage;
import br.com.sbk.sbking.networking.kryonet.messages.servertoclient.YourIdIsMessage;
import br.com.sbk.sbking.networking.kryonet.messages.servertoclient.YourTableIsMessage;

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
    // DTOs
    kryo.register(LobbyScreenTableDTO.class);

    // Server to Client Message classes
    kryo.register(DealMessage.class);
    kryo.register(FinishDealMessage.class);
    kryo.register(GameModeOrStrainChooserMessage.class);
    kryo.register(GetTablesResponseMessage.class);
    kryo.register(InitializeDealMessage.class);
    kryo.register(InvalidRulesetMessage.class);
    kryo.register(IsNotSpectatorMessage.class);
    kryo.register(IsSpectatorMessage.class);
    kryo.register(PositiveOrNegativeChooserMessage.class);
    kryo.register(PositiveOrNegativeMessage.class);
    kryo.register(TextMessage.class);
    kryo.register(ValidRulesetMessage.class);
    kryo.register(YourDirectionIsMessage.class);
    kryo.register(YourIdIsMessage.class);
    kryo.register(YourTableIsMessage.class);

    // Client to Server Message classes
    kryo.register(GetTablesMessage.class);
  }
}
