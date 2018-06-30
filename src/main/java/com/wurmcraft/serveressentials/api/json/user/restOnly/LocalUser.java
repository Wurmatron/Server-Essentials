package com.wurmcraft.serveressentials.api.json.user.restOnly;

import com.wurmcraft.serveressentials.api.json.user.Home;
import com.wurmcraft.serveressentials.api.json.user.LocationWrapper;

public class LocalUser {

  private String lastSeen;
  private String firstJoin;
  public Home[] homes;
  private String[] vaults;
  private boolean tpLock;
  private String currentChannel;
  private String onlineTime;
  private LocationWrapper lastLocation;

  public LocalUser(String firstJoin, String currentChannel) {
    this.firstJoin = firstJoin;
    this.currentChannel = currentChannel;
  }

  public String getLastSeen() {
    return lastSeen;
  }

  public void setLastSeen(String lastSeen) {
    this.lastSeen = lastSeen;
  }

  public String getFirstJoin() {
    return firstJoin;
  }

  public void setFirstJoin(String firstJoin) {
    this.firstJoin = firstJoin;
  }

  public Home[] getHomes() {
    return homes;
  }

  public void setHomes(Home[] homes) {
    this.homes = homes;
  }

  public String[] getVaults() {
    return vaults;
  }

  public void setVaults(String[] vaults) {
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

  public String getOnlineTime() {
    return onlineTime;
  }

  public void setOnlineTime(String onlineTime) {
    this.onlineTime = onlineTime;
  }

  public LocationWrapper getLastLocation() {
    return lastLocation;
  }

  public void setLastLocation(LocationWrapper lastLocation) {
    this.lastLocation = lastLocation;
  }
}
