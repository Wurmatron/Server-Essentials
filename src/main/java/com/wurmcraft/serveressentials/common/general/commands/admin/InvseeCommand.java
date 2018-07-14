package com.wurmcraft.serveressentials.common.general.commands.admin;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.SECommand;
import com.wurmcraft.serveressentials.common.general.commands.utils.PlayerInventory;
import com.wurmcraft.serveressentials.common.utils.UsernameResolver;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.text.TextComponentString;

// TODO Rework Command
@Command(moduleName = "General")
public class InvseeCommand extends SECommand {

  @Override
  public String getName() {
    return "invsee";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    super.execute(server, sender, args);
    if (args.length == 0) {
      sender.sendMessage(new TextComponentString(getUsage(sender)));
    }
    if (args.length == 1) {
      EntityPlayerMP player = (EntityPlayerMP) sender;
      PlayerList players = server.getServer().getPlayerList();
      if (players.getPlayers().size() > 0) {
        boolean open = false;
        EntityPlayer victim = UsernameResolver.getPlayer(args[0]);
        if (victim != null) {
          if (player.openContainer != player.inventoryContainer) {
            player.closeScreen();
          }
          player.displayGUIChest(new PlayerInventory((EntityPlayerMP) victim, player));
          open = true;
        }
        if (!open) {
          sender.sendMessage(
              new TextComponentString(
                  getCurrentLanguage(sender).PLAYER_NOT_FOUND.replaceAll("%PLAYER%", args[0])));
        }
      }
    }
  }
}
