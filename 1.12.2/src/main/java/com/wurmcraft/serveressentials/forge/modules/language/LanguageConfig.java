package com.wurmcraft.serveressentials.forge.modules.language;

import com.wurmcraft.serveressentials.core.api.data.StoredDataType;
import com.wurmcraft.serveressentials.core.api.module.ConfigModule;

@ConfigModule(moduleName = "Language")
public class LanguageConfig implements StoredDataType {

  public String defaultLang;
  public String languageLocation;
  public String defaultChannel;

  public LanguageConfig() {
    this.defaultLang = "en_us";
    this.languageLocation = "https://raw.githubusercontent.com/Wurmcraft/Server-Essentials/modular/Language/";
    this.defaultChannel = "global";
  }

  public LanguageConfig(String defaultLang, String languageLocation,
      String defaultChannel) {
    this.defaultLang = defaultLang;
    this.languageLocation = languageLocation;
    this.defaultChannel = defaultChannel;
  }

  @Override
  public String getID() {
    return "Language";
  }
}
