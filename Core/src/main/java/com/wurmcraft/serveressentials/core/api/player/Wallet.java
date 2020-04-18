package com.wurmcraft.serveressentials.core.api.player;

import com.wurmcraft.serveressentials.core.api.json.JsonParser;
import java.util.Currency;

public class Wallet implements JsonParser {

  public Currency[] currency;

  public Wallet(Currency[] currency) {
    this.currency = currency;
  }
}
