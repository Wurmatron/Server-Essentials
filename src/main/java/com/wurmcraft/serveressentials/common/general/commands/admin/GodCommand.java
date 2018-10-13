package com.wurmcraft.serveressentials.common.general.commands.admin;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.common.chat.ChatHelper;
import com.wurmcraft.serveressentials.common.utils.SECommand;
import com.wurmcraft.serveressentials.common.utils.UsernameResolver;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

// TODO Rework Command
@Command(moduleName = "General")
public class GodCommand extends SECommand {

  @Override
  public String getName() {
    return "god";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    super.execute(server, sender, args);
    EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
    if (args.length == 1) {
      player = UsernameResolver.getPlayer(args[0]);
      if (player == null) {
        ChatHelper.sendMessage(
            sender, getCurrentLanguage(sender).PLAYER_NOT_FOUND.replaceAll("%PLAYER%", args[0]));
        return;
      }
    }
    if (player.capabilities.disableDamage) {
      player.capabilities.disableDamage = false;
      ChatHelper.sendMessage(sender, getCurrentLanguage(player).GOD_DISABLE);
    } else {
      player.capabilities.disableDamage = true;
      ChatHelper.sendMessage(sender, getCurrentLanguage(player).GOD_ENABBLE);
    }
  }

  @Override
  public boolean canConsoleRun() {
    return false;
  }
}
