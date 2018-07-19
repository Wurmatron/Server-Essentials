package com.wurmcraft.serveressentials.common.general;

import static com.wurmcraft.serveressentials.common.ConfigHandler.saveLocation;
import static com.wurmcraft.serveressentials.common.reference.Keys.KIT;

import com.wurmcraft.serveressentials.api.json.global.GlobalData;
import com.wurmcraft.serveressentials.api.json.global.Kit;
import com.wurmcraft.serveressentials.api.json.global.Warp;
import com.wurmcraft.serveressentials.api.module.IModule;
import com.wurmcraft.serveressentials.api.module.Module;
import com.wurmcraft.serveressentials.common.ServerEssentialsServer;
import com.wurmcraft.serveressentials.common.general.events.GlobaDataEvents;
import com.wurmcraft.serveressentials.common.general.events.MOTDEvent;
import com.wurmcraft.serveressentials.common.general.events.PlayerTickEvent;
import com.wurmcraft.serveressentials.common.general.utils.DataHelper;
import com.wurmcraft.serveressentials.common.reference.Keys;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Objects;
import net.minecraftforge.common.MinecraftForge;

@Module(name = "General")
public class GeneralModule implements IModule {

  @Override
  public void setup() {
    setupKits();
    setupGlobal();
    setupWarps();
    MinecraftForge.EVENT_BUS.register(new GlobaDataEvents());
    MinecraftForge.EVENT_BUS.register(new PlayerTickEvent());
    MinecraftForge.EVENT_BUS.register(new MOTDEvent());
  }

  private void setupKits() {
    File kitDir = new File(saveLocation + File.separator + KIT.name());
    if (kitDir.exists()) {
      for (File file : Objects.requireNonNull(kitDir.listFiles())) {
        DataHelper.load(file, Keys.KIT, new Kit());
      }
    } else {
      kitDir.mkdirs();
    }
  }

  private void setupGlobal() {
    File global = new File(saveLocation + File.separator + "Global.json");
    if (global.exists()) {
      try {
        DataHelper.globalSettings =
            DataHelper.GSON.fromJson(new FileReader(global), GlobalData.class);
      } catch (FileNotFoundException e) {
        ServerEssentialsServer.logger.warn("Failed to load '" + global.getAbsolutePath() + "'");
      }
    } else {
      GlobalData globalData =
          new GlobalData(
              null,
              new String[] {},
              new String[] {},
              "https://github.com/Wurmcraft/Server-Essentials/");
      DataHelper.forceSave(new File(saveLocation), globalData);
      setupGlobal();
    }
  }

  private void setupWarps() {
    File warpDir = new File(saveLocation + File.separator + Keys.WARP.name());
    if (warpDir.exists()) {
      for (File file : Objects.requireNonNull(warpDir.listFiles())) {
        DataHelper.load(file, Keys.WARP, new Warp());
      }
    } else {
      warpDir.mkdirs();
    }
  }
}
