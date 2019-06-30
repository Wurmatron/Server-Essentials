package com.wurmcraft.serveressentials.common.modules.teleport.command;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.api.lang.Lang;
import com.wurmcraft.serveressentials.api.storage.json.LocationWithName;
import com.wurmcraft.serveressentials.common.modules.language.ChatHelper;
import com.wurmcraft.serveressentials.common.reference.Replacment;
import com.wurmcraft.serveressentials.common.reference.Storage;
import com.wurmcraft.serveressentials.common.storage.file.DataHelper;
import com.wurmcraft.serveressentials.common.utils.command.CommandUtils;
import com.wurmcraft.serveressentials.common.utils.user.TeleportUtils;
import java.util.List;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

@ModuleCommand(moduleName = "Teleportation")
public class WarpCommand extends Command {

  @Override
  public String getName() {
    return "Warp";
  }

  @Override
  public String getUsage(Lang lang) {
    return "/warp <name>";
  }

  @Override
  public String getDescription(Lang lang) {
    return lang.local.DESCRIPTION_WARP;
  }

  @Override
  public void execute(
      MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    if (args.length == 1) {
      if (args[0].equalsIgnoreCase("list")) {
        ChatHelper.sendMessage(sender, senderLang.local.CHAT_SPACER);
        ChatHelper.sendMessage(sender, senderLang.local.TELEPORT_WARP_LIST);
        for (LocationWithName warp : DataHelper.getData(Storage.WARP, new LocationWithName())) {
          ChatHelper.sendMessage(sender, warp.name);
        }
        ChatHelper.sendMessage(sender, senderLang.local.CHAT_SPACER);
      } else {
        LocationWithName warp =
            (LocationWithName) DataHelper.get(Storage.WARP, args[0].toLowerCase());
        if (warp != null) {
          TeleportUtils.teleportTo(
              (EntityPlayerMP) sender.getCommandSenderEntity(), warp, true, false);
          ChatHelper.sendMessage(
              sender, senderLang.local.TELEPORT_WARP.replaceAll(Replacment.WARP, warp.name));
        } else {
          ChatHelper.sendMessage(
              sender,
              senderLang.local.TELEPORT_WARP_NOT_FOUND.replaceAll(Replacment.WARP, args[0]));
        }
      }
    } else {
      ChatHelper.sendMessage(sender, getUsage(senderLang));
    }
  }

  @Override
  public List<String> getAutoCompletion(
      MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
    return CommandUtils.predictWarp(args, 0);
  }
}
