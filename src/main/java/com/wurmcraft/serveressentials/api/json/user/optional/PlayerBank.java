package com.wurmcraft.serveressentials.api.json.user.optional;

import java.util.UUID;

public class PlayerBank {

  public UUID uuid;
  public Bank bank;

  public PlayerBank(UUID uuid, Bank bank) {
    this.uuid = uuid;
    this.bank = bank;
  }
}
