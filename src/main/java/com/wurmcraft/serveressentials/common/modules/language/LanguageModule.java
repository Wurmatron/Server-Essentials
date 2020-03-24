package com.wurmcraft.serveressentials.common.modules.language;

import com.wurmcraft.serveressentials.api.ServerEssentialsAPI;
import com.wurmcraft.serveressentials.api.lang.Lang;
import com.wurmcraft.serveressentials.api.module.Module;
import com.wurmcraft.serveressentials.api.storage.json.Channel;
import com.wurmcraft.serveressentials.api.storage.json.Channel.Type;
import com.wurmcraft.serveressentials.api.user.file.FileUser;
import com.wurmcraft.serveressentials.api.user.rest.GlobalRestUser;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.ServerEssentialsServer;
import com.wurmcraft.serveressentials.common.modules.language.event.ChatEvents;
import com.wurmcraft.serveressentials.common.reference.Storage;
import com.wurmcraft.serveressentials.common.storage.file.DataHelper;
import com.wurmcraft.serveressentials.common.utils.user.UserManager;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.apache.commons.io.IOUtils;

@Module(name = "Language")
public class LanguageModule {

  public void setup() {
    MinecraftForge.EVENT_BUS.register(new ChatEvents());
    loadChannels();
    Lang defaultLang = loadLanguage(ConfigHandler.defaultLanguage);
    if (defaultLang == null) {
      throw new IllegalStateException("Unable to load default language file!");
    }
  }

  public static Lang loadLanguage(String langKey) {
    if (langKey.isEmpty()) {
      langKey = ConfigHandler.defaultLanguage;
    }
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

  public static Lang getUserLanguage(ICommandSender sender) {
    if (sender.getCommandSenderEntity() instanceof EntityPlayer) {
      return getUserLanguage((EntityPlayer) sender.getCommandSenderEntity());
    }
    return getDefaultLang();
  }

  private static Lang loadUserLang(Lang userLang, String lang) {
    if (userLang == null) {
      userLang = loadLanguage(lang);
      if (lang.length() > 0) {
        ServerEssentialsServer.LOGGER.info("Loading Language '" + lang + "'");
      } else {
        return getDefaultLang();
      }
    }
    return userLang;
  }

  public static Channel[] loadChannels() {
    try {
      return DataHelper.load(Storage.CHANNEL, new Channel[0], new Channel());
    } catch (Exception e) {
      Channel global = new Channel("global", "[G]", Type.PUBLIC, "");
      DataHelper.save(
          new File(ConfigHandler.saveLocation + File.separator + Storage.CHANNEL), global);
      return loadChannels();
    }
  }

  public static List<EntityPlayerMP> getPlayersInChannel(Channel channel) {
    List<EntityPlayerMP> players = new ArrayList<>();
    for (EntityPlayerMP player :
        FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()) {
      Channel ch = UserManager.getUserChannel(player.getGameProfile().getId());
      if (channel.getName().equalsIgnoreCase(ch.getName())) {
        players.add(player);
      }
    }
    return players;
  }
}
