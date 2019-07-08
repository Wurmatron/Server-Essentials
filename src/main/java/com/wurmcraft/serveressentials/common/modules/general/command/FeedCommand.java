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
public class FeedCommand extends Command {

  @Override
  public String getName() {
    return "Feed";
  }

  @Override
  public String getUsage(Lang lang) {
    return "/feed <user>";
  }

  @Override
  public String getDescription(Lang lang) {
    return lang.local.DESCRIPTION_FEED;
  }

  @Override
  public void execute(
      MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    if (args.length == 0 && sender.getCommandSenderEntity() instanceof EntityPlayer) {
      (((EntityPlayer) sender.getCommandSenderEntity())).getFoodStats().setFoodLevel(20);
      ChatHelper.sendMessage(sender, senderLang.local.GENERAL_FEED);
    } else if (args.length == 1) {
      EntityPlayer player = CommandUtils.getPlayerForName(args[0]);
      if (player != null) {
        player.getFoodStats().setFoodLevel(20);
        ChatHelper.sendMessage(player, LanguageModule.getUserLanguage(player).local.GENERAL_FEED);
        ChatHelper.sendMessage(
            sender,
            senderLang.local.GENERAL_FEED_OTHER.replaceAll(
                Replacment.PLAYER, player.getDisplayNameString()));
      } else {
        ChatHelper.sendMessage(
            sender, senderLang.local.PLAYER_NOT_FOUND.replaceAll(Replacment.PLAYER, args[0]));
      }
    } else {
      ChatHelper.sendMessage(sender, getUsage(senderLang));
    }
  }

  @Override
  public boolean canConsoleRun() {
    return true;
  }

  @Override
  public List<String> getAliases(List<String> aliases) {
    aliases.add("Food");
    return aliases;
  }

  @Override
  public List<String> getAutoCompletion(
      MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
    return CommandUtils.predictUsernames(args, 0);
  }
}
