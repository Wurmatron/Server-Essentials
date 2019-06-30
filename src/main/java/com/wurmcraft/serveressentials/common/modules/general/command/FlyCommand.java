package com.wurmcraft.serveressentials.common.modules.general.command;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.api.lang.Lang;
import com.wurmcraft.serveressentials.common.modules.language.ChatHelper;
import com.wurmcraft.serveressentials.common.modules.language.LanguageModule;
import com.wurmcraft.serveressentials.common.reference.Replacment;
import com.wurmcraft.serveressentials.common.utils.command.CommandUtils;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

@ModuleCommand(moduleName = "General")
public class FlyCommand extends Command {

  @Override
  public String getName() {
    return "Fly";
  }

  @Override
  public String getUsage(Lang lang) {
    return "/fly <user>";
  }

  @Override
  public String getDescription(Lang lang) {
    return lang.local.DESCRIPTION_FLY;
  }

  @Override
  public void execute(
      MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    if (args.length == 0 && sender instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
      boolean fly = !player.capabilities.allowFlying;
      player.capabilities.allowFlying = fly;
      if (fly || player.isCreative()) { // Avoid messing up creative players
        player.capabilities.isFlying = true;
      }
      if (fly) {
        ChatHelper.sendMessage(
            player, LanguageModule.getUserLanguage(player).local.GENERAL_FLY_ENABLED);
      } else {
        ChatHelper.sendMessage(
            player, LanguageModule.getUserLanguage(player).local.GENERAL_FLY_DISABLED);
      }
      player.sendPlayerAbilities();
    } else if (args.length == 1) {
      EntityPlayer player = CommandUtils.getPlayerForName(args[0]);
      if (player != null) {
        boolean fly = player.capabilities.allowFlying;
        player.capabilities.allowFlying = !fly;
        if (fly) {
          ChatHelper.sendMessage(
              sender,
              senderLang.local.GENERAL_FLY_OTHER_ENABLED.replaceAll(
                  Replacment.PLAYER, player.getDisplayNameString()));
          ChatHelper.sendMessage(
              player, LanguageModule.getUserLanguage(player).local.GENERAL_FLY_ENABLED);
        } else {
          ChatHelper.sendMessage(
              sender,
              senderLang.local.GENERAL_FLY_OTHER_DISABLED.replaceAll(
                  Replacment.PLAYER, player.getDisplayNameString()));
          ChatHelper.sendMessage(
              player, LanguageModule.getUserLanguage(player).local.GENERAL_FLY_DISABLED);
        }
        player.sendPlayerAbilities();
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
}
