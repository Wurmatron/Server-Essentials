package com.wurmcraft.serveressentials.common.modules.teleport.command;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.api.lang.Lang;
import com.wurmcraft.serveressentials.api.user.storage.Home;
import com.wurmcraft.serveressentials.common.modules.language.ChatHelper;
import com.wurmcraft.serveressentials.common.reference.Replacment;
import com.wurmcraft.serveressentials.common.utils.command.CommandUtils;
import com.wurmcraft.serveressentials.common.utils.user.TeleportUtils;
import com.wurmcraft.serveressentials.common.utils.user.UserManager;
import java.util.List;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;

@ModuleCommand(moduleName = "Teleportation")
public class HomeCommand extends Command {

  @Override
  public String getName() {
    return "Home";
  }

  @Override
  public String getUsage(Lang lang) {
    return "/home <name>";
  }

  @Override
  public String getDescription(Lang lang) {
    return lang.local.DESCRIPTION_HOME;
  }

  @Override
  public void execute(
      MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
    if (args.length == 0) {
      Home home = UserManager.getHome(player, SetHomeCommand.DEFAULT_HOME);
      teleportHome(sender, senderLang, (EntityPlayerMP) player, home, SetHomeCommand.DEFAULT_HOME);
    } else if (args.length == 1) {
      if (args[0].equalsIgnoreCase("list")) {
        ChatHelper.sendMessage(sender, senderLang.local.CHAT_SPACER);
        for (Home home : UserManager.getHomes(player)) {
          ChatHelper.sendMessage(sender, TextFormatting.LIGHT_PURPLE + home.getName());
        }
        ChatHelper.sendMessage(sender, senderLang.local.CHAT_SPACER);
      } else {
        Home home = UserManager.getHome(player, args[0]);
        teleportHome(sender, senderLang, (EntityPlayerMP) player, home, args[0]);
      }
    } else {
      ChatHelper.sendMessage(sender, getUsage(senderLang));
    }
  }

  private void teleportHome(
      ICommandSender sender, Lang senderLang, EntityPlayerMP player, Home home, String homeName) {
    if (home != null) {
      if (TeleportUtils.teleportTo(player, home.getPos(), true, false)) {
        ChatHelper.sendMessage(
            sender, senderLang.local.TELEPORT_HOME.replaceAll(Replacment.HOME, home.getName()));
      }
    } else {
      ChatHelper.sendMessage(
          sender, senderLang.local.TELEPORT_HOME_NOT_FOUND.replaceAll(Replacment.HOME, homeName));
    }
  }

  @Override
  public List<String> getAutoCompletion(
      MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
    return CommandUtils.predictHome((EntityPlayer) sender.getCommandSenderEntity(), args, 0);
  }

  @Override
  public List<String> getAliases(List<String> aliases) {
    aliases.add("H");
    return aliases;
  }
}
