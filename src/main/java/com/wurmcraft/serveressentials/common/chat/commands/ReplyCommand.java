package com.wurmcraft.serveressentials.common.chat.commands;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.SECommand;
import com.wurmcraft.serveressentials.common.general.utils.DataHelper;
import com.wurmcraft.serveressentials.common.reference.Keys;
import joptsimple.internal.Strings;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;

// TODO Rework Command
@Command(moduleName = "Chat")
public class ReplyCommand extends SECommand {

  @Override
  public String getName() {
    return "reply";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    super.execute(server, sender, args);
    EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
    if (DataHelper.getTemp(
            Keys.LAST_MESSAGE, player.getGameProfile().getId(), player.getGameProfile().getId())
        != null) {
      for (EntityPlayer entity : server.getPlayerList().getPlayers()) {
        if (entity
            .getGameProfile()
            .getId()
            .equals(
                DataHelper.getTemp(
                    Keys.LAST_MESSAGE,
                    player.getGameProfile().getId(),
                    player.getGameProfile().getId()))) {
          FMLCommonHandler.instance()
              .getMinecraftServerInstance()
              .getCommandManager()
              .executeCommand(
                  sender, "/msg " + entity.getDisplayNameString() + " " + Strings.join(args, " "));
        }
      }
    } else {
      player.sendMessage(new TextComponentString(getUsage(sender)));
    }
  }
}
