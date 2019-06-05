package com.wurmcraft.serveressentials.common.modules.general.command;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.api.lang.Lang;
import com.wurmcraft.serveressentials.common.modules.language.ChatHelper;
import com.wurmcraft.serveressentials.common.reference.Replacment;
import java.util.List;
import joptsimple.internal.Strings;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

@ModuleCommand(moduleName = "General")
public class RenameCommand extends Command {

  @Override
  public String getName() {
    return "Rename";
  }

  @Override
  public String getUsage(Lang lang) {
    return "/Rename <name>";
  }

  @Override
  public String getDescription(Lang lang) {
    return lang.local.DESCRIPTION_RENAME;
  }

  @Override
  public void execute(
      MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    ((EntityPlayer) sender.getCommandSenderEntity())
        .getHeldItemMainhand()
        .setStackDisplayName(Strings.join(args, " ").replaceAll("&", Replacment.FORMATTING_CODE));
    ChatHelper.sendMessage(
        sender,
        senderLang.local.GENERAL_RENAME.replaceAll(
            Replacment.NAME, Strings.join(args, " ").replaceAll("&", Replacment.FORMATTING_CODE)));
  }

  @Override
  public List<String> getAliases(List<String> aliases) {
    aliases.add("Name");
    return aliases;
  }
}
