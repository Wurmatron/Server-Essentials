package com.wurmcraft.serveressentials.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wurmcraft.serveressentials.core.api.data.DataKey;
import com.wurmcraft.serveressentials.core.api.data.IDataHandler;
import com.wurmcraft.serveressentials.core.api.json.Language;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.core.utils.URLUtils;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Logger;

public class SECore {

  // Config
  public static ScheduledExecutorService executors;
  public static IDataHandler dataHandler;

  public static Logger logger = Logger.getLogger(Global.NAME);
  public static File SAVE_DIR = new File("" + Global.NAME.replaceAll(" ", "_"));
  public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

  public static void setup() {
    SERegistry.loadAndSetup();
    forceLanguage();
  }

  private static void forceLanguage() {
    if (SERegistry.isModuleLoaded("Language")) {
      // Force a language file (Language Module is not loaded)
      try {
        Language lang =
            GSON.fromJson(
                URLUtils.readStringFromURL(
                    "https://raw.githubusercontent.com/Wurmcraft/Server-Essentials/modular/Language/en_us.json"),
                Language.class);
        SERegistry.register(DataKey.LANGUAGE, lang);
      } catch (IOException e) {
        SECore.logger.severe("Unable to load default language file");
        e.printStackTrace();
      }
    }
  }
}
