package com.wurmcraft.serveressentials.common.utils;

import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class UsernameResolver {

  // TODO Implement
  public static UUID getUUIDFromName(String name) {
    return UUID.fromString("148cf139-dd14-4bf4-97a2-08305dfef0a9");
  }

  public static EntityPlayer getPlayer(String name) {
    UUID uuid = getUUIDFromName(name);
    for (EntityPlayer player : FMLCommonHandler.instance().getMinecraftServerInstance()
        .getPlayerList().getPlayers()) {
      if (uuid.equals(player.getGameProfile().getId())) {
        return player;
      }
    }
    return null;
  }
}
