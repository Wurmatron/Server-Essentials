package com.wurmcraft.serveressentials.common.modules.general.command;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.api.lang.Lang;
import com.wurmcraft.serveressentials.common.modules.language.ChatHelper;
import com.wurmcraft.serveressentials.common.modules.language.LanguageModule;
import com.wurmcraft.serveressentials.common.reference.Replacment;
import com.wurmcraft.serveressentials.common.utils.command.CommandUtils;
import java.util.List;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

@ModuleCommand(moduleName = "General")
public class GodCommand extends Command {

  @Override
  public String getName() {
    return "God";
  }

  @Override
  public String getUsage(Lang lang) {
    return "/god <user>";
  }

  @Override
  public String getDescription(Lang lang) {
    return lang.local.DESCRIPTION_GOD;
  }

  @Override
  public void execute(
      MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    if (args.length == 1 && sender.getCommandSenderEntity() instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
      if (player.capabilities.disableDamage) {
        player.capabilities.disableDamage = false;
        ChatHelper.sendMessage(sender, senderLang.local.GENERAL_GOD_ENABLED);
      } else {
        player.capabilities.disableDamage = true;
        ChatHelper.sendMessage(sender, senderLang.local.GENERAL_GOD_DISABLED);
      }
    } else if (args.length == 2) {
      EntityPlayer player = CommandUtils.getPlayerForName(args[0]);
      if (player != null) {
        if (player.capabilities.disableDamage) {
          player.capabilities.disableDamage = false;
          ChatHelper.sendMessage(sender, senderLang.local.GENERAL_GOD_ENABLED_OTHER);
          ChatHelper.sendMessage(
              player, LanguageModule.getUserLanguage(player).local.GENERAL_GOD_ENABLED);
        } else {
          player.capabilities.disableDamage = true;
          ChatHelper.sendMessage(sender, senderLang.local.GENERAL_GOD_DISABLED_OTHER);
          ChatHelper.sendMessage(
              player, LanguageModule.getUserLanguage(player).local.GENERAL_GOD_DISABLED);
        }
      } else {
        ChatHelper.sendMessage(
            sender, senderLang.local.PLAYER_NOT_FOUND.replaceAll(Replacment.PLAYER, args[0]));
      }
    } else {
      ChatHelper.sendMessage(sender, getUsage(senderLang));
    }
  }

  @Override
  public List<String> getAliases(List<String> aliases) {
    aliases.add("G");
    return aliases;
  }

  @Override
  public List<String> getAutoCompletion(
      MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
    return CommandUtils.predictUsernames(args, 0);
  }

  @Override
  public boolean canConsoleRun() {
    return true;
  }
}
