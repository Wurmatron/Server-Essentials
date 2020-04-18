package com.wurmcraft.serveressentials.core.api.player;

import com.wurmcraft.serveressentials.core.api.data.LocationWrapper;

public class Home extends LocationWrapper {

  public String name;

  public Home(String name, double x, double y, double z, int dim) {
    super(x, y, z, dim);
    this.name = name;
  }
}
