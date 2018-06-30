package com.wurmcraft.serveressentials.api.json.user.restOnly;

import com.wurmcraft.serveressentials.api.json.user.optional.Bank;
import com.wurmcraft.serveressentials.api.json.user.optional.Share;

public class GlobalUser {

  private String uuid;
  public String rank;
  private String nick;
  private Bank bank;
  private String team;
  private String onlineTime;
  private String lastSeen;
  private Share[] stocks;
  private int loadedChunks;
  private long firstJoin;
  private boolean muted;
  private String lang;
  private String discord;
  private String permission;
  private String perks;

  public GlobalUser(String uuid, String rank) {
    this.uuid = uuid;
    this.rank = rank;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public String getRank() {
    return rank;
  }

  public void setRank(String rank) {
    this.rank = rank;
  }

  public String getNick() {
    return nick;
  }

  public void setNick(String nick) {
    this.nick = nick;
  }

  public Bank getBank() {
    return bank;
  }

  public void setBank(Bank bank) {
    this.bank = bank;
  }

  public String getTeam() {
    return team;
  }

  public void setTeam(String team) {
    this.team = team;
  }

  public String getOnlineTime() {
    return onlineTime;
  }

  public void setOnlineTime(String onlineTime) {
    this.onlineTime = onlineTime;
  }

  public String getLastSeen() {
    return lastSeen;
  }

  public void setLastSeen(String lastSeen) {
    this.lastSeen = lastSeen;
  }

  public Share[] getStocks() {
    return stocks;
  }

  public void setStocks(Share[] stocks) {
    this.stocks = stocks;
  }

  public int getLoadedChunks() {
    return loadedChunks;
  }

  public void setLoadedChunks(int loadedChunks) {
    this.loadedChunks = loadedChunks;
  }

  public long getFirstJoin() {
    return firstJoin;
  }

  public void setFirstJoin(long firstJoin) {
    this.firstJoin = firstJoin;
  }

  public boolean isMuted() {
    return muted;
  }

  public void setMuted(boolean muted) {
    this.muted = muted;
  }

  public String getLang() {
    return lang;
  }

  public void setLang(String lang) {
    this.lang = lang;
  }

  public String getDiscord() {
    return discord;
  }

  public void setDiscord(String discord) {
    this.discord = discord;
  }

  public String getPermission() {
    return permission;
  }

  public void setPermission(String permission) {
    this.permission = permission;
  }

  public String getPerks() {
    return perks;
  }

  public void setPerks(String perks) {
    this.perks = perks;
  }
}
