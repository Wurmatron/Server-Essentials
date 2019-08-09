package com.wurmcraft.serveressentials.api.storage.json;

import com.wurmcraft.serveressentials.api.storage.FileType;

public class Kit implements FileType {

  public String[] items;
  public int timer;
  public String name;

  public Kit() {}

  public Kit(String[] items, int timer, String name) {
    this.items = items;
    this.timer = timer;
    this.name = name;
  }

  @Override
  public String getID() {
    return name;
  }
}
