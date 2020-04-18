package com.wurmcraft.serveressentials.core.api.player;

import com.wurmcraft.serveressentials.core.api.json.JsonParser;

public class Vault implements JsonParser {

  public String items;

  public Vault(String items) {
    this.items = items;
  }
}
