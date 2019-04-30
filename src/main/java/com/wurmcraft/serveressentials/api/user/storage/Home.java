package com.wurmcraft.serveressentials.api.user.storage;

import com.wurmcraft.serveressentials.api.storage.LocationWrapper;

public class Home {

  private String name;
  private LocationWrapper pos;

  public Home(String name, LocationWrapper location) {
    this.name = setName(name);
    this.pos = setPos(location);
  }

  public String getName() {
    return name;
  }

  public String setName(String name) {
    this.name = name.replaceAll("[^a-zA-Z0-9-_.]", "_");
    return getName();
  }

  public LocationWrapper getPos() {
    return pos;
  }

  public LocationWrapper setPos(LocationWrapper pos) {
    this.pos = pos;
    return getPos();
  }
}
