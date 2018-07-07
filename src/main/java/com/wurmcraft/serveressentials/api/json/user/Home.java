package com.wurmcraft.serveressentials.api.json.user;

/** Holds all the values for a home */
public class Home {

  private String name;
  private LocationWrapper pos;

  public Home() {}

  public Home(String name, LocationWrapper location) {
    this.name = setName(name);
    this.pos = setPos(location);
  }

  public String getName() {
    return name;
  }

  public String setName(String name) {
    this.name = name.replaceAll("[^a-zA-Z0-9-_\\.]", "_");
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
