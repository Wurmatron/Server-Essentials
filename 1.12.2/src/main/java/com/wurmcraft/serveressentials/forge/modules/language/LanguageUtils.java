package com.wurmcraft.serveressentials.forge.modules.language;

import static com.wurmcraft.serveressentials.core.SECore.GSON;

import com.wurmcraft.serveressentials.core.SECore;
import com.wurmcraft.serveressentials.core.api.data.DataKey;
import com.wurmcraft.serveressentials.core.api.json.Language;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.core.utils.URLUtils;
import com.wurmcraft.serveressentials.forge.common.ServerEssentialsServer;
import java.io.IOException;
import org.cliffc.high_scale_lib.NonBlockingHashMap;

public class LanguageUtils {

  public static NonBlockingHashMap<String, String> lastMessageTracker = new NonBlockingHashMap<>();

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
      ServerEssentialsServer.logger.fatal("Unable to load default language file");
      e.printStackTrace();
    }
    return loadLanguage("en_us");
  }

}
