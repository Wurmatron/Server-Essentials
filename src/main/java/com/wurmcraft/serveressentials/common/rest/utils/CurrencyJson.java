package com.wurmcraft.serveressentials.common.rest.utils;

import com.wurmcraft.serveressentials.api.json.user.optional.Currency;

public class CurrencyJson extends Currency {

  private final String authKey;

  public CurrencyJson(Currency currency, String authKey) {
    super(currency.getName(), currency.getSell(), currency.getBuy());
    this.authKey = authKey;
  }
}
