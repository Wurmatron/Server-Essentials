package com.wurmcraft.serveressentials.forge.api.event;

import com.wurmcraft.serveressentials.core.api.player.GlobalPlayer;
import com.wurmcraft.serveressentials.core.api.player.StoredPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Event;

public class RestPlayerSyncEvent extends Event {

  public EntityPlayer player;
  public StoredPlayer serverData;
  public GlobalPlayer restData;

  public RestPlayerSyncEvent(EntityPlayer player,
      StoredPlayer serverData,
      GlobalPlayer restData) {
    this.player = player;
    this.serverData = serverData;
    this.restData = restData;
  }
}
