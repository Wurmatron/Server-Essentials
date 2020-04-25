package com.wurmcraft.serveressentials.core.api.data;

import com.wurmcraft.serveressentials.core.api.json.JsonParser;

public class LocationWrapper implements JsonParser {

  public double x;
  public double y;
  public double z;
  public int dim;

  public LocationWrapper(double x, double y, double z, int dim) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.dim = dim;
  }

  public LocationWrapper(double x, double y, double z) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.dim = 0;
  }

  @Override
  public String toString() {
    return "X:" + Math.round(x) + " Y:" + Math.round(y) + " Z:" + Math.round(z) + " Dim:" + dim;
  }
}
