package com.wurmcraft.serveressentials.forge.modules.core.event;

import com.wurmcraft.serveressentials.core.api.player.StoredPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Event;

public class NewPlayerJoin extends Event {

  public EntityPlayer player;
  public StoredPlayer playerData;

  public NewPlayerJoin(EntityPlayer player,
      StoredPlayer playerData) {
    this.player = player;
    this.playerData = playerData;
  }
}
