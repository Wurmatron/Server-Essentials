package com.wurmcraft.serveressentials.forge.modules.language;

import static com.wurmcraft.serveressentials.forge.modules.language.LanguageUtils.loadLanguage;

import com.wurmcraft.serveressentials.core.api.data.DataKey;
import com.wurmcraft.serveressentials.core.api.module.Module;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.forge.modules.language.event.ChatEvents;
import net.minecraftforge.common.MinecraftForge;

@Module(name = "Language")
public class LanguageModule {

  public void initSetup() {
    loadLanguage(((LanguageConfig) SERegistry
        .getStoredData(DataKey.MODULE_CONFIG, "Language")).defaultLang);
    MinecraftForge.EVENT_BUS.register(new ChatEvents());
  }

  public void finalizeModule() {

  }
}
