package com.wurmcraft.serveressentials.api;

import com.wurmcraft.serveressentials.api.json.global.Keys;
import com.wurmcraft.serveressentials.api.json.user.DataType;
import com.wurmcraft.serveressentials.api.json.user.Rank;
import com.wurmcraft.serveressentials.api.module.IModule;
import com.wurmcraft.serveressentials.common.utils.DataHelper;
import com.wurmcraft.serveressentials.common.utils.UserManager;
import java.util.List;
import java.util.UUID;

public class ServerEssentialsAPI {

  public static List<IModule> modules;

  /**
   * Gets the ServerEssentials Local Data for a user
   *
   * @param uuid Player's UUID
   * @return PlayerData or {GlobalUser, LocalUser} depending on the loaded module
   */
  public Object[] getUserData(UUID uuid) {
    return UserManager.getPlayerData(uuid);
  }

  public Rank[] getRanks() {
    return UserManager.getRanks();
  }

  public DataType[] getData(Keys type) {
    return DataHelper.getData(type).toArray(new DataType[0]);
  }
}
