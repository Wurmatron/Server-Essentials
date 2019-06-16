package com.wurmcraft.serveressentials.common.modules.general.command;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.api.lang.Lang;
import com.wurmcraft.serveressentials.api.storage.LocationWrapper;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.modules.general.GeneralModule;
import com.wurmcraft.serveressentials.common.modules.language.ChatHelper;
import com.wurmcraft.serveressentials.common.storage.file.DataHelper;
import java.io.File;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

@ModuleCommand(moduleName = "General")
public class SetSpawnCommand extends Command {

  @Override
  public String getName() {
    return "setSpawn";
  }

  @Override
  public String getUsage(Lang lang) {
    return "/setSpawn";
  }

  @Override
  public String getDescription(Lang lang) {
    return lang.local.DESCRIPTION_SETSPAWN;
  }

  @Override
  public void execute(
      MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    if (args.length == 0) {
      GeneralModule.config.spawn =
          new LocationWrapper(sender.getPosition(), sender.getCommandSenderEntity().dimension);
      DataHelper.save(new File(ConfigHandler.saveLocation), GeneralModule.config);
      sender.getCommandSenderEntity().getEntityWorld().setSpawnPoint(sender.getPosition());
      ChatHelper.sendMessage(sender, senderLang.local.GENERAL_SPAWN_SET);
    } else {
      ChatHelper.sendMessage(sender, getUsage(senderLang));
    }
  }
}
