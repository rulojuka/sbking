package br.com.sbk.sbking.networking.kryonet;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.networking.kryonet.messages.servertoclient.GameModeOrStrainChooserMessage;
import br.com.sbk.sbking.networking.kryonet.messages.servertoclient.YourIdIsMessage;

public final class KryonetUtils {

  private KryonetUtils() {
    throw new IllegalStateException("Utility class");
  }

  // This registers objects that are going to be sent over the network.
  public static void register(EndPoint endPoint) {
    Kryo kryo = endPoint.getKryo();

    // Core classes
    kryo.register(Direction.class);

    // Server to Client Message classes
    kryo.register(GameModeOrStrainChooserMessage.class);
    kryo.register(YourIdIsMessage.class);
  }
}
