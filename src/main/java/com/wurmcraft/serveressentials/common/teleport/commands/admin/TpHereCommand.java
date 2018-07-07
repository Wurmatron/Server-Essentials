package com.wurmcraft.serveressentials.common.teleport.commands.admin;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.SECommand;
import com.wurmcraft.serveressentials.common.language.LanguageModule;
import com.wurmcraft.serveressentials.common.teleport.utils.TeleportUtils;
import com.wurmcraft.serveressentials.common.utils.UsernameResolver;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

@Command(moduleName = "Teleportation")
public class TpHereCommand extends SECommand {

  @Override
  public String getName() {
    return "tpHere";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    super.execute(server, sender, args);
    EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
    if (args.length == 1) {
      EntityPlayer herePlayer = UsernameResolver.getPlayer(args[0]);
      TeleportUtils.teleportTo(herePlayer, player);
      sender.sendMessage(
          new TextComponentString(
              getCurrentLanguage(sender)
                  .TP_HERE
                  .replaceAll("%PLAYER%", herePlayer.getDisplayNameString())));
      herePlayer.sendMessage(
          new TextComponentString(
              LanguageModule.getLangfromUUID(herePlayer.getGameProfile().getId()).TP));
    } else {
      sender.sendMessage(new TextComponentString(getUsage(sender)));
    }
  }

  @Override
  public boolean canConsoleRun() {
    return false;
  }
}
