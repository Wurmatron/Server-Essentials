package com.wurmcraft.serveressentials.common.protect.events;

import com.wurmcraft.serveressentials.api.protection.Town;
import com.wurmcraft.serveressentials.common.protect.ProtectionModule;
import java.util.HashMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class PlayerTrackerEvents {

  private static HashMap<EntityPlayer, BlockPos> playerChunkLoc = new HashMap<>();

  private boolean hasMoved(EntityPlayer player) {
    if (playerChunkLoc.containsKey(player)) {
      return !(playerChunkLoc.get(player).getX() == ((int) player.posX)
          && playerChunkLoc.get(player).getZ() == ((int) player.posZ)) ;
    }
    return false;
  }

  @SubscribeEvent
  public void playerTick(TickEvent.PlayerTickEvent e) {
    if (hasMoved(e.player)) {
      Town oldTown = ProtectionModule.getTownForPos(playerChunkLoc.get(e.player));
      Town town = ProtectionModule.getTownForPos(e.player.getPosition());
      if (town != null && oldTown!= null && !oldTown.getID().equalsIgnoreCase(town.getID()) || town != null && oldTown == null) {
        e.player.sendStatusMessage(new TextComponentString(TextFormatting.RED + town.getID()), true);
      }
      playerChunkLoc.put(e.player, e.player.getPosition());
    } else if (!playerChunkLoc.containsKey(e.player)) {
      playerChunkLoc.put(e.player, e.player.getPosition());
    }
  }
}
