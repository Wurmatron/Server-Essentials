package com.wurmcraft.serveressentials.common.modules.language;

import com.wurmcraft.serveressentials.api.ServerEssentialsAPI;
import com.wurmcraft.serveressentials.api.lang.Lang;
import com.wurmcraft.serveressentials.api.module.Module;
import com.wurmcraft.serveressentials.api.user.file.FileUser;
import com.wurmcraft.serveressentials.api.user.rest.GlobalRestUser;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.ServerEssentialsServer;
import com.wurmcraft.serveressentials.common.reference.Storage;
import com.wurmcraft.serveressentials.common.storage.file.DataHelper;
import com.wurmcraft.serveressentials.common.utils.user.UserManager;
import java.io.IOException;
import java.net.URL;
import net.minecraft.entity.player.EntityPlayer;
import org.apache.commons.io.IOUtils;
import sun.plugin.dom.exception.InvalidStateException;

@Module(name = "Language")
public class LanguageModule {

  public void setup() {
    Lang defaultLang = loadLanguage(ConfigHandler.defaultLanguage);
    if (defaultLang == null) {
      throw new InvalidStateException("Unable to load default language file!");
    }
  }

  public static Lang loadLanguage(String langKey) {
    try {
      String url = IOUtils.toString(new URL(ConfigHandler.languageURLFormat + langKey + ".json"));
      DataHelper.save(Storage.LANGUAGE, new Lang(langKey, url));
      return DataHelper.load(Storage.LANGUAGE, new Lang(langKey));
    } catch (IOException e) {
      ServerEssentialsServer.LOGGER.error(e.getLocalizedMessage());
    }
    ServerEssentialsServer.LOGGER.error("Unable to load '" + langKey + "'");
    return null;
  }

  public static Lang getDefaultLang() {
    return (Lang) DataHelper.get(Storage.LANGUAGE, ConfigHandler.defaultLanguage);
  }

  public static Lang getUserLanguage(EntityPlayer player) {
    if (ServerEssentialsAPI.storageType.equalsIgnoreCase("Rest")) {
      GlobalRestUser user = (GlobalRestUser) UserManager.getUserData(player)[0];
      Lang userLang = (Lang) DataHelper.get(Storage.LANGUAGE, user.getLang());
      userLang = loadUserLang(userLang, user.getLang());
      if (userLang != null) {
        return userLang;
      }
    } else if (ServerEssentialsAPI.storageType.equalsIgnoreCase("File")) {
      FileUser fileUser = (FileUser) UserManager.getUserData(player)[0];
      Lang userLang = (Lang) DataHelper.get(Storage.LANGUAGE, fileUser.getLang());
      userLang = loadUserLang(userLang, fileUser.getLang());
      if (userLang != null) {
        return userLang;
      }
    }
    return getDefaultLang();
  }

  private static Lang loadUserLang(Lang userLang, String lang) {
    if (userLang == null) {
      userLang = loadLanguage(lang);
      ServerEssentialsServer.LOGGER.info("Loading Language '" + lang + "'");
    }
    return userLang;
  }
}
