package com.wurmcraft.serveressentials.forge.common.utils;

import com.wurmcraft.serveressentials.core.SECore;
import com.wurmcraft.serveressentials.core.api.data.DataKey;
import com.wurmcraft.serveressentials.core.api.data.LocationWrapper;
import com.wurmcraft.serveressentials.core.api.player.StoredPlayer;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import java.util.NoSuchElementException;
import net.minecraft.entity.player.EntityPlayer;

public class TeleportUtils {

  public static void teleportTo(EntityPlayer player, LocationWrapper loc) {
    try {
      StoredPlayer playerData = (StoredPlayer) SERegistry
          .getStoredData(DataKey.PLAYER, player.getGameProfile().getId().toString());
      playerData.server.teleportTimer = System.currentTimeMillis();
      playerData.server.lastLocation = new LocationWrapper(player.posX, player.posY,
          player.posZ, player.dimension);
      if (player.dimension != loc.dim) {
        player.changeDimension(loc.dim);
      }
      player.setPositionAndUpdate(loc.x + .5, loc.y + 1, loc.z + .5);
    } catch (NoSuchElementException e) {
      SECore.logger.warning("Unable to find data for '" + player.getDisplayNameString()
          + "', unable to teleport player!");
    }
  }
}
