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

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof ConnectionWithIdentifier)) {
      return false;
    }
    ConnectionWithIdentifier otherConnection = (ConnectionWithIdentifier) obj;
    return this.getIdentifier() == otherConnection.getIdentifier();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this.getIdentifier() == null) ? 0 : this.getIdentifier().hashCode());
    return result;
  }

}
