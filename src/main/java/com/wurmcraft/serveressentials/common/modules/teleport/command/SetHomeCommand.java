package com.wurmcraft.serveressentials.common.modules.teleport.command;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.api.lang.Lang;
import com.wurmcraft.serveressentials.api.storage.LocationWrapper;
import com.wurmcraft.serveressentials.api.user.storage.Home;
import com.wurmcraft.serveressentials.common.modules.language.ChatHelper;
import com.wurmcraft.serveressentials.common.reference.Replacment;
import com.wurmcraft.serveressentials.common.utils.user.UserManager;
import java.util.List;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

@ModuleCommand(moduleName = "Teleportation")
public class SetHomeCommand extends Command {

  public static final String DEFAULT_HOME = "home";

  @Override
  public String getName() {
    return "sethome";
  }

  @Override
  public String getUsage(Lang lang) {
    return "/setHome <name>";
  }

  @Override
  public String getDescription(Lang lang) {
    return lang.local.DESCRIPTION_SETHOME;
  }

  @Override
  public void execute(
      MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    if (args.length == 0) {
      EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
      Home home =
          new Home(
              DEFAULT_HOME,
              new LocationWrapper(sender.getPosition(), sender.getCommandSenderEntity().dimension));
      if (UserManager.setHome(player, home)) {
        ChatHelper.sendMessage(
            sender, senderLang.local.TELEPORT_HOME_SET.replaceAll(Replacment.HOME, home.getName()));
      } else {
        ChatHelper.sendMessage(sender, senderLang.local.TELEPORT_HOME_MAX);
      }
    } else if (args.length == 1) {
      EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
      Home home =
          new Home(
              args[0],
              new LocationWrapper(sender.getPosition(), sender.getCommandSenderEntity().dimension));
      if (UserManager.setHome(player, home)) {
        ChatHelper.sendMessage(
            sender, senderLang.local.TELEPORT_HOME_SET.replaceAll(Replacment.HOME, home.getName()));
      }
    } else ChatHelper.sendMessage(sender, getUsage(senderLang));
  }

  @Override
  public List<String> getAliases(List<String> aliases) {
    aliases.add("setH");
    return aliases;
  }
}
