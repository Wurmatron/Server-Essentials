package com.wurmcraft.serveressentials.common.general.commands.item;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.SECommand;
import com.wurmcraft.serveressentials.common.chat.ChatHelper;
import joptsimple.internal.Strings;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

// TODO Rework Command
@Command(moduleName = "General")
public class RenameCommand extends SECommand {

  @Override
  public String getName() {
    return "rename";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    super.execute(server, sender, args);
    if (args.length > 0) {
      EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
      if (player.getHeldItemMainhand() != null) {
        player
            .getHeldItemMainhand()
            .setStackDisplayName(Strings.join(args, " ").replaceAll("&", "\u00A7"));
        ChatHelper.sendMessage(
            player,
            getCurrentLanguage(sender)
                .NAME_CHANGED
                .replaceAll("%NAME%", Strings.join(args, " ").replaceAll("&", "\u00A7")));
      } else {
        ChatHelper.sendMessage(sender, getCurrentLanguage(sender).NO_ITEM);
      }
    } else {
      sender.sendMessage(new TextComponentString(getUsage(sender)));
    }
  }

  @Override
  public boolean canConsoleRun() {
    return false;
  }
}
