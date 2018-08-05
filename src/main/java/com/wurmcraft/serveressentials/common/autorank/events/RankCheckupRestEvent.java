package com.wurmcraft.serveressentials.common.autorank.events;

import com.wurmcraft.serveressentials.api.json.user.Rank;
import com.wurmcraft.serveressentials.api.json.user.fileOnly.AutoRank;
import com.wurmcraft.serveressentials.api.json.user.restOnly.GlobalUser;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.autorank.AutoRankModule;
import com.wurmcraft.serveressentials.common.chat.ChatHelper;
import com.wurmcraft.serveressentials.common.language.LanguageModule;
import com.wurmcraft.serveressentials.common.rest.utils.RequestHelper;
import com.wurmcraft.serveressentials.common.utils.UserManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RankCheckupRestEvent {

  @SubscribeEvent
  public void onWorldTickEvent(LivingUpdateEvent e) {
    if (e.getEntityLiving() instanceof EntityPlayer) {
      EntityPlayer p = (EntityPlayer) e.getEntityLiving();
      if (UserManager.autoRankCache.size() > 0) {
        if (p.world.getTotalWorldTime() % (ConfigHandler.syncPeriod * 20) == 0) {
          for (EntityPlayer player : p.world.getMinecraftServer().getPlayerList().getPlayers()) {
            if (UserManager.getPlayerData(player.getGameProfile().getId()).length > 0) {
              GlobalUser data =
                  (GlobalUser) UserManager.getPlayerData(player.getGameProfile().getId())[0];
              for (AutoRank autoRank : UserManager.autoRankCache.values()) {
                if (data.getRank().equalsIgnoreCase(autoRank.getRank())
                    && AutoRankModule.verifyAutoRank(autoRank, player, data)) {
                  Rank nextRank = UserManager.getRank(autoRank.getNextRank());
                  data.setRank(nextRank.getName());
                  RequestHelper.UserResponses.overridePlayerData(data);
                  UserManager.playerData.put(
                      player.getGameProfile().getId(),
                      new Object[] {
                        data, UserManager.getPlayerData(player.getGameProfile().getId())[1]
                      });
                  ChatHelper.sendMessage(
                      player,
                      LanguageModule.getLangfromUUID(player.getGameProfile().getId())
                          .RANKUP
                          .replaceAll("%RANK%", nextRank.getPrefix()));
                  for (EntityPlayerMP users :
                      FMLCommonHandler.instance()
                          .getMinecraftServerInstance()
                          .getPlayerList()
                          .getPlayers()) {
                    ChatHelper.sendMessage(
                        users,
                        LanguageModule.getLangfromUUID(users.getGameProfile().getId())
                            .RANKUP_OTHER
                            .replaceAll(
                                "%PLAYER%",
                                UsernameCache.getLastKnownUsername(player.getGameProfile().getId()))
                            .replaceAll("%RANK%", nextRank.getPrefix()));
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}
