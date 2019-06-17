package com.wurmcraft.serveressentials.common.modules.general.command;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.api.command.SubCommand;
import com.wurmcraft.serveressentials.api.lang.Lang;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.modules.general.GeneralModule;
import com.wurmcraft.serveressentials.common.modules.language.ChatHelper;
import com.wurmcraft.serveressentials.common.reference.Replacment;
import com.wurmcraft.serveressentials.common.storage.file.DataHelper;
import java.io.File;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

@ModuleCommand(moduleName = "General")
public class DiscordCommand extends Command {

  @Override
  public String getName() {
    return "discord";
  }

  @Override
  public String getUsage(Lang lang) {
    return "/discord <set> <link>";
  }

  @Override
  public String getDescription(Lang lang) {
    return lang.local.DESCRIPTION_DISCORD;
  }

  @Override
  public void execute(
      MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    if (args.length == 0) {
      ChatHelper.sendMessage(
          sender,
          senderLang.local.GENERAL_DISCORD_LINK.replaceAll(
              Replacment.DISCORD, GeneralModule.config.discordLink));
    } else {
      ChatHelper.sendMessage(sender, getUsage(senderLang));
    }
  }

  @SubCommand
  public void set(MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    if (args.length == 1) {
      GeneralModule.config.discordLink = args[0];
      DataHelper.save(
          new File(ConfigHandler.saveLocation + File.separator + "Global.json"),
          GeneralModule.config);
      ChatHelper.sendMessage(
          sender, senderLang.local.GENERAL_DISCORD_SET.replaceAll(Replacment.DISCORD, args[0]));
    } else {
      ChatHelper.sendMessage(sender, getUsage(senderLang));
    }
  }
}