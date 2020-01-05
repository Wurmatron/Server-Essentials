package com.wurmcraft.bot.json;

import com.wurmcraft.bot.json.Player.Bank;
import java.io.Serializable;

public class GlobalRestUser implements Serializable {

  private String uuid;
  public String rank;
  private String nick;
  private Bank bank;
  private String team;
  private ServerTime[] serverData;
  private boolean muted;
  private String language;
  private String discord;
  private String[] permission;
  private String[] perks;

  public GlobalRestUser() {
  }

  public GlobalRestUser(String uuid, String rank, String nick,
      Bank bank, String team, ServerTime[] serverData, boolean muted, String language,
      String discord, String[] permission, String[] perks) {
    this.uuid = uuid;
    this.rank = rank;
    this.nick = nick;
    this.bank = bank;
    this.team = team;
    this.serverData = serverData;
    this.muted = muted;
    this.language = language;
    this.discord = discord;
    this.permission = permission;
    this.perks = perks;
  }

  public String getUuid() {
    return uuid;
  }

  public String getRank() {
    return rank;
  }

  public String getNick() {
    return nick;
  }

  public Bank getBank() {
    return bank;
  }

  public String getTeam() {
    return team;
  }

  public boolean isMuted() {
    return muted;
  }

  public String getLang() {
    return language;
  }

  public String getDiscord() {
    return discord;
  }

  public String[] getPermission() {
    if (permission == null) {
      return new String[0];
    }
    return permission;
  }

  public String[] getPerks() {
    return perks;
  }

  public ServerTime[] getServerData() {
    return serverData;
  }
}

