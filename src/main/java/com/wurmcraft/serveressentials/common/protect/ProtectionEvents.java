package com.wurmcraft.serveressentials.common.protect;

import com.wurmcraft.serveressentials.api.protection.Town;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ProtectionEvents {

  @SubscribeEvent
  public void blockBreak(BreakEvent e) {
    if (ProtectionModule.isAreaClaimed(e.getPos())) {
      Town town = ProtectionModule.getTownForPos(e.getPos());
      if (!town.getOwnerID().equalsIgnoreCase(e.getPlayer().getGameProfile().getId().toString()))
        e.setCanceled(true);
    }
  }
}
