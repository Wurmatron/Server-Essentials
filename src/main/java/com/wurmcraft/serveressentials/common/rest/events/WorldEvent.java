package com.wurmcraft.serveressentials.common.rest.events;

import com.wurmcraft.serveressentials.api.json.user.rest.GlobalUser;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.chat.ChatHelper;
import com.wurmcraft.serveressentials.common.language.LanguageModule;
import com.wurmcraft.serveressentials.common.rest.RestModule;
import com.wurmcraft.serveressentials.common.rest.utils.RequestHelper;
import com.wurmcraft.serveressentials.common.security.SecurityModule;
import com.wurmcraft.serveressentials.common.team.TeamModule;
import com.wurmcraft.serveressentials.common.utils.UserManager;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.ForgeChunkManager.ForceChunkEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class WorldEvent {

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public void onJoinEvent(PlayerEvent.PlayerLoggedInEvent e) {
    RestModule.syncPlayer(e.player.getGameProfile().getId());
    if (!UserManager.JOIN_TIME.containsKey(e.player.getGameProfile().getId())) {
      UserManager.JOIN_TIME.put(e.player.getGameProfile().getId(), System.currentTimeMillis());
    }
    if (!RestModule.isValid && SecurityModule.isTrustedMember(e.player)) {
      ChatHelper.sendMessage(
          e.player,
          LanguageModule.getLangfromUUID(e.player.getGameProfile().getId())
              .REST_INVALID
              .replaceAll("%URL%", ConfigHandler.restURL));
    }
  }

  @SubscribeEvent
  public void onLeaveEvent(PlayerEvent.PlayerLoggedOutEvent e) {
    if (UserManager.PLAYER_DATA.containsKey(e.player.getGameProfile().getId())) {
      GlobalUser globalUser =
          (GlobalUser) UserManager.getPlayerData(e.player.getGameProfile().getId())[0];
      updateOnlineTime(e.player);
      RequestHelper.UserResponses.overridePlayerData(globalUser);
      UserManager.JOIN_TIME.remove(e.player.getGameProfile().getId());
      TeamModule.unloadRestTeam(globalUser);
      RestModule.deletePlayerData(e.player.getGameProfile().getId());
    }
  }

  public static long calculateOnTime(UUID uuid) {
    return (System.currentTimeMillis() - UserManager.JOIN_TIME.getOrDefault(uuid, 0L)) / 60000;
  }

  public static void updateOnlineTime(UUID uuid) {
    GlobalUser globalUser = (GlobalUser) UserManager.getPlayerData(uuid)[0];
    globalUser.updateServerData(
        globalUser.getServerData(ConfigHandler.serverName).getOnlineTime() + calculateOnTime(uuid));
  }

  public static void updateOnlineTime(EntityPlayer player) {
    updateOnlineTime(player.getGameProfile().getId());
  }

  // TODO Implement
  @SubscribeEvent
  public void forceLoadChunk(ForceChunkEvent e) {}

  // TODO Implement
  @SubscribeEvent
  public void unloadLoadedChunk(ForceChunkEvent e) {}
}
