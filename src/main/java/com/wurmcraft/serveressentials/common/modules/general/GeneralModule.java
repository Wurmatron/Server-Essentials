package com.wurmcraft.serveressentials.common.modules.general;

import com.wurmcraft.serveressentials.api.module.Module;
import com.wurmcraft.serveressentials.api.storage.json.GlobalConfig;
import com.wurmcraft.serveressentials.api.storage.json.Kit;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.modules.general.event.GeneralEvents;
import com.wurmcraft.serveressentials.common.reference.Storage;
import com.wurmcraft.serveressentials.common.storage.file.DataHelper;
import java.io.File;
import net.minecraftforge.common.MinecraftForge;

@Module(name = "General")
public class GeneralModule {

  public static GlobalConfig config;

  public void setup() {
    MinecraftForge.EVENT_BUS.register(new GeneralEvents());
    loadAndSetupGlobal();
    DataHelper.load(Storage.KIT, new Kit[0], new Kit());
  }

  public static void loadAndSetupGlobal() {
    config =
        DataHelper.load(
            new File(ConfigHandler.saveLocation + File.separator + "Global.json"),
            "",
            new GlobalConfig());
    if (config == null) {
      config = new GlobalConfig("https://discord.gg/n6RFDUc", "https://www.wurmatron.io/");
      DataHelper.save(new File(ConfigHandler.saveLocation), config);
    }
  }
}
