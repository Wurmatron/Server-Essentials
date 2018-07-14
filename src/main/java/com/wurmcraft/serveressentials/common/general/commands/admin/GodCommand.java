package com.wurmcraft.serveressentials.common.general.commands.admin;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.SECommand;
import com.wurmcraft.serveressentials.common.utils.UsernameResolver;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

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
    if (args.length == 0) {
      EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
      if (player.capabilities.disableDamage) {
        player.capabilities.disableDamage = false;
        sender.sendMessage(new TextComponentString(getCurrentLanguage(sender).GOD_DISABLE));
      } else {
        player.capabilities.disableDamage = true;
        sender.sendMessage(new TextComponentString(getCurrentLanguage(sender).GOD_ENABBLE));
      }
    } else if (args.length == 1) {
      EntityPlayer player = UsernameResolver.getPlayer(args[0]);
      if (player.capabilities.disableDamage) {
        player.capabilities.disableDamage = false;
        player.sendMessage(new TextComponentString(getCurrentLanguage(player).GOD_DISABLE));
      } else {
        player.capabilities.disableDamage = true;
        player.sendMessage(new TextComponentString(getCurrentLanguage(player).GOD_ENABBLE));
      }
    }
  }

  @Override
  public boolean canConsoleRun() {
    return false;
  }
}
