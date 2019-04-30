package com.wurmcraft.serveressentials.api.user.eco;

import com.wurmcraft.serveressentials.common.ConfigHandler;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Bank {

  private Coin[] coin;

  public Bank() {
    coin = new Coin[0];
  }

  public Bank(Coin[] currency) {
    this.coin = currency;
  }

  public double getCurrency(String name) {
    if (coin != null) {
      for (Coin coin : coin) {
        if (coin.getName().equalsIgnoreCase(name)) {
          return coin.getAmount();
        }
      }
    } else {
      coin = new Coin[0];
    }
    return 0;
  }

  public Coin[] getCoin() {
    return coin;
  }

  public void setCoin(Coin[] coin) {
    this.coin = coin;
  }

  public void spend(String name, double amount) {
    for (Coin coin : coin) {
      if (coin.getName().equalsIgnoreCase(name.replaceAll(" ", "_"))) {
        coin.setAmount(coin.getAmount() - amount);
      }
    }
  }

  public void earn(String name, double amount) {
    boolean found = false;
    for (Coin coin : coin) {
      if (coin.getName().equalsIgnoreCase(name.replaceAll(" ", "_"))) {
        //        cleanCurrency();
        coin.setAmount(coin.getAmount() + amount);
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
    Collections.addAll(coins, this.coin);
    coins.add(coin);
    this.coin = coins.toArray(new Coin[0]);
  }

  private void cleanCurrency() {
    List<Coin> activeCurr = new ArrayList<>();
    for (String name : ConfigHandler.activeCurrency) {
      for (Coin coin : coin) {
        if (coin.getName().equalsIgnoreCase(name) && !coinExists(coin)) {
          activeCurr.add(coin);
        }
      }
    }
    coin = activeCurr.toArray(new Coin[0]);
  }

  private boolean coinExists(Coin coin) {
    for (Coin c : this.coin) {
      if (coin.getName().equalsIgnoreCase(c.getName())) {
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
