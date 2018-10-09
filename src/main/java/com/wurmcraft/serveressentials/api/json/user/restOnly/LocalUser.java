package com.wurmcraft.serveressentials.api.json.user.restOnly;

import com.wurmcraft.serveressentials.api.json.user.DataType;
import com.wurmcraft.serveressentials.api.json.user.Home;
import com.wurmcraft.serveressentials.api.json.user.LocationWrapper;
import com.wurmcraft.serveressentials.api.json.user.optional.Vault;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class LocalUser implements DataType {

  private UUID uuid;
  private long lastSeen;
  private long firstJoin;
  private Home[] homes;
  private Vault[] vaults;
  private boolean tpLock;
  private String currentChannel;
  private long onlineTime;
  private LocationWrapper lastLocation;
  private long teleportTimer;
  private boolean frozen;
  private String[] ignored;

  public LocalUser(UUID uuid) {
    this.uuid = uuid;
    firstJoin = System.currentTimeMillis();
    lastSeen = System.currentTimeMillis();
    homes = new Home[0];
    vaults = new Vault[0];
    tpLock = false;
    currentChannel = ConfigHandler.defaultChannel;
    onlineTime = 0;
    lastLocation = new LocationWrapper(0, 0, 0, 0);
    ignored = new String[0];
  }

  public long getLastSeen() {
    return lastSeen;
  }

  public void setLastSeen(long lastSeen) {
    this.lastSeen = lastSeen;
  }

  public long getFirstJoin() {
    return firstJoin;
  }

  public void setFirstJoin(long firstJoin) {
    this.firstJoin = firstJoin;
  }

  public Home[] getHomes() {
    return homes;
  }

  public void setHomes(Home[] homes) {
    this.homes = homes;
  }

  public Vault[] getVaults() {
    return vaults;
  }

  public void setVaults(Vault[] vaults) {
    this.vaults = vaults;
  }

  public boolean isTpLock() {
    return tpLock;
  }

  public void setTpLock(boolean tpLock) {
    this.tpLock = tpLock;
  }

  public String getCurrentChannel() {
    return currentChannel;
  }

  public void setCurrentChannel(String currentChannel) {
    this.currentChannel = currentChannel;
  }

  public long getOnlineTime() {
    return onlineTime;
  }

  public void setOnlineTime(long onlineTime) {
    this.onlineTime = onlineTime;
  }

  public LocationWrapper getLastLocation() {
    return lastLocation;
  }

  public void setLastLocation(LocationWrapper lastLocation) {
    this.lastLocation = lastLocation;
  }

  public UUID getUuid() {
    return uuid;
  }

  public void setUuid(UUID uuid) {
    this.uuid = uuid;
  }

  @Override
  public String getID() {
    return uuid.toString();
  }

  public long getTeleportTimer() {
    return teleportTimer;
  }

  public void setTeleportTimer(long teleportTimer) {
    this.teleportTimer = teleportTimer;
  }

  public void addHome(Home home) {
    List<Home> homeData = new ArrayList<>();
    Collections.addAll(homeData, getHomes());
    homeData.add(home);
    this.homes = homeData.toArray(new Home[0]);
  }

  public void delHome(String name) {
    List<Home> homesArray = new ArrayList<>();
    for (Home home : getHomes()) {
      if (!home.getName().equalsIgnoreCase(name)) {
        homesArray.add(home);
      }
    }
    this.homes = homesArray.toArray(new Home[0]);
  }

  public boolean isFrozen() {
    return frozen;
  }

  public void setFrozen(boolean frozen) {
    this.frozen = frozen;
  }

  public String[] getIgnored() {
    return ignored;
  }

  public void setIgnored(String[] ignored) {
    this.ignored = ignored;
  }

  public void addIgnored(String ign) {
    List<String> list = new ArrayList<>();
    Collections.addAll(list, getIgnored());
    list.add(ign);
    this.ignored = list.toArray(new String[0]);
  }

  public void delIgnored(String ign) {
    List<String> list = new ArrayList<>();
    for (String i : getIgnored()) {
      if (!i.equalsIgnoreCase(ign)) {
        list.add(i);
      }
    }
    this.ignored = list.toArray(new String[0]);
  }

  public boolean isIgnored(String type) {
    if (getIgnored() == null) ignored = new String[0];
    for (String list : getIgnored()) {
      if (list.equalsIgnoreCase(type)) {
        return true;
      }
    }
    return false;
  }
}
