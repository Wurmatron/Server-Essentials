package com.wurmcraft.serveressentials.common.modules.language.command;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.api.command.SubCommand;
import com.wurmcraft.serveressentials.api.lang.Lang;
import com.wurmcraft.serveressentials.api.storage.FileType;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.modules.language.ChatHelper;
import com.wurmcraft.serveressentials.common.modules.language.LanguageModule;
import com.wurmcraft.serveressentials.common.reference.Replacment;
import com.wurmcraft.serveressentials.common.reference.Storage;
import com.wurmcraft.serveressentials.common.storage.file.DataHelper;
import com.wurmcraft.serveressentials.common.utils.user.UserManager;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

@ModuleCommand(moduleName = "Language")
public class LanguageCommand extends Command {

  @Override
  public String getName() {
    return "Language";
  }

  @Override
  public String getUsage(Lang lang) {
    return "/language <key | reload>";
  }

  @Override
  public String getDescription(Lang lang) {
    return lang.local.DESCRIPTION_LANGUAGE;
  }

  @Override
  public void execute(
      MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    if (args.length == 1 && !args[0].equalsIgnoreCase("reload")) {
      if (sender.getCommandSenderEntity() instanceof EntityPlayer) {
        Lang lang = (Lang) DataHelper.get(Storage.LANGUAGE, args[0]);
        if (lang != null) {
          UserManager.setLanguage((EntityPlayer) sender.getCommandSenderEntity(), lang);
          ChatHelper.sendMessage(
              sender, senderLang.local.LANG_LANG_SET.replaceAll(Replacment.LANGUAGE, lang.getID()));
        } else {
          ChatHelper.sendMessage(
              sender, senderLang.local.LANG_LANG_INVAID.replaceAll(Replacment.LANGUAGE, args[0]));
        }
      } else { // Set Default Server Language
        Lang lang = (Lang) DataHelper.get(Storage.LANGUAGE, args[0]);
        if (lang != null) {
          ConfigHandler.defaultLanguage = lang.getID();
          ChatHelper.sendMessage(
              sender, senderLang.local.LANG_LANG_SET.replaceAll(Replacment.LANGUAGE, lang.getID()));
        } else {
          ChatHelper.sendMessage(
              sender, senderLang.local.LANG_LANG_INVAID.replaceAll(Replacment.LANGUAGE, args[0]));
        }
      }
    } else if (args.length == 0) {
      ChatHelper.sendMessage(sender, getUsage(senderLang));
    }
  }

  @SubCommand
  public void reload(
      MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    for (FileType type : DataHelper.getData(Storage.LANGUAGE)) {
      LanguageModule.loadLanguage(type.getID());
    }
    ChatHelper.sendMessage(sender, senderLang.local.LANG_RELOAD);
  }

  @Override
  public List<String> getAliases(List<String> aliases) {
    aliases.add("Lang");
    return aliases;
  }

  @Override
  public List<String> getAutoCompletion(
      MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
    List<String> validLang = new ArrayList<>();
    for (FileType type : DataHelper.getData(Storage.LANGUAGE)) {
      validLang.add(type.getID());
    }
    return validLang;
  }

  @Override
  public boolean canConsoleRun() {
    return true;
  }
}
