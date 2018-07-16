package com.wurmcraft.serveressentials.common.general.commands.info;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.SECommand;
import com.wurmcraft.serveressentials.common.chat.ChatHelper;
import com.wurmcraft.serveressentials.common.utils.UsernameResolver;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

@Command(moduleName = "General")
public class UUIDCommand extends SECommand {

  @Override
  public String getName() {
    return "uuid";
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "\u00A79/uuid \u00A7b<player>";
  }

  @Override
  public List<String> getAltNames() {
    List<String> alts = new ArrayList<>();
    alts.add("id");
    return alts;
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    super.execute(server, sender, args);
    if (args.length == 0 && sender.getCommandSenderEntity() instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
      ChatHelper.sendMessage(
          sender,
          getCurrentLanguage(sender)
              .UUID
              .replaceAll("%UUID%", player.getGameProfile().getId().toString()));
    } else if (args.length == 1) {
      EntityPlayer player = UsernameResolver.getPlayer(args[0]);
      if (player != null) {
        ChatHelper.sendMessage(
            sender,
            getCurrentLanguage(sender)
                .UUID
                .replaceAll("%UUID%", player.getGameProfile().getId().toString()));
      } else {
        ChatHelper.sendMessage(
            sender, getCurrentLanguage(sender).PLAYER_NOT_FOUND.replaceAll("%PLAYER%", args[0]));
      }
    } else {
      ChatHelper.sendMessage(sender, getUsage(sender));
    }
  }
}
