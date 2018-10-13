package com.wurmcraft.serveressentials.common.teleport.commands.admin;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.SECommand;
import com.wurmcraft.serveressentials.api.json.user.LocationWrapper;
import com.wurmcraft.serveressentials.common.chat.ChatHelper;
import com.wurmcraft.serveressentials.common.language.LanguageModule;
import com.wurmcraft.serveressentials.common.teleport.utils.TeleportUtils;
import com.wurmcraft.serveressentials.common.utils.UsernameResolver;
import java.util.List;
import javax.annotation.Nullable;
import joptsimple.internal.Strings;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

@Command(moduleName = "Teleportation")
public class TpCommand extends SECommand {

  @Override
  public String getName() {
    return "tp";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    super.execute(server, sender, args);
    if (args.length == 1) {
      EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
      if (player != null) {
        EntityPlayer p = UsernameResolver.getPlayer(args[0]);
        teleportToPlayer(player, p, 1);
      } else {
        ChatHelper.sendMessage(sender, getCurrentLanguage(sender).PLAYER_ONLY);
      }
    } else if (args.length == 2) {
      EntityPlayer from = UsernameResolver.getPlayer(args[0]);
      EntityPlayer to = UsernameResolver.getPlayer(args[1]);
      if (from != null && to != null) {
        teleportToPlayer(from, to, 2);
      } else {
        ChatHelper.sendMessage(sender, getCurrentLanguage(sender).PLAYER_ONLY);
      }
    } else if (args.length == 3 && sender.getCommandSenderEntity() instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
      try {
        teleportToCords(
            player,
            new LocationWrapper(
                Integer.parseInt(args[0]),
                Integer.parseInt(args[1]),
                Integer.parseInt(args[2]),
                player.dimension),
            1);
      } catch (NumberFormatException e) {
        ChatHelper.sendMessage(
            sender,
            getCurrentLanguage(sender)
                .INVALID_NUMBER
                .replaceAll("%NUMBER%", Strings.join(args, " ")));
      }
    } else if (args.length == 4) {
      EntityPlayer player = UsernameResolver.getPlayer(args[0]);
      try {
        teleportToCords(
            player,
            new LocationWrapper(
                Integer.parseInt(args[1]),
                Integer.parseInt(args[2]),
                Integer.parseInt(args[3]),
                player.dimension),
            1);
      } catch (NumberFormatException e) {
        ChatHelper.sendMessage(
            sender,
            getCurrentLanguage(sender)
                .INVALID_NUMBER
                .replaceAll("%NUMBER%", Strings.join(args, " ")));
      }
    } else if (args.length == 5) {
      EntityPlayer player = UsernameResolver.getPlayer(args[0]);
      try {
        teleportToCords(
            player,
            new LocationWrapper(
                Integer.parseInt(args[1]),
                Integer.parseInt(args[2]),
                Integer.parseInt(args[3]),
                Integer.parseInt(args[4])),
            1);
      } catch (NumberFormatException e) {
        ChatHelper.sendMessage(
            sender,
            getCurrentLanguage(sender)
                .INVALID_NUMBER
                .replaceAll("%NUMBER%", Strings.join(args, " ")));
      }
    } else {
      ChatHelper.sendMessage(sender, getUsage(sender));
    }
  }

  private void teleportToPlayer(EntityPlayer from, EntityPlayer to, int displayChat) {
    if (TeleportUtils.teleportTo(from, to)) {
      if (displayChat >= 1) {
        ChatHelper.sendMessage(
            from,
            LanguageModule.getLangfromUUID(from.getGameProfile().getId())
                .TP_HOME
                .replaceAll("%HOME%", to.getDisplayNameString())
                .replaceAll("&", FORMATTING_CODE));
      } else if (displayChat >= 2) {
        ChatHelper.sendMessage(
            from,
            LanguageModule.getLangfromUUID(from.getGameProfile().getId())
                .TP_HOME_OTHER
                .replaceAll("%FROM%", from.getDisplayNameString())
                .replaceAll("%TO%", to.getDisplayNameString())
                .replaceAll("%HOME%", to.getDisplayNameString())
                .replaceAll("&", FORMATTING_CODE));
      }
    }
  }

  private void teleportToCords(EntityPlayer player, LocationWrapper cords, int displayChat) {
    if (TeleportUtils.teleportTo((EntityPlayerMP) player, cords, true) && displayChat >= 1) {
      ChatHelper.sendMessage(
          player,
          LanguageModule.getLangfromUUID(player.getGameProfile().getId())
              .TP_HOME
              .replaceAll(
                  "%HOME%", "[" + cords.getX() + ", " + cords.getY() + ", " + cords.getZ() + "]")
              .replaceAll("&", FORMATTING_CODE));
    }
  }

  @Override
  public boolean canConsoleRun() {
    return true;
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return DEFAULT_COLOR
        + "/tp "
        + DEFAULT_USAGE_COLOR
        + "<toPlayer | fromPlayer|  x> <toPlayer | x |  y>  <z> <dim>";
  }

  @Override
  public String getDescription(ICommandSender sender) {
    return getCurrentLanguage(sender).COMMAND_TP.replaceAll("&", FORMATTING_CODE);
  }

  @Override
  public List<String> getTabCompletions(
      MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
    if (args.length == 1) {
      return autoCompleteUsername(args, 0);
    } else if (args.length == 2) {
      return autoCompleteUsername(args, 1);
    }
    return super.getTabCompletions(server, sender, args, pos);
  }
}
