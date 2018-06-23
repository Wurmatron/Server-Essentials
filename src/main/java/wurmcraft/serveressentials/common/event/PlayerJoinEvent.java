package wurmcraft.serveressentials.common.event;

import java.util.HashMap;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import wurmcraft.serveressentials.common.api.storage.PlayerData;
import wurmcraft.serveressentials.common.chat.ChannelManager;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.config.ConfigHandler;
import wurmcraft.serveressentials.common.reference.Keys;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.security.SecurityUtils;
import wurmcraft.serveressentials.common.utils.DataHelper2;
import wurmcraft.serveressentials.common.utils.RankManager;

public class PlayerJoinEvent {

  public static HashMap<UUID, Long> joinTime = new HashMap<>();

  @SubscribeEvent
  public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent e) {
    boolean newPlayer = DataHelper2.createIfNonExist(Keys.PLAYER_DATA,
        new PlayerData(e.player.getGameProfile().getId(), RankManager.getDefaultRank()));
    PlayerData playerData = (PlayerData) DataHelper2
        .get(Keys.PLAYER_DATA, e.player.getGameProfile().getId().toString());
    if (playerData != null) {
      if (newPlayer) {
        playerData.setFirstJoin();
      }
      playerData.setLastseen(System.currentTimeMillis());
      if (ConfigHandler.forceDefaultChannelOnJoin) {
        playerData.setCurrentChannel(ChannelManager.getFromName(ConfigHandler.defaultChannel));
        ChannelManager.setPlayerChannel(e.player.getGameProfile().getId(),
            ChannelManager.getFromName(playerData.getCurrentChannel()));
      }
      if (playerData.getMail() != null && playerData.getMail().size() > 0) {
        ChatHelper.sendMessageTo(e.player, Local.HAS_MAIL);
      }
      if (!joinTime.containsKey(e.player.getGameProfile().getId())) {
        joinTime.put(e.player.getGameProfile().getId(), System.currentTimeMillis());
      }
      if (playerData.isFrozen()) {
        PlayerTickEvent
            .addFrozen(e.player, new BlockPos(e.player.posX, e.player.posY, e.player.posZ));
      }
      if (!SecurityUtils.isTrustedMember(e.player)
          && DataHelper2.globalSettings.getBannedMods().length > 0) {
        for (String id : SecurityUtils.getPlayerMods(e.player)) {
          for (String modid : DataHelper2.globalSettings.getBannedMods()) {
            if (modid.equalsIgnoreCase(id)) {
              ((EntityPlayerMP) e.player).connection
                  .disconnect(new TextComponentString(Local.BANNED_MOD.replaceAll("#", id)));
            }
          }
        }
      }
    }
    String[] motd = DataHelper2.globalSettings.getMotd();
    if (motd != null && motd.length > 0) {
      for (String mod : motd) {
        ChatHelper.sendMessageTo(e.player, mod.replaceAll("&", "\u00A7"));
      }
    }
  }
}
