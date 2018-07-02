package com.wurmcraft.serveressentials.common.language;

import com.wurmcraft.serveressentials.api.json.user.fileOnly.PlayerData;
import com.wurmcraft.serveressentials.api.json.user.restOnly.GlobalUser;
import com.wurmcraft.serveressentials.api.module.IModule;
import com.wurmcraft.serveressentials.api.module.Module;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.ServerEssentialsServer;
import com.wurmcraft.serveressentials.common.general.utils.DataHelper;
import com.wurmcraft.serveressentials.common.reference.Global;
import com.wurmcraft.serveressentials.common.utils.UserManager;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.UUID;
import org.apache.commons.io.FileUtils;

@Module(name = "Language")
public class LanguageModule implements IModule {

  public static HashMap<String, Local> loadedLanguages = new HashMap<>();

  @Override
  public void setup() {
    for (String langKey : ConfigHandler.supportedLanguages) {
      save(
          Global.LOCAL_WEB + langKey + ".lang",
          new File(
              ConfigHandler.saveLocation
                  + File.separator
                  + "Language"
                  + File.separator
                  + langKey
                  + ".json"));
      cacheLanguages();
    }
  }

  private void save(String webFile, File saveLocation) {
    URL textFile = null;
    File location;
    try {
      textFile = new URL(webFile);
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
    location = saveLocation;
    if (location != null) {
      try {
        if (!location.exists()) {
          FileUtils.copyURLToFile(textFile, location, 10000, 12000);
        }
      } catch (IOException e) {
        ServerEssentialsServer.logger.error("Cannot read " + textFile.getPath() + " I/O Exception");
        e.printStackTrace();
      }
    }
  }

  private void cacheLanguages() {
    for (String langKey : ConfigHandler.supportedLanguages) {
      try {
        Local local =
            DataHelper.GSON.fromJson(
                new FileReader(
                    new File(
                        ConfigHandler.saveLocation
                            + File.separator
                            + "Language"
                            + File.separator
                            + langKey
                            + ".json")),
                Local.class);
        loadedLanguages.put(langKey, local);
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
    }
  }

  public static Local getLangfromUUID(UUID uuid) {
    return loadedLanguages.getOrDefault(
        getPlayerLang(uuid), loadedLanguages.get(ConfigHandler.defaultLanguage));
  }

  public static Local getLangFromKey(String langKey) {
    return loadedLanguages.getOrDefault(
        langKey, loadedLanguages.get(ConfigHandler.defaultLanguage));
  }

  private static String getPlayerLang(UUID uuid) {
    if (ConfigHandler.storageType.equalsIgnoreCase("File")) {
      PlayerData data = (PlayerData) UserManager.getPlayerData(uuid)[0];
      return data.getLang();
    } else if (ConfigHandler.storageType.equalsIgnoreCase("Rest")) {
      GlobalUser user = (GlobalUser) UserManager.getPlayerData(uuid)[0];
      return user.getLang();
    }
    return ConfigHandler.defaultLanguage;
  }
}
