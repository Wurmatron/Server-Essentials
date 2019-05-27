package com.wurmcraft.serveressentials.common.modules.language.command;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.api.command.SubCommand;
import com.wurmcraft.serveressentials.api.lang.Lang;
import com.wurmcraft.serveressentials.api.storage.json.Channel;
import com.wurmcraft.serveressentials.common.modules.language.ChatHelper;
import com.wurmcraft.serveressentials.common.modules.language.LanguageModule;
import com.wurmcraft.serveressentials.common.reference.Replacment;
import com.wurmcraft.serveressentials.common.reference.Storage;
import com.wurmcraft.serveressentials.common.storage.file.DataHelper;
import com.wurmcraft.serveressentials.common.utils.user.UserManager;
import java.util.Arrays;
import java.util.List;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

@ModuleCommand(moduleName = "Language")
public class ChannelCommand extends Command {

  private static List<String> emptyAutoCompletion =
      Arrays.asList("addFilter", "remFilter", "create", "delete", "list");

  @Override
  public String getName() {
    return "Channel";
  }

  @Override
  public String getUsage(Lang lang) {
    return "/channel <name, addFilter, remFilter, create, delete, list> <> | <find> | <word> | <channelName>  | <channelName> | <> <> | <replace> | <> | <prefix> | <> | <>";
  }

  @Override
  public String getDescription(Lang lang) {
    return lang.local.DESCRIPTION_CHANNEL;
  }

  @Override
  public void execute(
      MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    if (args.length > 0) {
      if (DataHelper.get(Storage.CHANNEL, args[0]) != null) {
        Channel ch = DataHelper.get(Storage.CHANNEL, args[0], new Channel());
        if (sender.getCommandSenderEntity() instanceof EntityPlayer) {
          EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
          UserManager.setUserChannel(player, ch);
          ChatHelper.sendMessage(
              sender,
              senderLang.local.LANG_CHANNEL_CHANGED.replaceAll(Replacment.CHANNEL, ch.getName()));
        }
      } else {
        boolean notValidInput = true;
        for (String validInput : emptyAutoCompletion) {
          if (args[0].equalsIgnoreCase(validInput)) {
            notValidInput = false;
          }
        }
        if (notValidInput) {
          ChatHelper.sendMessage(
              sender,
              senderLang.local.LANG_CHANNEL_INVALID.replaceAll(Replacment.CHANNEL, args[0]));
          list(server, sender, args, senderLang);
        }
      }
    } else {
      ChatHelper.sendMessage(sender, getUsage(LanguageModule.getUserLanguage(sender)));
    }
  }

  @SubCommand(aliases = "createFilter")
  public void addFilter(
      MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    if (args.length == 3) {
      if (DataHelper.get(Storage.CHANNEL, args[0]) != null) {
        Channel ch = DataHelper.get(Storage.CHANNEL, args[0], new Channel());
        ch.addFilter(args[1], args[2]);
        DataHelper.save(Storage.CHANNEL, ch);
        ChatHelper.sendMessage(
            sender,
            senderLang
                .local
                .LANG_CHANNEL_ADDFILTER
                .replaceAll(Replacment.CHANNEL, ch.getName())
                .replaceAll("%Find%", args[1])
                .replaceAll("%Replace%", args[2]));
      } else {
        ChatHelper.sendMessage(
            sender, senderLang.local.LANG_CHANNEL_INVALID.replaceAll("%Channel%", args[0]));
      }
    } else {
      ChatHelper.sendMessage(sender, getUsage(senderLang));
    }
  }

  @SubCommand(aliases = {"deleteFilter", "remFilter", "removeFilter"})
  public void delFilter(
      MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {}

  @SubCommand
  public void reload(
      MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {}

  @SubCommand(aliases = "New")
  public void create(
      MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {}

  @SubCommand(aliases = {"Del", "Rem", "Remove"})
  public void delete(
      MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {}

  @SubCommand(aliases = "Show")
  public void list(MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {}

  @Override
  public List<String> getAliases(List<String> aliases) {
    aliases.add("ch");
    aliases.add("chat");
    return aliases;
  }

  @Override
  public List<String> getAutoCompletion(
      MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
    if (args.length == 0 || args.length == 1 && args[0].isEmpty()) {
      return emptyAutoCompletion;
    }
    return getAutoCompletion(server, sender, args, pos);
  }
}
