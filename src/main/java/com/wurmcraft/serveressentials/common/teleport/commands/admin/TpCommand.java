package com.wurmcraft.serveressentials.common.teleport.commands.admin;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.SECommand;
import com.wurmcraft.serveressentials.api.json.user.LocationWrapper;
import com.wurmcraft.serveressentials.common.language.LanguageModule;
import com.wurmcraft.serveressentials.common.teleport.utils.TeleportUtils;
import com.wurmcraft.serveressentials.common.utils.UsernameResolver;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

// TODO Rework Command
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
        TeleportUtils.teleportTo(
            (EntityPlayerMP) player,
            new LocationWrapper(new BlockPos(p.posX, p.posY, p.posZ), p.dimension),
            false);
        sender.sendMessage(
            new TextComponentString(
                LanguageModule.getLangfromUUID(player.getGameProfile().getId())
                    .TP_HOME
                    .replaceAll("%HOME%", p.getDisplayNameString())
                    .replaceAll("&", "\u00A7")));
      } else {
        sender.sendMessage(new TextComponentString(getCurrentLanguage(sender).PLAYER_ONLY));
      }
    } else if (args.length == 2) {
      EntityPlayer from = UsernameResolver.getPlayer(args[0]);
      EntityPlayer to = UsernameResolver.getPlayer(args[1]);
      if (from != null && to != null) {
        TeleportUtils.teleportTo(
            (EntityPlayerMP) from,
            new LocationWrapper(new BlockPos(to.posX, to.posY, to.posZ), to.dimension),
            false);
        sender.sendMessage(
            new TextComponentString(
                getCurrentLanguage(sender)
                    .TP_HOME_OTHER
                    .replaceAll("%FROM%", from.getDisplayNameString())
                    .replaceAll("%TO%", to.getDisplayNameString())
                    .replaceAll("%HOME%", to.getDisplayNameString())
                    .replaceAll("&", "\u00A7")));
        from.sendMessage(
            new TextComponentString(
                LanguageModule.getLangfromUUID(from.getGameProfile().getId())
                    .TP_HOME
                    .replaceAll("%HOME%", to.getDisplayNameString())
                    .replaceAll("&", "\u00A7")));
      } else {
        sender.sendMessage(new TextComponentString(getCurrentLanguage(sender).PLAYER_ONLY));
      }
    } else if (args.length == 3 && sender.getCommandSenderEntity() instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
      try {
        int x = Integer.parseInt(args[0]);
        int y = Integer.parseInt(args[1]);
        int z = Integer.parseInt(args[2]);
        if (x != -1 && y != -1 & z != -1) {
          TeleportUtils.teleportTo(
              (EntityPlayerMP) player,
              new LocationWrapper(new BlockPos(x, y, z), player.dimension),
              false);
          sender.sendMessage(
              new TextComponentString(
                  LanguageModule.getLangfromUUID(player.getGameProfile().getId())
                      .TP_HOME
                      .replaceAll("%HOME%", "[" + x + ", " + y + ", " + z + "]")
                      .replaceAll("&", "\u00A7")));
        }
      } catch (NumberFormatException e) {
      }
    } else if (args.length == 4) {
      EntityPlayer player = UsernameResolver.getPlayer(args[0]);
      if (player != null) {
        try {
          int x = Integer.parseInt(args[0]);
          int y = Integer.parseInt(args[1]);
          int z = Integer.parseInt(args[2]);
          if (x != -1 && y != -1 & z != -1) {
            TeleportUtils.teleportTo(
                (EntityPlayerMP) player,
                new LocationWrapper(new BlockPos(x, y, z), player.dimension),
                false);
            sender.sendMessage(
                new TextComponentString(
                    LanguageModule.getLangfromUUID(player.getGameProfile().getId())
                        .TP_HOME
                        .replaceAll("%HOME%", "[" + x + ", " + y + ", " + z + "]")
                        .replaceAll("&", "\u00A7")));
          }
        } catch (NumberFormatException e) {
        }
      }
    } else {
      sender.sendMessage(new TextComponentString(getUsage(sender)));
    }
  }

  @Override
  public boolean canConsoleRun() {
    return true;
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "\u00A79/tp \u00A7b<to|x> <from|y> <z> <dimension>";
  }

  @Override
  public String getDescription(ICommandSender sender) {
    return getCurrentLanguage(sender).COMMAND_TP.replaceAll("&", "\u00A7");
  }

  @Override
  public List<String> getTabCompletions(
      MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
    return autoCompleteUsername(args, 0);
  }
}
