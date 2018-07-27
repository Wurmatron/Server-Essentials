package com.wurmcraft.serveressentials.api.json.user.optional;

import com.wurmcraft.serveressentials.common.ConfigHandler;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Bank {

  public Coin[] currency;

  public Bank() {
    currency = new Coin[0];
  }

  public Bank(Coin[] currency) {
    this.currency = currency;
  }

  public double getCurrency(String name) {
    for (Coin coin : currency) {
      if (coin.name.equalsIgnoreCase(name)) {
        return coin.amount;
      }
    }
    return 0;
  }

  public Coin[] getCurrency() {
    return currency;
  }

  public void setCurrency(Coin[] currency) {
    this.currency = currency;
  }

  public void spend(String name, double amount) {
    for (Coin coin : currency) {
      if (coin.name.equalsIgnoreCase(name.replaceAll(" ", "_"))) {
        coin.setAmount(coin.amount - amount);
      }
    }
  }

  public void earn(String name, double amount) {
    boolean found = false;
    for (Coin coin : currency) {
      if (coin.name.equalsIgnoreCase(name.replaceAll(" ", "_"))) {
        cleanCurrency();
        coin.setAmount(coin.amount + amount);
        found = true;
      }
    }
    if (!found && validCurrency(name)) {
      addCurrency(new Coin(name, amount));
    }
  }

  private void addCurrency(Coin coin) {
    cleanCurrency();
    List<Coin> coins = new ArrayList<>();
    Collections.addAll(coins, currency);
    coins.add(coin);
    currency = coins.toArray(new Coin[0]);
  }

  private void cleanCurrency() {
    List<Coin> activeCurr = new ArrayList<>();
    List<String> tested = new ArrayList<>();
    for (String name : ConfigHandler.activeCurrency) {
      for (Coin coin : currency) {
        if (coin.name.equalsIgnoreCase(name) && !coinExists(coin)) {
          activeCurr.add(coin);
          tested.add(coin.name);
        }
      }
    }
    currency = activeCurr.toArray(new Coin[0]);
  }

  public boolean coinExists(Coin coin) {
    for (Coin c : currency) {
      if (coin.name.equalsIgnoreCase(c.name)) {
        return true;
      }
    }
    return false;
  }

  private boolean validCurrency(String name) {
    for (String c : ConfigHandler.activeCurrency) {
      if (c.equalsIgnoreCase(name)) {
        return true;
      }
    }
    return false;
  }
}
