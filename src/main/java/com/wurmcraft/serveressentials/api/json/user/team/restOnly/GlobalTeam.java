package com.wurmcraft.serveressentials.api.json.user.team.restOnly;

import com.wurmcraft.serveressentials.api.json.user.optional.Bank;
import java.util.UUID;

public class GlobalTeam {

  private String name;
  private Bank bank;
  private String[] perks;
  private UUID owner;
  private String[] members;

  public GlobalTeam() {
    this.name = "";
    this.bank = new Bank();
    this.perks = new String[0];
    this.owner = null;
    this.members = new String[0];
  }

  public GlobalTeam(GlobalTeam team) {
    this.name = team.name;
    this.bank = team.bank;
    this.perks = team.perks;
    this.owner = team.owner;
    this.members = team.members;
  }

  public GlobalTeam(String name, Bank bank, String[] perks, UUID owner, String[] members) {
    this.name = name;
    this.bank = bank;
    this.perks = perks;
    this.owner = owner;
    this.members = members;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Bank getBank() {
    return bank;
  }

  public void setBank(Bank bank) {
    this.bank = bank;
  }

  public String[] getPerks() {
    return perks;
  }

  public void setPerks(String[] perks) {
    this.perks = perks;
  }

  public UUID getOwner() {
    return owner;
  }

  public void setOwner(UUID owner) {
    this.owner = owner;
  }

  public String[] getMembers() {
    return members;
  }

  public void setMembers(String[] members) {
    this.members = members;
  }
}
