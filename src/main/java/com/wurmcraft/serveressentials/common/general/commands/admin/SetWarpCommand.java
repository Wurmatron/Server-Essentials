package com.wurmcraft.serveressentials.common.general.commands.admin;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.SECommand;
import com.wurmcraft.serveressentials.api.json.global.Keys;
import com.wurmcraft.serveressentials.api.json.global.Warp;
import com.wurmcraft.serveressentials.api.json.user.LocationWrapper;
import com.wurmcraft.serveressentials.common.chat.ChatHelper;
import com.wurmcraft.serveressentials.common.general.utils.DataHelper;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

@Command(moduleName = "General")
public class SetWarpCommand extends SECommand {

  @Override
  public String getName() {
    return "setWarp";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    super.execute(server, sender, args);
    EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
    if (args.length == 1) {
      DataHelper.createIfNonExist(
          Keys.WARP,
          new Warp(args[0], new LocationWrapper(player.getPosition(), player.dimension)));
      ChatHelper.sendMessage(
          sender, getCurrentLanguage(sender).WARP_SET.replaceAll("%NAME%", args[0]));
    } else {
      ChatHelper.sendMessage(sender, getUsage(sender));
    }
  }

  @Override
  public boolean canConsoleRun() {
    return false;
  }
}
