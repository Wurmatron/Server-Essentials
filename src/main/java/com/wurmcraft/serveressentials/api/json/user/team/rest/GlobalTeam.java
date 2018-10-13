package com.wurmcraft.serveressentials.api.json.user.team.rest;

import com.wurmcraft.serveressentials.api.json.user.optional.Bank;
import com.wurmcraft.serveressentials.api.json.user.team.ITeam;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class GlobalTeam implements ITeam {

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

  @Override
  public UUID getLeader() {
    return owner;
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

  @Override
  public String[] getMembers() {
    return members;
  }

  public void setMembers(String[] members) {
    this.members = members;
  }

  public void addMember(String uuid) {
    List<String> currentMembers = new ArrayList<>();
    Collections.addAll(currentMembers, members);
    if (!currentMembers.contains(uuid)) {
      currentMembers.add(uuid);
    }
    this.members = currentMembers.toArray(new String[0]);
  }

  public void delMember(String uuid) {
    List<String> currentMembers = new ArrayList<>();
    Collections.addAll(currentMembers, members);
    currentMembers.remove(uuid);
    this.members = currentMembers.toArray(new String[0]);
  }
}
