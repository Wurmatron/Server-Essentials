package com.wurmcraft.serveressentials.forge.modules.ftbutils;

import com.feed_the_beast.ftbutilities.ranks.Ranks;
import com.wurmcraft.serveressentials.core.api.data.DataKey;
import com.wurmcraft.serveressentials.core.api.json.rank.Rank;
import com.wurmcraft.serveressentials.core.api.player.StoredPlayer;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.forge.modules.economy.EconomyConfig;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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
        if (p.startsWith("perk.claim.")) {
          try {
            total =+ Integer.parseInt(p.substring(p.lastIndexOf(".") + 1)) * ((EconomyConfig) SERegistry.getStoredData(
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
      BufferedWriter os = new BufferedWriter(
          new FileWriter(FtbUtilsModule.PLAYER_RANKS, true));
      os.append("[" + player.getGameProfile().getId().toString() + "]" + "\n");
      os.append("ftbutilities.claims.max_chunks: " + amount + "\n");
      os.append("ftbutilities.chunkloader.max_chunks: 0" + "\n");
      os.close();
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
      List<String> fileLines = FileUtils.readLines(FtbUtilsModule.PLAYER_RANKS);
      List<String> newFile = new ArrayList<>();
      for (int i = 0; i < fileLines.size(); i++) {
        if (!fileLines.get(i)
            .equals("[" + player.getGameProfile().getId().toString() + "]")) {
          newFile.add(fileLines.get(i));
        } else {
         i =+ 2;
        }
      }
      FileUtils.writeLines(FtbUtilsModule.PLAYER_RANKS, newFile, false);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
