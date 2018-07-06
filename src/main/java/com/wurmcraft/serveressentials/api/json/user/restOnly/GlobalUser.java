package com.wurmcraft.serveressentials.api.json.user.restOnly;

import com.wurmcraft.serveressentials.api.json.user.optional.Bank;
import com.wurmcraft.serveressentials.api.json.user.optional.Share;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GlobalUser {

  private String uuid;
  public String rank;
  private String nick;
  private Bank bank;
  private String team;
  private long onlineTime;
  private long lastSeen;
  private Share[] stocks;
  private int loadedChunks;
  private long firstJoin;
  private boolean muted;
  private String lang;
  private String discord;
  private String[] permission;
  private String[] perks;

  public GlobalUser() {
  }

  public GlobalUser(GlobalUser user) {
    this.uuid = user.uuid;
    this.rank = user.rank;
    this.nick = user.nick;
    this.bank = user.bank;
    this.team = user.team;
    this.onlineTime = user.onlineTime;
    this.lastSeen = user.lastSeen;
    this.stocks = user.stocks;
    this.loadedChunks = user.loadedChunks;
    this.firstJoin = user.firstJoin;
    this.muted = user.muted;
    this.lang = user.lang;
    this.discord = user.discord;
    this.permission = user.permission;
    this.perks = user.perks;
  }

  public GlobalUser(String uuid, String rank) {
    this.uuid = uuid;
    this.rank = rank;
    this.nick = "";
    this.bank = new Bank();
    this.team = "";
    this.onlineTime = 0;
    this.lastSeen = System.currentTimeMillis();
    this.stocks = new Share[0];
    this.loadedChunks = 0;
    this.firstJoin = System.currentTimeMillis();
    this.muted = false;
    this.lang = ConfigHandler.defaultLanguage;
    this.discord = "";
    this.permission = new String[0];
    this.perks = new String[0];
  }

  public GlobalUser(
      String uuid,
      String rank,
      String nick,
      Bank bank,
      String team,
      long onlineTime,
      long lastSeen,
      Share[] stocks,
      int loadedChunks,
      long firstJoin,
      boolean muted,
      String lang,
      String discord,
      String[] permission,
      String[] perks) {
    this.uuid = uuid;
    this.rank = rank;
    this.nick = nick;
    this.bank = bank;
    this.team = team;
    this.onlineTime = onlineTime;
    this.lastSeen = lastSeen;
    this.stocks = stocks;
    this.loadedChunks = loadedChunks;
    this.firstJoin = firstJoin;
    this.muted = muted;
    this.lang = lang;
    this.discord = discord;
    this.permission = permission;
    this.perks = perks;
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

  public long getOnlineTime() {
    return onlineTime;
  }

  public void setOnlineTime(long onlineTime) {
    this.onlineTime = onlineTime;
  }

  public long getLastSeen() {
    return lastSeen;
  }

  public void setLastSeen(long lastSeen) {
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

  public String[] getPermission() {
    return permission;
  }

  public void setPermission(String[] permission) {
    this.permission = permission;
  }

  public String[] getPerks() {
    return perks;
  }

  public void setPerks(String[] perks) {
    this.perks = perks;
  }

  public void addPermission(String... perm) {
    List<String> currentPermissions = new ArrayList<>();
    Collections.addAll(currentPermissions, getPermission());
    currentPermissions.addAll(Arrays.asList(perm));
    this.permission = currentPermissions.toArray(new String[0]);
  }

  public void delPermission(String perm) {
    List<String> currentPermissions = new ArrayList<>();
    Collections.addAll(currentPermissions, getPermission());
    String delNode = "";
    for (String node : currentPermissions) {
      if (node.equalsIgnoreCase(perm)) {
        delNode = node;
      }
    }
    currentPermissions.remove(delNode);
    this.permission = currentPermissions.toArray(new String[0]);
  }

  public void addPerk(String... perk) {
    List<String> currentPerks = new ArrayList<>();
    Collections.addAll(currentPerks, getPerks());
    Collections.addAll(currentPerks, perk);
    this.perks = currentPerks.toArray(new String[0]);
  }

  public void delPerk(String perk) {
    List<String> currentPerks = new ArrayList<>();
    Collections.addAll(currentPerks, getPermission());
    String delNode = "";
    for (String node : currentPerks) {
      if (node.equalsIgnoreCase(perk)) {
        delNode = node;
      }
    }
    currentPerks.remove(delNode);
    this.permission = currentPerks.toArray(new String[0]);
  }
}
