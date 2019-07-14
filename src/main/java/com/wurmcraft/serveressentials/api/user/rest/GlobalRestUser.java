package com.wurmcraft.serveressentials.api.user.rest;

import com.wurmcraft.serveressentials.api.ServerEssentialsAPI;
import com.wurmcraft.serveressentials.api.user.eco.Bank;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.utils.command.CommandUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

  public GlobalRestUser() {}

  public GlobalRestUser(GlobalRestUser user) {
    this.uuid = user.uuid;
    this.rank = user.rank;
    this.nick = user.nick;
    this.bank = user.bank;
    this.team = user.team;
    this.muted = user.muted;
    this.language = user.language;
    this.discord = user.discord;
    this.permission = user.permission;
    this.perks = user.perks;
  }

  public GlobalRestUser(String uuid, String rank) {
    this.uuid = uuid;
    this.rank = rank;
    this.nick = "";
    this.bank = new Bank();
    this.team = "";
    this.muted = false;
    this.language = ConfigHandler.defaultLanguage;
    this.discord = "";
    this.permission = new String[0];
    this.perks = new String[0];
    serverData = new ServerTime[0];
  }

  public GlobalRestUser(
      String uuid,
      String rank,
      String nick,
      Bank bank,
      String team,
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
    this.muted = muted;
    this.language = lang;
    this.discord = discord;
    this.permission = permission;
    this.perks = perks;
    serverData = new ServerTime[0];
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

  public boolean isMuted() {
    return muted;
  }

  public void setMuted(boolean muted) {
    this.muted = muted;
  }

  public String getLang() {
    return language;
  }

  public void setLang(String lang) {
    this.language = lang;
  }

  public String getDiscord() {
    return discord;
  }

  public void setDiscord(String discord) {
    this.discord = discord;
  }

  public String[] getPermission() {
    if (permission == null) {
      return new String[0];
    }
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
    for (String p : perk) {
      if (p.contains(".")) {
        delPerk(p.substring(0, p.indexOf(".")));
      }
    }
    List<String> currentPerks = new ArrayList<>();
    Collections.addAll(currentPerks, getPerks());
    Collections.addAll(currentPerks, perk);
    this.perks = currentPerks.toArray(new String[0]);
  }

  public void delPerk(String perk) {
    List<String> currentPerks = new ArrayList<>();
    for (String perm : perks) {
      if (perk.contains("%LEVEL%")) {
        if (perm.substring(perm.lastIndexOf('.'))
            .equalsIgnoreCase(perk.substring(perm.lastIndexOf('.')))) {
          currentPerks.add(perm);
        }
      } else if (!perm.equalsIgnoreCase(perk)) {
        currentPerks.add(perm);
      }
    }
    perks = currentPerks.toArray(new String[0]);
  }

  public boolean hasPerk(String perk) {
    return Arrays.stream(perks).anyMatch(p -> p.equalsIgnoreCase(perk));
  }

  public ServerTime[] getServerData() {
    if (serverData == null || serverData.length <= 0) {
      addServerData(
          new ServerTime(
              ConfigHandler.serverName,
              0,
              System.currentTimeMillis(),
              System.currentTimeMillis(),
              0));
    }
    return serverData;
  }

  public void setServerData(ServerTime[] serverData) {
    this.serverData = serverData;
  }

  public void addServerData(ServerTime data) {
    List<ServerTime> current = new ArrayList<>();
    if (serverData != null) {
      Collections.addAll(current, serverData);
    }
    current
        .stream()
        .filter(time -> time.getServerID().equalsIgnoreCase(data.getServerID()))
        .forEachOrdered(current::remove);
    current.add(data);
    serverData = current.toArray(new ServerTime[0]);
  }

  public void updateServerData(double onlineTime) {
    ServerTime time = getServerData(ConfigHandler.serverName);
    if (time != null) {
      time.setLastSeen(System.currentTimeMillis());
      time.setOnlineTime(onlineTime);
    }
  }

  public void updateServerData(int chunkAmount) {
    ServerTime serverData = getServerData(ConfigHandler.serverName);
    if (serverData != null) {
      int chunkCount = serverData.getLoadedChunks() + chunkAmount;
      serverData.setLoadedChunks(chunkCount > 0 ? chunkCount : 0);
    }
  }

  public ServerTime getServerData(String name) {
    for (ServerTime data : getServerData()) {
      if (data.getServerID().equalsIgnoreCase(name)) {
        return data;
      }
    }
    return null;
  }

  public boolean hasPerm(String perm) {
    if (hasPerm(perm, permission)
        || hasPerm(
            perm,
            CommandUtils.generatePermissionList(ServerEssentialsAPI.rankManager.getRank(rank))
                .toArray(new String[0]))) {
      return true;
    }
    return false;
  }

  public boolean hasPerm(String perm, String[] perms) {
    if (perms != null && perms.length > 0) {
      for (String p : perms) {
        if (p.equalsIgnoreCase("*")) { // Check for Global Node
          return true;
        } else if (p.contains(".*")
            && perm.contains(".")) { // Check for Everything in a module node
          return perm.substring(perm.indexOf('.'))
              .equalsIgnoreCase(p.substring(p.indexOf('.') - 1));
        } else if (p.equalsIgnoreCase(perm)) { // Check on a case-per basis
          return true;
        }
      }
    }
    return false;
  }
}
