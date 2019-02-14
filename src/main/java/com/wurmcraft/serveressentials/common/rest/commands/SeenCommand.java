package com.wurmcraft.serveressentials.common.rest.commands;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.json.user.rest.GlobalUser;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.chat.ChatHelper;
import com.wurmcraft.serveressentials.common.utils.SECommand;
import com.wurmcraft.serveressentials.common.utils.UsernameResolver;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
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
        ChatHelper.sendMessage(
            sender,
            getCurrentLanguage(sender).CHAT_LASTSEEN
                + ": "
                + TextFormatting.AQUA
                + new Date(global.getServerData(ConfigHandler.serverName).getLastSeen())
                    .toString()
                    .replaceAll("&", FORMATTING_CODE));
      } else {
        ChatHelper.sendMessage(
            sender,
            getCurrentLanguage(sender)
                .PLAYER_NOT_FOUND
                .replaceAll("%PLAYER%", args[0])
                .replaceAll("&", FORMATTING_CODE));
      }
    } else {
      ChatHelper.sendMessage(sender, getUsage(sender));
    }
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "\u00Abseen \u00A79<username>";
  }

  @Override
  public String getDescription(ICommandSender sender) {
    return getCurrentLanguage(sender).COMMAND_SEEN.replaceAll("&", FORMATTING_CODE);
  }

  @Override
  public List<String> getTabCompletions(
      MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
    return autoCompleteUsername(args, 0);
  }
}
