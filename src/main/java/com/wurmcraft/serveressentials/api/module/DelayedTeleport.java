package com.wurmcraft.serveressentials.api.module;

import net.minecraft.entity.player.EntityPlayer;

public class DelayedTeleport {

  public long startTime;
  public int time;
  public EntityPlayer A;
  public EntityPlayer B;

  public DelayedTeleport(long startTime, int time, EntityPlayer a, EntityPlayer b) {
    this.startTime = startTime;
    this.time = time;
    A = a;
    B = b;
  }
}
