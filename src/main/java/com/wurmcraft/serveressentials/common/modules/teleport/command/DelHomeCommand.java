package com.wurmcraft.serveressentials.common.modules.teleport.command;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.api.lang.Lang;
import com.wurmcraft.serveressentials.common.modules.language.ChatHelper;
import com.wurmcraft.serveressentials.common.reference.Replacment;
import com.wurmcraft.serveressentials.common.utils.command.CommandUtils;
import com.wurmcraft.serveressentials.common.utils.user.UserManager;
import java.util.List;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

@ModuleCommand(moduleName = "Teleportation")
public class DelHomeCommand extends Command {

  @Override
  public String getName() {
    return "delHome";
  }

  @Override
  public String getUsage(Lang lang) {
    return "/delHome <name>";
  }

  @Override
  public String getDescription(Lang lang) {
    return lang.local.DESCRIPTION_DELHOME;
  }

  @Override
  public void execute(
      MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    if (args.length == 0) {
      EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
      UserManager.delHome(player, UserManager.getHome(player, SetHomeCommand.DEFAULT_HOME));
      ChatHelper.sendMessage(
          sender,
          senderLang.local.TELEPORT_DELHOME.replaceAll(
              Replacment.HOME, SetHomeCommand.DEFAULT_HOME));
    } else if (args.length == 1) {
      EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
      UserManager.delHome(player, UserManager.getHome(player, args[0]));
      ChatHelper.sendMessage(
          sender, senderLang.local.TELEPORT_DELHOME.replaceAll(Replacment.HOME, args[0]));
    } else {
      ChatHelper.sendMessage(sender, getUsage(senderLang));
    }
  }

  @Override
  public List<String> getAutoCompletion(
      MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
    return CommandUtils.predictHome((EntityPlayer) sender.getCommandSenderEntity(), args, 0);
  }

  @Override
  public List<String> getAliases(List<String> aliases) {
    aliases.add("delH");
    aliases.add("deleteHome");
    aliases.add("deleteH");
    aliases.add("remHome");
    aliases.add("remH");
    aliases.add("removeHome");
    aliases.add("removeH");
    return aliases;
  }
}
