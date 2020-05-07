package com.wurmcraft.serveressentials.forge.api.player;

import net.minecraft.entity.player.EntityPlayer;

public class TPAHereRequest extends TPARequest {

  public TPAHereRequest(EntityPlayer sendingPlayer,
      EntityPlayer requestedPlayer) {
    super(sendingPlayer, requestedPlayer);
  }
}
