package com.wurmcraft.serveressentials.common.teleport.commands.admin;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.json.user.LocationWrapper;
import com.wurmcraft.serveressentials.common.chat.ChatHelper;
import com.wurmcraft.serveressentials.common.teleport.utils.TeleportUtils;
import com.wurmcraft.serveressentials.common.utils.SECommand;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

@Command(moduleName = "General")
public class TopCommand extends SECommand {

  @Override
  public String getName() {
    return "top";
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return DEFAULT_COLOR + "/top";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    super.execute(server, sender, args);
    EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
    // TODO Possible CC Error
    TeleportUtils.teleportTo(
        (EntityPlayerMP) player,
        new LocationWrapper(
            player.world.getTopSolidOrLiquidBlock(player.getPosition()), player.dimension),
        true);
    ChatHelper.sendMessage(sender, getCurrentLanguage(sender).TELEPORT_TOP);
  }

  @Override
  public boolean canConsoleRun() {
    return false;
  }

  @Override
  public String getDescription(ICommandSender sender) {
    return getCurrentLanguage(sender).COMMAND_TOP;
  }
}
