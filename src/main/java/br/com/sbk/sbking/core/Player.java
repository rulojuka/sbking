package br.com.sbk.sbking.core;

import java.util.UUID;

public class Player {

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
