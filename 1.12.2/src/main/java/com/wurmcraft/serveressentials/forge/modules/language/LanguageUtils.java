package com.wurmcraft.serveressentials.forge.modules.language;

import static com.wurmcraft.serveressentials.core.SECore.GSON;

import com.wurmcraft.serveressentials.core.SECore;
import com.wurmcraft.serveressentials.core.api.data.DataKey;
import com.wurmcraft.serveressentials.core.api.json.Language;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.core.utils.URLUtils;
import java.io.IOException;

public class LanguageUtils {

  public static Language loadLanguage(String key) {
    try {
      Language lang =
          GSON.fromJson(
              URLUtils.readStringFromURL(
                  ((LanguageConfig) SERegistry
                      .getStoredData(DataKey.MODULE_CONFIG, "Language")).languageLocation
                      + key + ".json"),
              Language.class);
      SERegistry.register(DataKey.LANGUAGE, lang);
      return lang;
    } catch (IOException e) {
      SECore.logger.severe("Unable to load default language file");
      e.printStackTrace();
    }
    return loadLanguage("en_us");
  }

}
