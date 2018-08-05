package com.wurmcraft.serveressentials.common.teleport.commands.admin;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.SECommand;
import com.wurmcraft.serveressentials.common.chat.ChatHelper;
import com.wurmcraft.serveressentials.common.language.LanguageModule;
import com.wurmcraft.serveressentials.common.teleport.utils.TeleportUtils;
import com.wurmcraft.serveressentials.common.utils.UsernameResolver;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

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
      ChatHelper.sendMessage(
          sender,
          getCurrentLanguage(sender)
              .TP_HERE
              .replaceAll("%PLAYER%", herePlayer.getDisplayNameString())
              .replaceAll("&", "\u00A7"));
      ChatHelper.sendMessage(
          herePlayer,
          LanguageModule.getLangfromUUID(herePlayer.getGameProfile().getId())
              .TP
              .replaceAll("%NAME%", player.getDisplayNameString())
              .replaceAll("&", "\u00A7"));
    } else {
      ChatHelper.sendMessage(sender, getUsage(sender));
    }
  }

  @Override
  public boolean canConsoleRun() {
    return false;
  }

  @Override
  public String getDescription(ICommandSender sender) {
    return getCurrentLanguage(sender).COMMAND_TPHERE.replaceAll("&", "\u00A7");
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "\u00A79/tphere \u00A7b<player>";
  }

  @Override
  public List<String> getTabCompletions(
      MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
    return autoCompleteUsername(args, 0);
  }
}
