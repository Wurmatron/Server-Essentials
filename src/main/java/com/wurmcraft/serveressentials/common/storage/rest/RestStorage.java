package com.wurmcraft.serveressentials.common.storage.rest;

import com.wurmcraft.serveressentials.api.ServerEssentialsAPI;
import com.wurmcraft.serveressentials.api.storage.Storage;
import com.wurmcraft.serveressentials.common.ServerEssentialsServer;
import com.wurmcraft.serveressentials.common.storage.rest.RequestGenerator.Status;
import net.minecraftforge.common.MinecraftForge;

public class RestStorage implements Storage {

  public static String restVersion = Status.getValidation().version;

  @Override
  public void setup() {
    ServerEssentialsAPI.rankManager = new RestRankManager();
    ServerEssentialsServer.LOGGER.info("Rest API Version is: " + restVersion);
    MinecraftForge.EVENT_BUS.register(new RestWorldEvents());
  }
}
