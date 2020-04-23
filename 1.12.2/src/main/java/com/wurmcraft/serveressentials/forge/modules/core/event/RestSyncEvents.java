package com.wurmcraft.serveressentials.forge.modules.core.event;

import com.wurmcraft.serveressentials.forge.api.event.RestPlayerSyncEvent;
import java.time.Instant;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RestSyncEvents {

  @SubscribeEvent
  public void syncRestEvent(RestPlayerSyncEvent e) {
    e.serverData.server.lastSeen = Instant.now().getEpochSecond();
    e.serverData.global.lastSeen = Instant.now().getEpochSecond();
    e.restData.lastSeen = Instant.now().getEpochSecond();
    e.restData.rank = e.serverData.global.rank;
    e.restData.muted = e.serverData.global.muted;
    e.restData.discordID = e.serverData.global.discordID;
  }
}
