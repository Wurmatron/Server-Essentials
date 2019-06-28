package com.wurmcraft.serveressentials.common.modules.teleport.command;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.api.lang.Lang;
import com.wurmcraft.serveressentials.api.storage.LocationWrapper;
import com.wurmcraft.serveressentials.common.modules.language.ChatHelper;
import com.wurmcraft.serveressentials.common.modules.language.LanguageModule;
import com.wurmcraft.serveressentials.common.reference.Replacment;
import com.wurmcraft.serveressentials.common.utils.command.CommandUtils;
import com.wurmcraft.serveressentials.common.utils.user.TeleportUtils;
import java.util.List;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

@ModuleCommand(moduleName = "Teleportation")
public class TpCommand extends Command {

  @Override
  public String getName() {
    return "Tp";
  }

  @Override
  public String getUsage(Lang lang) {
    return "/tp <user, x,y,z, dim> | <to, from>";
  }

  @Override
  public String getDescription(Lang lang) {
    return lang.local.DESCRIPTION_TP;
  }

  @Override
  public void execute(
      MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    if (args.length == 1 && sender.getCommandSenderEntity() instanceof EntityPlayer) {
      try {
        int dim = Integer.parseInt(args[0]);
        TeleportUtils.teleportTo(
            (EntityPlayerMP) sender.getCommandSenderEntity(),
            new LocationWrapper(sender.getPosition(), dim),
            false,
            false);
        ChatHelper.sendMessage(sender, senderLang.local.TELEPORT_FORCED);
      } catch (NumberFormatException e) {
        ChatHelper.sendMessage(
            sender, senderLang.local.CHAT_INVALID_NUMBER.replaceAll(Replacment.NUMBER, args[0]));
      }
    } else if (args.length == 3 || args.length == 4 || args.length == 5) {
      try {
        int x = Integer.parseInt(args[0]);
        int y = Integer.parseInt(args[1]);
        int z = Integer.parseInt(args[2]);
        int d =
            args.length > 3 ? Integer.parseInt(args[3]) : sender.getCommandSenderEntity().dimension;
        TeleportUtils.teleportTo(
            (EntityPlayerMP) sender.getCommandSenderEntity(),
            new LocationWrapper(x, y, z, d),
            false,
            false);
        ChatHelper.sendMessage(sender, senderLang.local.TELEPORT_FORCED);
      } catch (NumberFormatException e) {
        EntityPlayer player = CommandUtils.getPlayerForName(args[0]);
        int x = Integer.parseInt(args[1]);
        int y = Integer.parseInt(args[2]);
        int z = Integer.parseInt(args[3]);
        int d = args.length > 4 ? Integer.parseInt(args[4]) : player.dimension;
        TeleportUtils.teleportTo(
            (EntityPlayerMP) player, new LocationWrapper(x, y, z, d), false, false);
        ChatHelper.sendMessage(sender, senderLang.local.TELEPORT_FORCED);
      }
    } else if (args.length == 2) {
      EntityPlayer first = CommandUtils.getPlayerForName(args[0]);
      EntityPlayer second = CommandUtils.getPlayerForName(args[1]);
      TeleportUtils.teleportTo(
          (EntityPlayerMP) second,
          new LocationWrapper(first.getPosition(), first.dimension),
          false,
          false);
      ChatHelper.sendMessage(second, LanguageModule.getUserLanguage(second).local.TELEPORT_FORCED);
      ChatHelper.sendMessage(
          sender,
          senderLang
              .local
              .TELEPORT_OTHER
              .replaceAll(Replacment.PLAYER, second.getDisplayNameString())
              .replaceAll(Replacment.USER, first.getDisplayNameString()));
    }
  }

  @Override
  public List<String> getAutoCompletion(
      MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
    if (args.length == 0) {
      return CommandUtils.predictUsernames(args, 0);
    } else if (args.length == 1) {
      return CommandUtils.predictUsernames(args, 1);
    }
    return super.getAutoCompletion(server, sender, args, pos);
  }

  @Override
  public boolean canConsoleRun() {
    return true;
  }
}
