package com.wurmcraft.serveressentials.api.json.user.team.restOnly;

import com.wurmcraft.serveressentials.api.json.user.Home;
import com.wurmcraft.serveressentials.api.json.user.IDataType;

public class LocalTeam implements IDataType {

  public String name;
  public Home[] homes;

  public LocalTeam() {
    this.name = "";
    this.homes = new Home[0];
  }

  public LocalTeam(String name) {
    this.name = name;
  }

  @Override
  public String getID() {
    return name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Home[] getHomes() {
    return homes;
  }

  public void setHomes(Home[] homes) {
    this.homes = homes;
  }
}
