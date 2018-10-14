package com.wurmcraft.serveressentials.common.general.commands.admin;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.common.chat.ChatHelper;
import com.wurmcraft.serveressentials.common.general.commands.utils.PlayerInventory;
import com.wurmcraft.serveressentials.common.utils.SECommand;
import com.wurmcraft.serveressentials.common.utils.UsernameResolver;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.math.BlockPos;

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
      ChatHelper.sendMessage(sender, getUsage(sender));
    }
    if (args.length == 1) {
      EntityPlayerMP player = (EntityPlayerMP) sender;
      PlayerList players = server.getServer().getPlayerList();
      if (!players.getPlayers().isEmpty()) {
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
          ChatHelper.sendMessage(
              sender, getCurrentLanguage(sender).PLAYER_NOT_FOUND.replaceAll("%PLAYER%", args[0]));
        }
      }
    }
  }

  @Override
  public List<String> getTabCompletions(
      MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
    return autoCompleteUsername(args, 0);
  }
}
