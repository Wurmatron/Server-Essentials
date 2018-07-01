package com.wurmcraft.serveressentials.common.rest.events;

import com.wurmcraft.serveressentials.api.json.user.restOnly.GlobalUser;
import com.wurmcraft.serveressentials.common.rest.RestModule;
import com.wurmcraft.serveressentials.common.rest.utils.RequestHelper;
import com.wurmcraft.serveressentials.common.team.TeamModule;
import com.wurmcraft.serveressentials.common.utils.UserManager;
import java.util.UUID;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class WorldEvent {

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public void onJoinEvent(PlayerEvent.PlayerLoggedInEvent e) {
    RestModule.syncPlayer(e.player.getGameProfile().getId());
    if (!UserManager.joinTime.containsKey(e.player.getGameProfile().getId())) {
      UserManager.joinTime.put(e.player.getGameProfile().getId(), System.currentTimeMillis());
    }
  }

  @SubscribeEvent
  public void onLeaveEvent(PlayerEvent.PlayerLoggedOutEvent e) {
    GlobalUser globalUser = (GlobalUser) UserManager
        .getPlayerData(e.player.getGameProfile().getId())[0];
    globalUser.setLastSeen(System.currentTimeMillis());
    globalUser.setOnlineTime(
        globalUser.getOnlineTime() + calculateOnTime(e.player.getGameProfile().getId()));
    RequestHelper.UserResponses.overridePlayerData(globalUser);
    UserManager.joinTime.remove(e.player.getGameProfile().getId());
    TeamModule.unloadRestTeam(globalUser);
    RestModule.deletePlayerData(e.player.getGameProfile().getId());
  }

  private long calculateOnTime(UUID uuid) {
    return (System.currentTimeMillis() - UserManager.joinTime.get(uuid)) / 60000;
  }
}
