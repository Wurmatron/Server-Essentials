package com.wurmcraft.serveressentials.core.api.player;

import com.wurmcraft.serveressentials.core.api.eco.Coin;
import com.wurmcraft.serveressentials.core.api.json.JsonParser;

public class Wallet implements JsonParser {

  public Coin[] currency;

  public Wallet(Coin[] currency) {
    this.currency = currency;
  }
}
