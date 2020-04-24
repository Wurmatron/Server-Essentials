package com.wurmcraft.serveressentials.forge.modules.rank;

import com.wurmcraft.serveressentials.core.SECore;
import com.wurmcraft.serveressentials.core.api.data.DataKey;
import com.wurmcraft.serveressentials.core.api.json.rank.Rank;
import com.wurmcraft.serveressentials.core.api.player.StoredPlayer;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import org.cliffc.high_scale_lib.NonBlockingHashMap;

public class RankUtils {

  public static void scheduleRankUpdates(int delayPeriod) {
    SECore.executors.scheduleAtFixedRate(() -> {
      NonBlockingHashMap<String, Rank> data = SECore.dataHandler
          .getDataFromKey(DataKey.RANK, new Rank());
      if (data.isEmpty()) {
        SECore.logger.warning("Connection to rank database is broken!");
      }
    }, 300, delayPeriod, TimeUnit.SECONDS);
  }

  public static boolean hasPermission(Rank rank, String perm) {
    return false;
  }

  public static Rank getRank(ICommandSender sender) {
    if (sender != null && sender.getCommandSenderEntity() instanceof EntityPlayer) {
      try {
        StoredPlayer playerData = (StoredPlayer) SERegistry.getStoredData(DataKey.PLAYER,
            ((EntityPlayer) sender.getCommandSenderEntity()).getGameProfile().getId()
                .toString());
        return (Rank) SERegistry.getStoredData(DataKey.RANK, playerData.global.rank);
      } catch (NoSuchElementException e) {
        SECore.logger
            .info(sender.getName() + " does not have any loaded data / a valid rank!");
        return new Rank();
      }
    }
    return new Rank(); // TODO Change to default console rank
  }

}
