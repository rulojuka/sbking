package br.com.sbk.sbking.core;

import java.io.Serializable;
import java.util.UUID;

@SuppressWarnings("serial")
public class Player implements Serializable {

  private UUID identifier;
  private String nickname;

  /**
   * @deprecated Kryo needs a no-arg constructor
   */
  private Player() {
  }

  public Player(UUID identifier, String nickname) {
    this.identifier = identifier;
    this.nickname = nickname;
  }

  public UUID getIdentifier() {
    return identifier;
  }

  public String getNickname() {
    return nickname;
  }

  public void setNickname(String name) {
    this.nickname = name;
  }

}
