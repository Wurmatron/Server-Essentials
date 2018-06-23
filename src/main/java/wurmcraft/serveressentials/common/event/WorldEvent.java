package wurmcraft.serveressentials.common.event;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import wurmcraft.serveressentials.common.api.storage.AutoRank;
import wurmcraft.serveressentials.common.api.storage.PlayerData;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.reference.Keys;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.utils.DataHelper2;
import wurmcraft.serveressentials.common.utils.RankManager;
import wurmcraft.serveressentials.common.utils.UsernameResolver;

public class WorldEvent {

  public static void checkAndHandleAutoRank(EntityPlayer player) {
    PlayerData data = UsernameResolver.getPlayerData(player.getGameProfile().getId());
    if (DataHelper2.getData(Keys.AUTO_RANK).size() > 0) {
      for (AutoRank autoRank : DataHelper2.getData(Keys.AUTO_RANK, new AutoRank())) {
        if (autoRank.getRank().equalsIgnoreCase(data.getRank().getName())
            && autoRank.getPlayTime() <= data.getOnlineTime() && autoRank.getBalance() <= data
            .getMoney() && autoRank.getExp() <= player.experienceLevel) {
          data.setRank(RankManager.getRankFromName(autoRank.getNextRank()));
          ChatHelper.sendMessageTo(player, Local.RANK_UP
              .replaceAll("#", RankManager.getRankFromName(autoRank.getNextRank()).getName()));
          FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().sendMessage(
              new TextComponentString(Local.RANK_UP_NOTIFY.replaceAll("#",
                  UsernameCache.getLastKnownUsername(player.getGameProfile().getId()))
                  .replaceAll("~", RankManager.getRankFromName(autoRank.getNextRank()).getName())));
        }
      }
    }
  }

  @SubscribeEvent
  public void onWorldTick(TickEvent.WorldTickEvent e) {
    if (e.world.getWorldTime() % 12000 == 0) {
      for (EntityPlayerMP player : FMLCommonHandler.instance().getMinecraftServerInstance()
          .getPlayerList().getPlayers()) {
        PlayerData data = UsernameResolver.getPlayerData(player.getGameProfile().getId());
        long timeSinceLastUpdate = PlayerJoinEvent.joinTime.get(player.getGameProfile().getId());
        long timeSinceUpdate = System.currentTimeMillis() - timeSinceLastUpdate;
        int timeGained = (int) (timeSinceUpdate / 60000);
        data.setOnlineTime(data.getOnlineTime() + timeGained);
        PlayerJoinEvent.joinTime.put(player.getGameProfile().getId(), System.currentTimeMillis());
        checkAndHandleAutoRank(player);
      }
    }
  }
}
