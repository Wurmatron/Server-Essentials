package com.wurmcraft.serveressentials.common.modules.language.command;

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

@ModuleCommand(moduleName = "Language")
public class NickCommand extends Command {

  @Override
  public String getName() {
    return "Nick";
  }

  @Override
  public String getUsage(Lang lang) {
    return "/nick <user> <nick>";
  }

  @Override
  public String getDescription(Lang lang) {
    return lang.local.DESCRIPTION_NICK;
  }

  @Override
  public void execute(
      MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    if (args.length == 2) {
      EntityPlayer player = CommandUtils.getPlayerForName(args[0]);
      if (player != null) {
        UserManager.setNickName(player, args[1]);
        ChatHelper.sendMessage(
            player, senderLang.local.LANG_NICK_CHANGED.replaceAll(Replacment.NICK, args[0]));
        ChatHelper.sendMessage(
            sender,
            senderLang
                .local
                .LANG_NICK_CHANGED_SENDER
                .replaceAll(Replacment.NICK, args[0])
                .replaceAll(Replacment.PLAYER, player.getDisplayNameString()));
      } else {
        ChatHelper.sendMessage(
            sender, senderLang.local.PLAYER_NOT_FOUND.replaceAll(Replacment.PLAYER, args[0]));
      }
    } else if (args.length == 1) {
      if (sender.getCommandSenderEntity() instanceof EntityPlayer) {
        UserManager.setNickName((EntityPlayer) sender.getCommandSenderEntity(), args[0]);
        ChatHelper.sendMessage(
            sender, senderLang.local.LANG_NICK_CHANGED.replaceAll(Replacment.NICK, args[0]));
      } else {
        ChatHelper.sendMessage(
            sender, senderLang.local.PLAYER_NOT_FOUND.replaceAll(Replacment.PLAYER, "Console"));
      }
    } else {
      ChatHelper.sendMessage(sender, getUsage(senderLang));
    }
  }

  @Override
  public List<String> getAliases(List<String> aliases) {
    aliases.add("n");
    aliases.add("Name");
    return aliases;
  }

  @Override
  public boolean canConsoleRun() {
    return true;
  }

  @Override
  public List<String> getAutoCompletion(
      MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
    return CommandUtils.predictUsernames(args, 0);
  }
}
