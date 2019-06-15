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
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

@ModuleCommand(moduleName = "General")
public class SmiteCommand extends Command {

  @Override
  public String getName() {
    return "Smite";
  }

  @Override
  public String getUsage(Lang lang) {
    return "/smite <user>";
  }

  @Override
  public String getDescription(Lang lang) {
    return lang.local.DESCRIPTION_SMITE;
  }

  @Override
  public void execute(
      MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    if (args.length == 0) {
      sender
          .getEntityWorld()
          .addWeatherEffect(
              new EntityLightningBolt(
                  sender.getEntityWorld(),
                  sender.getPosition().getX(),
                  sender.getPosition().getY(),
                  sender.getPosition().getZ(),
                  false));
    } else if (args.length == 1) {
      EntityPlayer player = CommandUtils.getPlayerForName(args[0]);
      if (player != null) {
        player
            .getEntityWorld()
            .addWeatherEffect(
                new EntityLightningBolt(
                    player.getEntityWorld(),
                    player.getPosition().getX(),
                    player.getPosition().getY(),
                    player.getPosition().getZ(),
                    false));
        ChatHelper.sendMessage(
            sender, senderLang.local.GENERAL_SMITE_OTHER.replaceAll(Replacment.PLAYER, args[0]));
        ChatHelper.sendMessage(player, LanguageModule.getUserLanguage(player).local.GENERAL_SMITE);
      } else {
        ChatHelper.sendMessage(
            sender, senderLang.local.PLAYER_NOT_FOUND.replaceAll(Replacment.PLAYER, args[0]));
      }
    } else {
      ChatHelper.sendMessage(sender, getUsage(senderLang));
    }
  }

  @Override
  public List<String> getAutoCompletion(
      MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
    return CommandUtils.predictUsernames(args, 0);
  }
}
