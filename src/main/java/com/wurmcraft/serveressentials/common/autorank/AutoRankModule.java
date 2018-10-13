package com.wurmcraft.serveressentials.common.autorank;

import static com.wurmcraft.serveressentials.api.json.global.Keys.AUTO_RANK;
import static com.wurmcraft.serveressentials.common.ConfigHandler.saveLocation;

import com.wurmcraft.serveressentials.api.json.global.Keys;
import com.wurmcraft.serveressentials.api.json.user.Rank;
import com.wurmcraft.serveressentials.api.json.user.file.AutoRank;
import com.wurmcraft.serveressentials.api.json.user.rest.GlobalUser;
import com.wurmcraft.serveressentials.api.module.IModule;
import com.wurmcraft.serveressentials.api.module.Module;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.ServerEssentialsServer;
import com.wurmcraft.serveressentials.common.autorank.events.RankCheckupRestEvent;
import com.wurmcraft.serveressentials.common.general.utils.DataHelper;
import com.wurmcraft.serveressentials.common.rest.RestModule;
import com.wurmcraft.serveressentials.common.rest.utils.RequestHelper;
import com.wurmcraft.serveressentials.common.utils.UserManager;
import java.io.File;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;

@Module(name = "AutoRank")
public class AutoRankModule implements IModule {

  public static void syncAutoRanks() {
    RestModule.EXECUTORs.scheduleAtFixedRate(
        () -> {
          try {
            AutoRank[] allAutoRanks = RequestHelper.AutoRankResponses.getAllAutoRanks();
            UserManager.AUTO_RANK_CACHE.clear();
            for (AutoRank rank : allAutoRanks) {
              UserManager.AUTO_RANK_CACHE.put(rank.getID(), rank);
            }
            if (UserManager.RANK_CACHE.size() == 0) {
              ServerEssentialsServer.LOGGER.debug("No AutoRank Found within the database");
            }
          } catch (Exception e) {
            ServerEssentialsServer.LOGGER.error(e.getLocalizedMessage());
          }
        },
        0L,
        ConfigHandler.syncPeriod,
        TimeUnit.MINUTES);
    ServerEssentialsServer.LOGGER.debug("Synced AutoRanks with REST API");
  }

  private static void loadAutoRanks() {
    File autoRankDir = new File(saveLocation + File.separator + AUTO_RANK.name());
    if (autoRankDir.exists()) {
      for (File file : Objects.requireNonNull(autoRankDir.listFiles())) {
        AutoRank rank = DataHelper.load(file, Keys.AUTO_RANK, new AutoRank());
        UserManager.AUTO_RANK_CACHE.put(rank.getID(), rank);
      }
    } else {
      autoRankDir.mkdirs();
    }
  }

  @Override
  public void setup() {
    if (ConfigHandler.storageType.equalsIgnoreCase("Rest")) {
      syncAutoRanks();
      MinecraftForge.EVENT_BUS.register(new RankCheckupRestEvent());
    } else if (ConfigHandler.storageType.equalsIgnoreCase("File")) {
      loadAutoRanks();
    }
  }

  public static boolean verifyAutoRank(AutoRank rank, EntityPlayer player, GlobalUser user) {
    boolean balance = rank.getBalance() == user.getBank().getCurrency(ConfigHandler.serverCurrency);
    boolean exp = rank.getExp() <= player.experienceLevel;
    boolean playTime = rank.getPlayTime() <= user.getOnlineTime();
    return balance && exp && playTime;
  }

  public static AutoRank getAutorankFromRank(Rank rank) {
    for (AutoRank auto : UserManager.AUTO_RANK_CACHE.values()) {
      if (auto.getRank().equalsIgnoreCase(rank.getName())) {
        return auto;
      }
    }
    return null;
  }

  public static AutoRank getAutorankFromRank(String rank) {
    for (AutoRank auto : UserManager.AUTO_RANK_CACHE.values()) {
      if (auto.getRank().equalsIgnoreCase(rank)) {
        return auto;
      }
    }
    return null;
  }
}
