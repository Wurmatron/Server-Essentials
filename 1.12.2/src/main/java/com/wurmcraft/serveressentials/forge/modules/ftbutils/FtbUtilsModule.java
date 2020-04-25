package com.wurmcraft.serveressentials.forge.modules.ftbutils;

import com.wurmcraft.serveressentials.core.SECore;
import com.wurmcraft.serveressentials.core.api.module.Module;
import com.wurmcraft.serveressentials.forge.modules.ftbutils.event.FtbUtilsEvents;
import java.io.File;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;

@Module(name = "FTBUtils", moduleDependencies = {"Rank", "Language"})
public class FtbUtilsModule {

  public static File PLAYER_RANKS = new File("local" + File.separator + "ftbutilities" + File.separator + "players.txt");

  public void initSetup() {
    if (!Loader.isModLoaded("ftbutilities")) {
      SECore.logger.info("Unable to load FTBUtils Module, Missing FTBUtils!");
    } else {
      MinecraftForge.EVENT_BUS.register(new FtbUtilsEvents());
    }
  }

  public void finalizeModule() {

  }

}
