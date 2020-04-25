package com.wurmcraft.serveressentials.forge.api.event;

import com.wurmcraft.serveressentials.core.api.json.rank.Rank;
import com.wurmcraft.serveressentials.core.api.player.StoredPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Event;

public class RankChangeEvent extends Event {

  public EntityPlayer player;
  public StoredPlayer data;
  public Rank oldRank;
  public Rank newRank;

  public RankChangeEvent(EntityPlayer player,
      StoredPlayer data, Rank oldRank,
      Rank newRank) {
    this.player = player;
    this.data = data;
    this.oldRank = oldRank;
    this.newRank = newRank;
  }
}
