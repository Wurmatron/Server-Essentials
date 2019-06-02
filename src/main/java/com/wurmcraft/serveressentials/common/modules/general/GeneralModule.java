package com.wurmcraft.serveressentials.common.modules.general;

import com.wurmcraft.serveressentials.api.module.Module;
import com.wurmcraft.serveressentials.api.storage.json.GlobalConfig;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.modules.general.event.GeneralEvents;
import com.wurmcraft.serveressentials.common.storage.file.DataHelper;
import java.io.File;
import net.minecraftforge.common.MinecraftForge;

@Module(name = "General")
public class GeneralModule {

  public static GlobalConfig config;

  public void setup() {
    MinecraftForge.EVENT_BUS.register(new GeneralEvents());
    config =
        DataHelper.load(
            new File(ConfigHandler.saveLocation + File.separator + "Global.json"),
            "",
            new GlobalConfig());
    if (config == null) {
      config = new GlobalConfig("", "");
      DataHelper.save(new File(ConfigHandler.saveLocation), config);
    }
  }
}
