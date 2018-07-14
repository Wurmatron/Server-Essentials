package com.wurmcraft.serveressentials.common.language.command;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.SECommand;
import com.wurmcraft.serveressentials.api.json.user.fileOnly.PlayerData;
import com.wurmcraft.serveressentials.api.json.user.restOnly.GlobalUser;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.general.utils.DataHelper;
import com.wurmcraft.serveressentials.common.language.LanguageModule;
import com.wurmcraft.serveressentials.common.language.Local;
import com.wurmcraft.serveressentials.common.reference.Keys;
import com.wurmcraft.serveressentials.common.rest.utils.RequestHelper;
import com.wurmcraft.serveressentials.common.utils.UserManager;
import com.wurmcraft.serveressentials.common.utils.UsernameResolver;
import java.util.UUID;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

// TODO Rework Command
@Command(moduleName = "Language")
public class LangCommand extends SECommand {

  private static void setUserLang(UUID uuid, String key) {
    if (ConfigHandler.storageType.equalsIgnoreCase("File")) {
      PlayerData data = (PlayerData) UserManager.getPlayerData(uuid)[0];
      data.setLang(key);
      DataHelper.forceSave(Keys.PLAYER_DATA, data);
    } else if (ConfigHandler.storageType.equalsIgnoreCase("Rest")) {
      GlobalUser user = forceUserFromUUID(uuid);
      user.setLang(key);
      RequestHelper.UserResponses.overridePlayerData(user);
    }
  }

  @Override
  public String getName() {
    return "lang";
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "/lang <key> | <username>";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    super.execute(server, sender, args);
    if (args.length == 1 && sender.getCommandSenderEntity() instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
      Local lang = LanguageModule.getLangFromKey(args[0]);
      if (LanguageModule.isValidLangKey(args[0]) && lang != null) {
        setUserLang(player.getGameProfile().getId(), args[0]);
        sender.sendMessage(
            new TextComponentString(lang.LANGUAGE_CHANGED.replaceAll("%LANG%", args[0])));
      } else {
        sender.sendMessage(
            new TextComponentString(lang.INVALID_LANG.replaceAll("%LANG%", args[0])));
      }
    } else if (args.length == 2) {
      UUID uuid = UsernameResolver.getUUIDFromName(args[1]);
      Local lang = LanguageModule.getLangFromKey(args[0]);
      if (lang != null) {
        setUserLang(uuid, args[0]);
        sender.sendMessage(
            new TextComponentString(lang.LANGUAGE_CHANGED.replaceAll("%LANG%", args[0])));
      } else {
        sender.sendMessage(
            new TextComponentString(lang.INVALID_LANG.replaceAll("%LANG%", args[0])));
      }
    } else {
      sender.sendMessage(new TextComponentString(getUsage(sender)));
    }
  }


}
