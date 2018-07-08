package com.wurmcraft.serveressentials.common.rank;

import com.wurmcraft.serveressentials.api.json.user.Rank;
import com.wurmcraft.serveressentials.api.json.user.fileOnly.AutoRank;
import com.wurmcraft.serveressentials.api.module.IModule;
import com.wurmcraft.serveressentials.api.module.Module;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.ServerEssentialsServer;
import com.wurmcraft.serveressentials.common.general.utils.DataHelper;
import com.wurmcraft.serveressentials.common.rank.events.WorldEvent;
import com.wurmcraft.serveressentials.common.reference.Keys;
import com.wurmcraft.serveressentials.common.utils.UserManager;
import java.io.File;
import java.util.Objects;
import net.minecraftforge.common.MinecraftForge;

@Module(name = "Rank")
public class RankModule implements IModule {

  @Override
  public void setup() {
    if (ConfigHandler.storageType.equalsIgnoreCase("File")) {
      setupRanks();
      setupAutoRanks();
      MinecraftForge.EVENT_BUS.register(new WorldEvent());
    } else {
      ServerEssentialsServer.logger.debug(
          "Rank Module enabled but not used for UserData or Ranks'" + ConfigHandler.restURL + "'");
    }
  }

  private void setupRanks() {
    File rankDir = new File(ConfigHandler.saveLocation + File.separator + Keys.RANK.name());
    if (rankDir.exists()) {
      for (File file : Objects.requireNonNull(rankDir.listFiles())) {
        Rank rank = DataHelper.load(file, Keys.RANK, new Rank());
        UserManager.rankCache.put(rank.getName(), rank);
      }
    } else {
      rankDir.mkdirs();
      createDefaultRanks();
    }
  }

  private void createDefaultRanks() {
    File groupLocation = new File(ConfigHandler.saveLocation + File.separator + Keys.RANK.name());
    if (!groupLocation.exists() || groupLocation.listFiles().length <= 0) {
      Rank defaultGroup =
          new Rank(
              "Default", "[Default]", "", null, new String[] {"common.*", "teleport.*", "claim.*"});
      Rank memberGroup =
          new Rank("Member", "[Member]", "", new String[] {"Default"}, new String[] {"perk.*"});
      Rank adminGroup =
          new Rank(
              "Admin", "[Admin]", "", new String[] {defaultGroup.getName()}, new String[] {"*"});
      DataHelper.createIfNonExist(Keys.RANK, defaultGroup);
      DataHelper.createIfNonExist(Keys.RANK, adminGroup);
      DataHelper.createIfNonExist(Keys.RANK, memberGroup);
      setupRanks();
    } else {
      groupLocation.mkdirs();
    }
  }

  private void setupAutoRanks() {
    File autoRankLocation =
        new File(ConfigHandler.saveLocation + File.separator + Keys.AUTO_RANK.name());
    if (autoRankLocation.exists()) {
      for (File file : Objects.requireNonNull(autoRankLocation.listFiles())) {
        DataHelper.load(file, Keys.AUTO_RANK, new AutoRank());
      }
    } else {
      autoRankLocation.mkdirs();
    }
  }
}
