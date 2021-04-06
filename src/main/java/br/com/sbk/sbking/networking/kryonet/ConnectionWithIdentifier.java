package br.com.sbk.sbking.networking.kryonet;

import java.util.UUID;

import com.esotericsoftware.kryonet.Connection;

public class ConnectionWithIdentifier extends Connection {

  private UUID uuid;

  public ConnectionWithIdentifier(UUID uuid) {
    super();
    this.uuid = uuid;
  }

  public UUID getIdentifier() {
    return this.uuid;
  }

}
