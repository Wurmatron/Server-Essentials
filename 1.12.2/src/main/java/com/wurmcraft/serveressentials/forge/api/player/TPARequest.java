package com.wurmcraft.serveressentials.forge.api.player;

import com.wurmcraft.serveressentials.core.api.data.StoredDataType;
import net.minecraft.entity.player.EntityPlayer;

public class TPARequest implements StoredDataType {

  public EntityPlayer sendingPlayer;
  public EntityPlayer requestedPlayer;

  public TPARequest(EntityPlayer sendingPlayer,
      EntityPlayer requestedPlayer) {
    this.sendingPlayer = sendingPlayer;
    this.requestedPlayer = requestedPlayer;
  }

  @Override
  public String getID() {
    return sendingPlayer.getGameProfile().getId().toString();
  }
}
