package com.wurmcraft.serveressentials.common.general.commands.utils;

import com.wurmcraft.serveressentials.api.json.user.IPerk;
import com.wurmcraft.serveressentials.api.json.user.rest.GlobalUser;

public class Perk implements IPerk {

  public String name;
  public double initialCost;
  public double scale;
  public String perk;

  public Perk(String name, double initialCost, double scale, String perk) {
    this.name = name;
    this.initialCost = initialCost;
    this.scale = scale;
    this.perk = perk;
  }

  @Override
  public String name() {
    return name;
  }

  @Override
  public String getPerk() {
    return perk;
  }

  @Override
  public double getCost(GlobalUser user) {
    if (scale > 0) {
      for (String perk : user.getPerks()) {
        int currentLvl = Integer.parseInt(perk.substring(perk.lastIndexOf('.') + 1));
        return (currentLvl + 1) * scale;
      }
    }
    return initialCost;
  }
}
