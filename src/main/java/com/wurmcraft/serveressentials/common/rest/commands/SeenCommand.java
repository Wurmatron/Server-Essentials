package com.wurmcraft.serveressentials.common.rest.commands;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.SECommand;
import com.wurmcraft.serveressentials.api.json.user.restOnly.GlobalUser;
import com.wurmcraft.serveressentials.common.utils.UsernameResolver;
import java.util.Date;
import java.util.UUID;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

@Command(moduleName = "Rest")
public class SeenCommand extends SECommand {

  @Override
  public String getName() {
    return "seen";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    super.execute(server, sender, args);
    if (args.length == 1) {
      UUID player = UsernameResolver.getUUIDFromName(args[0]);
      if (player != null) {
        GlobalUser global = forceUserFromUUID(player);
        sender.sendMessage(
            new TextComponentString(
                getCurrentLanguage(sender).CHAT_LASTSEEN
                    + ": "
                    + TextFormatting.AQUA
                    + new Date(global.getLastSeen()).toString()));
      } else {
        sender.sendMessage(
            new TextComponentString(
                getCurrentLanguage(sender).PLAYER_NOT_FOUND.replaceAll("%PLAYER%", args[0])));
      }
    } else {
      sender.sendMessage(new TextComponentString(getUsage(sender)));
    }
  }
}
