package com.wurmcraft.serveressentials.forge.api.event;

import com.wurmcraft.serveressentials.core.api.player.StoredPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Event;

public class PlayerDataSyncEvent extends Event {

  public final EntityPlayer player;
  public final StoredPlayer data;


  public PlayerDataSyncEvent(EntityPlayer player,
      StoredPlayer data) {
    this.player = player;
    this.data = data;
  }
}
