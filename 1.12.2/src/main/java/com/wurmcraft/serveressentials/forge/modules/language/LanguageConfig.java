package com.wurmcraft.serveressentials.forge.modules.language;

import static com.wurmcraft.serveressentials.forge.modules.language.LanguageUtils.loadLanguage;

import com.wurmcraft.serveressentials.core.api.data.StoredDataType;
import com.wurmcraft.serveressentials.core.api.module.ConfigModule;

@ConfigModule(moduleName = "Language")
public class LanguageConfig implements StoredDataType {

  public String defaultLang;
  public String languageLocation;

  public LanguageConfig() {
    this.defaultLang = "en_us";
    this.languageLocation = "https://raw.githubusercontent.com/Wurmcraft/Server-Essentials/modular/Language/";
  }

  public LanguageConfig(String defaultLang, String languageLocation) {
    this.defaultLang = defaultLang;
    this.languageLocation = languageLocation;
  }

  @Override
  public String getID() {
    return "Language";
  }
}
