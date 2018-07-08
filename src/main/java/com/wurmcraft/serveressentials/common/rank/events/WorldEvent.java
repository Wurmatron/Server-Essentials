package com.wurmcraft.serveressentials.common.rank.events;

import com.wurmcraft.serveressentials.api.json.user.fileOnly.PlayerData;
import com.wurmcraft.serveressentials.common.general.utils.DataHelper;
import com.wurmcraft.serveressentials.common.reference.Keys;
import com.wurmcraft.serveressentials.common.utils.UserManager;
import java.util.UUID;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class WorldEvent {

  @SubscribeEvent
  public void onJoinEvent(PlayerEvent.PlayerLoggedInEvent e) {
    boolean newPlayer =
        DataHelper.createIfNonExist(
            Keys.PLAYER_DATA,
            new PlayerData(e.player.getGameProfile().getId(), UserManager.getDefaultRank()));
    PlayerData playerData =
        (PlayerData) DataHelper.get(Keys.PLAYER_DATA, e.player.getGameProfile().getId().toString());
    if (playerData != null) {
      UserManager.playerData.put(e.player.getGameProfile().getId(), new Object[] {playerData});
      UserManager.userRanks.put(e.player.getGameProfile().getId(), playerData.getRank());
      if (newPlayer) {
        playerData.setFirstJoin();
      }
      playerData.setLastseen(System.currentTimeMillis());
      if (!UserManager.joinTime.containsKey(e.player.getGameProfile().getId())) {
        UserManager.joinTime.put(e.player.getGameProfile().getId(), System.currentTimeMillis());
      }
    }
  }

  @SubscribeEvent
  public void onLeaveEvent(PlayerEvent.PlayerLoggedOutEvent e) {
    PlayerData data =
        (PlayerData) DataHelper.get(Keys.PLAYER_DATA, e.player.getGameProfile().getId().toString());
    if (data != null) {
      data.setLastseen(System.currentTimeMillis());
      data.setOnlineTime(
          (int) (data.getOnlineTime() + calculateOnTime(e.player.getGameProfile().getId())));
      UserManager.joinTime.remove(e.player.getGameProfile().getId());
      DataHelper.forceSave(
          Keys.PLAYER_DATA, DataHelper.get(Keys.PLAYER_DATA, e.player.getGameProfile().getName()));
    }
  }

  private long calculateOnTime(UUID uuid) {
    return (System.currentTimeMillis() - UserManager.joinTime.get(uuid)) / 3600;
  }
}
