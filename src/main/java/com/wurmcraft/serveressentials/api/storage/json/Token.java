package com.wurmcraft.serveressentials.api.storage.json;

public class Token {

  public String id;
  public String token;

  public Token(String discordID, String token) {
    this.id = discordID;
    this.token = token;
  }
}
