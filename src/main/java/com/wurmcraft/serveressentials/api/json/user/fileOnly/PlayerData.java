package com.wurmcraft.serveressentials.api.json.user.fileOnly;

import com.wurmcraft.serveressentials.api.json.user.Channel;
import com.wurmcraft.serveressentials.api.json.user.Home;
import com.wurmcraft.serveressentials.api.json.user.IDataType;
import com.wurmcraft.serveressentials.api.json.user.LocationWrapper;
import com.wurmcraft.serveressentials.api.json.user.Mail;
import com.wurmcraft.serveressentials.api.json.user.Rank;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.language.LanguageModule;
import com.wurmcraft.serveressentials.common.utils.UserManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/** Used to hold the data about a player */
public class PlayerData implements IDataType {

  private String rank;
  private String nickname = "";
  private int max_homes = 2;
  private long teleport_timer;
  private long lastseen;
  private int money;
  private LocationWrapper lastLocation;
  private Home[] homes = new Home[max_homes];
  private List<Mail> currentMail = new ArrayList<>();
  private String team;
  private String currentChannel;
  private boolean muted = false;
  private int vaultSlots;
  private int marketSlots;
  private boolean spy;
  private long firstJoin;
  private int onlineTime;
  private boolean isFrozen;
  private boolean tpLock;
  private String[] customData = new String[0];
  private UUID uuid;
  private String lang;

  public PlayerData() {}

  public PlayerData(UUID uuid, Rank rank) {
    if (rank != null) {
      this.rank = rank.getName();
    } else {
      this.rank = UserManager.getDefaultRank().getName();
    }
    this.uuid = uuid;
  }

  public Home[] getHomes() {
    return homes;
  }

  public void setHomes(Home[] homes) {
    if (homes != null && homes.length > 0) {
      this.homes = homes;
    }
  }

  public Home getHome(String name) {
    if (homes != null) {
      for (Home h : homes) {
        if (h != null && h.getName().equalsIgnoreCase(name)) {
          return h;
        }
      }
    }
    return null;
  }

  public String addHome(Home home) {
    if (home != null && homes != null && home.getName() != null) {
      for (int index = 0; index < homes.length; index++) {
        if (homes[index] != null && homes[index].getName().equalsIgnoreCase(home.getName())) {
          homes[index] = home;
          return LanguageModule.getLangFromKey(lang).HOME_REPLACED.replaceAll("#", home.getName());
        } else if (homes[index] == null) {
          homes[index] = home;
          return LanguageModule.getLangFromKey(lang).HOME_SET.replaceAll("#", home.getName());
        }
      }
      return LanguageModule.getLangFromKey(lang)
          .HOME_MAX
          .replaceAll("#", Integer.toString(max_homes));
    }
    return LanguageModule.getLangFromKey(lang).HOME_INVALID;
  }

  public String delHome(String name) {
    if (name != null && homes != null) {
      for (int index = 0; index < homes.length; index++) {
        if (homes[index] != null && homes[index].getName().equalsIgnoreCase(name)) {
          homes[index] = null;
          return LanguageModule.getLangFromKey(lang).HOME_DELETED.replaceAll("#", name);
        }
      }
    }
    return LanguageModule.getLangFromKey(lang).HOME_ERROR_DELETION.replaceAll("#", name);
  }

  public long getTeleportTimer() {
    return teleport_timer;
  }

  public void setTeleportTimer(long time) {
    teleport_timer = time;
  }

  public List<Mail> getMail() {
    return currentMail;
  }

  public void addMail(Mail mail) {
    currentMail.add(mail);
  }

  public void removeMail(int index) {
    if (index < currentMail.size()) {
      currentMail.remove(index);
    }
  }

  public long getLastseen() {
    return lastseen;
  }

  public void setLastseen(long lastseen) {
    this.lastseen = lastseen;
  }

  public Rank getRank() {
    Rank group = UserManager.getRank(rank);
    if (group != null) {
      return group;
    }
    setRank(UserManager.getDefaultRank());
    return UserManager.getDefaultRank();
  }

  public void setRank(String rank) {
    Rank group = UserManager.getRank(rank);
    if (group != null) {
      setRank(group);
    } else {
      setRank(UserManager.getDefaultRank());
    }
  }

  public void setRank(Rank rank) {
    this.rank = UserManager.getRank(rank.getName()).getName();
  }

  public LocationWrapper getLastLocation() {
    return lastLocation;
  }

  public void setLastLocation(LocationWrapper loc) {
    this.lastLocation = loc;
  }

  public int getMoney() {
    return money;
  }

  public void setMoney(int money) {
    this.money = money;
  }

  public String getCurrentChannel() {
    return currentChannel;
  }

  public void setCurrentChannel(Channel channel) {
    if (channel != null) {
      this.currentChannel = channel.getName();
    } else {
      this.currentChannel = ConfigHandler.defaultChannel;
    }
  }

  public boolean isMuted() {
    return muted;
  }

  public void setMuted(boolean muted) {
    this.muted = muted;
  }

  public int getVaultSlots() {
    return vaultSlots;
  }

  public void setVaultSlots(int vaultSlots) {
    this.vaultSlots = vaultSlots;
  }

  public int getMarketSlots() {
    return marketSlots;
  }

  public void setMarketSlots(int marketSlots) {
    this.marketSlots = marketSlots;
  }

  public boolean isSpy() {
    return spy;
  }

  public void setSpy(boolean spy) {
    this.spy = spy;
  }

  public String getNickname() {
    return nickname;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

  public void setFirstJoin() {
    this.firstJoin = System.currentTimeMillis();
  }

  public long getFirstJoin() {
    return this.firstJoin;
  }

  public int getOnlineTime() {
    return onlineTime;
  }

  public void setOnlineTime(int onlineTime) {
    this.onlineTime = onlineTime;
  }

  public boolean isFrozen() {
    return isFrozen;
  }

  public void setFrozen(boolean frozen) {
    isFrozen = frozen;
  }

  public boolean isTpLock() {
    return tpLock;
  }

  public void setTpLock(boolean tpLock) {
    this.tpLock = tpLock;
  }

  public int getMaxHomes() {
    return max_homes;
  }

  public void setMaxHomes(int max) {
    max_homes = max;
    if (max_homes < 0) {
      max_homes = 0;
    }
  }

  public String[] getCustomData() {
    return customData;
  }

  public void setCustomData(String[] data) {
    this.customData = data;
  }

  public void addCustomData(String data) {
    List<String> custData = new ArrayList<>();
    if (customData != null && customData.length > 0) {
      Collections.addAll(custData, customData);
    }
    custData.add(data);
    customData = custData.toArray(new String[0]);
  }

  @Override
  public String getID() {
    return uuid.toString();
  }

  public String getLang() {
    return lang;
  }

  public void setLang(String lang) {
    this.lang = lang;
  }

  public String getTeam() {
    return team;
  }

  public void setTeam(String team) {
    this.team = team;
  }
}
