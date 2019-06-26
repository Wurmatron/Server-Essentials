package com.wurmcraft.serveressentials.common.modules.teleport.command;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.api.lang.Lang;
import com.wurmcraft.serveressentials.api.storage.json.LocationWithName;
import com.wurmcraft.serveressentials.common.modules.language.ChatHelper;
import com.wurmcraft.serveressentials.common.reference.Replacment;
import com.wurmcraft.serveressentials.common.reference.Storage;
import com.wurmcraft.serveressentials.common.storage.file.DataHelper;
import java.util.Arrays;
import java.util.List;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

@ModuleCommand(moduleName = "Teleportation")
public class SetWarpCommand extends Command {

  private static final List<String> BLACKLIST = Arrays.asList("list", "create");

  @Override
  public String getName() {
    return "SetWarp";
  }

  @Override
  public String getUsage(Lang lang) {
    return "/setWarp <name>";
  }

  @Override
  public String getDescription(Lang lang) {
    return lang.local.DESCRIPTION_SETWARP;
  }

  @Override
  public void execute(
      MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    LocationWithName warpLocation =
        new LocationWithName(
            args[0].toLowerCase(), sender.getPosition(), sender.getCommandSenderEntity().dimension);
    if (!BLACKLIST.contains(args[0].toLowerCase())) {
      DataHelper.save(Storage.WARP, warpLocation);
      ChatHelper.sendMessage(
          sender,
          senderLang.local.TELEPORT_SET_WARP.replaceAll(Replacment.WARP, warpLocation.name));
      DataHelper.load(Storage.WARP, warpLocation);
    }
  }
}
