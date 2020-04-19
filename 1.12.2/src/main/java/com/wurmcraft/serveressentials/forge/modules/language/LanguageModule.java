package com.wurmcraft.serveressentials.forge.modules.language;

import static com.wurmcraft.serveressentials.forge.modules.language.LanguageUtils.loadLanguage;

import com.wurmcraft.serveressentials.core.api.data.DataKey;
import com.wurmcraft.serveressentials.core.api.module.Module;
import com.wurmcraft.serveressentials.core.registry.SERegistry;

@Module(name = "Language")
public class LanguageModule {


  public void initSetup() {
    loadLanguage(((LanguageConfig) SERegistry.getStoredData(DataKey.MODULE_CONFIG, "Language")).defaultLang);
  }

  public void finalizeModule() {

  }
}
