package com.wurmcraft.serveressentials.core.api.eco;

import com.wurmcraft.serveressentials.core.api.data.StoredDataType;

public class Currency implements StoredDataType {

  public String name;
  public double worth;

  public Currency() {
    this.name = "Default";
    this.worth = 1.0;
  }

  public Currency(String name, double worth) {
    this.name = name;
    this.worth = worth;
  }

  @Override
  public String getID() {
    return name;
  }
}
