package com.wurmcraft.serveressentials.api.user.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Event;

public class NewPlayerJoin extends Event {

  public EntityPlayer player;
  public boolean networkPlayer;

  public NewPlayerJoin(EntityPlayer player) {
    this.player = player;
  }

  public NewPlayerJoin(EntityPlayer player, boolean networkPlayer) {
    this.player = player;
    this.networkPlayer = networkPlayer;
  }
}
