package com.wurmcraft.serveressentials.api.json.user;

import com.wurmcraft.serveressentials.api.json.user.restOnly.GlobalUser;

public interface IPerk {

  String name();

  String getPerk();

  double getCost(GlobalUser user);
}
