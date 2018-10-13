package com.wurmcraft.serveressentials.api.json.user;

import com.wurmcraft.serveressentials.api.json.user.rest.GlobalUser;

public interface IPerk {

  String name();

  String getPerk();

  double getCost(GlobalUser user);
}
