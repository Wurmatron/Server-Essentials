package com.wurmcraft.serveressentials.common.rest.utils;

import com.wurmcraft.serveressentials.api.json.user.optional.Currency;

public class CurrencyJson extends Currency {

  public String authKey;

  public CurrencyJson(Currency currency, String authKey) {
    super(currency.name, currency.sell,currency.buy);
    this.authKey = authKey;
  }
}
