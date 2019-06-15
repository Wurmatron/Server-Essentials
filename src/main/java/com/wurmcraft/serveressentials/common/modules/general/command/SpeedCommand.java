package com.wurmcraft.serveressentials.common.modules.general.command;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.api.command.SubCommand;
import com.wurmcraft.serveressentials.api.lang.Lang;
import com.wurmcraft.serveressentials.common.modules.language.ChatHelper;
import com.wurmcraft.serveressentials.common.modules.language.LanguageModule;
import com.wurmcraft.serveressentials.common.reference.Replacment;
import com.wurmcraft.serveressentials.common.utils.command.CommandUtils;
import java.util.*;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

@ModuleCommand(moduleName = "General")
public class SpeedCommand extends Command {

  private static final List<String> emptyAutoCompletion = Arrays.asList("walk", "fly");

  @Override
  public String getName() {
    return "Speed";
  }

  @Override
  public String getUsage(Lang lang) {
    return "/Speed <fly | walk> <user> <speed>";
  }

  @Override
  public String getDescription(Lang lang) {
    return lang.local.DESCRIPTION_SPEED;
  }

  @Override
  public void execute(
      MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {}

  @SubCommand
  public void fly(MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    if (args.length == 1) {
    } else if (args.length == 2) {

    } else {
      ChatHelper.sendMessage(sender, getUsage(senderLang));
    }
  }

  @SubCommand
  public void walk(MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    if (args.length == 1) {
      try {
        setSpeed(
            (EntityPlayer) sender.getCommandSenderEntity(), "walk", Double.parseDouble(args[0]));
        ChatHelper.sendMessage(
            sender, senderLang.local.GENERAL_SPEED.replaceAll(Replacment.NUMBER, args[0]));
      } catch (NumberFormatException e) {
        ChatHelper.sendMessage(sender, senderLang.local.CHAT_INVALID_NUMBER);
      }
    } else if (args.length == 2) {
      EntityPlayer player = CommandUtils.getPlayerForName(args[0]);
      if (player != null) {
        try {
          setSpeed(player, "walk", Double.parseDouble(args[0]));
          ChatHelper.sendMessage(
              player,
              LanguageModule.getUserLanguage(player)
                  .local
                  .GENERAL_SPEED
                  .replaceAll(Replacment.NUMBER, args[0]));
          ChatHelper.sendMessage(
              sender,
              senderLang
                  .local
                  .GENERAL_SPEED_OTHER
                  .replaceAll(Replacment.PLAYER, args[0])
                  .replaceAll(Replacment.NUMBER, args[1]));
        } catch (NumberFormatException e) {
          ChatHelper.sendMessage(sender, senderLang.local.CHAT_INVALID_NUMBER);
        }
      } else {
        ChatHelper.sendMessage(
            sender, senderLang.local.PLAYER_NOT_FOUND.replaceAll(Replacment.PLAYER, args[0]));
      }
    } else {
      ChatHelper.sendMessage(sender, getUsage(senderLang));
    }
  }

  public static void setSpeed(EntityPlayer player, String type, double speed) {
    NBTTagCompound tagCompound = new NBTTagCompound();
    player.capabilities.writeCapabilitiesToNBT(tagCompound);
    tagCompound
        .getCompoundTag("abilities")
        .setTag(type.toLowerCase() + "Speed", new NBTTagFloat((float) speed / 10));
    player.capabilities.readCapabilitiesFromNBT(tagCompound);
    player.sendPlayerAbilities();
  }

  @Override
  public List<String> getAutoCompletion(
      MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
    if (args.length == 0) {
      return emptyAutoCompletion;
    }
    return CommandUtils.predictUsernames(args, 1);
  }
}
