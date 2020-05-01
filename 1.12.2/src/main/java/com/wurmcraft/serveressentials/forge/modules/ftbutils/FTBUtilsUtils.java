package com.wurmcraft.serveressentials.forge.modules.ftbutils;

import com.feed_the_beast.ftbutilities.ranks.Ranks;
import com.wurmcraft.serveressentials.core.api.data.DataKey;
import com.wurmcraft.serveressentials.core.api.json.rank.Rank;
import com.wurmcraft.serveressentials.core.api.player.StoredPlayer;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.forge.modules.economy.EconomyConfig;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import org.apache.commons.io.FileUtils;

public class FTBUtilsUtils {

  public static int getMaxClaims(StoredPlayer player, Rank rank) {
    int total = 0;
    for (String p : rank.getPermission()) {
      if (p.startsWith("ftbutils.claim.")) {
        try {
          total = Integer.parseInt(p.substring(p.lastIndexOf(".") + 1));
        } catch (NumberFormatException ignored) {
        }
      }
    }
    if (player.global.perks != null && player.global.perks.length > 0) {
      for (String p : player.global.perks) {
        if (p.startsWith("claimblocks.amount.")) {
          try {
            total = +Integer.parseInt(p.substring(p.lastIndexOf(".") + 1))
                * ((EconomyConfig) SERegistry.getStoredData(
                DataKey.MODULE_CONFIG, "Economy")).claimAmountPerLevel;
          } catch (NumberFormatException ignored) {
          }
        }
      }
    }
    return total;
  }

  public static void setPlayerClaimBlocks(EntityPlayer player, int amount) {
    try {
      FileWriter writer = new FileWriter(FtbUtilsModule.PLAYER_RANKS, true);
      writer.append("[" + player.getGameProfile().getId().toString() + "]" + "\n"
          + "ftbutilities.claims.max_chunks: " + amount + "\n"
          + "ftbutilities.chunkloader.max_chunks: 0" + "\n");
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    Ranks.INSTANCE.reload();
  }

  public static void updatePlayerClaimBlocks(EntityPlayer player, int amount) {
    deletePlayerClaimBlocks(player);
    setPlayerClaimBlocks(player, amount);
  }

  private static void deletePlayerClaimBlocks(EntityPlayer player) {
    try {
     List<String> fileData = Files.readAllLines(FtbUtilsModule.PLAYER_RANKS.toPath());
     for(int i = 0; i < fileData.size(); i++) {
       if(fileData.get(i).startsWith("[" + player.getGameProfile().getId().toString())) {
         fileData.remove(i);
         fileData.remove(i);
         fileData.remove(i);
         i += 2;
       }
     }
      Files.write(FtbUtilsModule.PLAYER_RANKS.toPath(),fileData, StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
