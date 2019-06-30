package com.wurmcraft.serveressentials.common.modules.language.command;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.api.command.SubCommand;
import com.wurmcraft.serveressentials.api.lang.Lang;
import com.wurmcraft.serveressentials.api.storage.json.Channel;
import com.wurmcraft.serveressentials.api.storage.json.Channel.Type;
import com.wurmcraft.serveressentials.common.modules.language.ChatHelper;
import com.wurmcraft.serveressentials.common.modules.language.LanguageModule;
import com.wurmcraft.serveressentials.common.reference.Replacment;
import com.wurmcraft.serveressentials.common.reference.Storage;
import com.wurmcraft.serveressentials.common.storage.file.DataHelper;
import com.wurmcraft.serveressentials.common.utils.command.CommandUtils;
import com.wurmcraft.serveressentials.common.utils.user.UserManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import joptsimple.internal.Strings;
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
                .replaceAll(Replacment.FIND, args[1])
                .replaceAll(Replacment.REPLACE, args[2]));
      } else {
        ChatHelper.sendMessage(
            sender, senderLang.local.LANG_CHANNEL_INVALID.replaceAll(Replacment.CHANNEL, args[0]));
      }
    } else {
      ChatHelper.sendMessage(sender, getUsage(senderLang));
    }
  }

  @SubCommand(aliases = {"deleteFilter", "remFilter", "removeFilter"})
  public void delFilter(
      MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    if (args.length == 2) {
      if (DataHelper.get(Storage.CHANNEL, args[0]) != null) {
        Channel ch = DataHelper.get(Storage.CHANNEL, args[0], new Channel());
        ch.removeFilter(args[1]);
        ChatHelper.sendMessage(
            sender,
            senderLang
                .local
                .LANG_CHANNEL_DELFILTER
                .replaceAll(Replacment.FILTER, args[1])
                .replaceAll(Replacment.CHANNEL, ch.getName()));
      } else {
        ChatHelper.sendMessage(
            sender, senderLang.local.LANG_CHANNEL_INVALID.replaceAll(Replacment.CHANNEL, args[0]));
      }
    } else {
      ChatHelper.sendMessage(sender, getUsage(senderLang));
    }
  }

  @SubCommand
  public void reload(
      MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    for (Channel ch : DataHelper.getData(Storage.CHANNEL, new Channel())) {
      DataHelper.remove(Storage.CHANNEL, ch);
    }
    LanguageModule.loadChannels();
    ChatHelper.sendMessage(sender, senderLang.local.LANG_CHANNEL_RELOADED);
  }

  @SubCommand(aliases = "New")
  public void create(
      MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    if (args.length == 5) {
      String name = args[0];
      if (validateName(name)) {
        Boolean logChat = CommandUtils.parseTF(args[2]);
        if (logChat != null) {
          Type type = getType(args[3]);
          if (type != null) {
            Channel ch = new Channel(name, args[1], type, args[4]);
            DataHelper.save(Storage.CHANNEL, ch);
            DataHelper.load(Storage.CHANNEL, ch);
            ChatHelper.sendMessage(
                sender,
                senderLang
                    .local
                    .LANG_CHANNEL_CREATED
                    .replaceAll(Replacment.CHANNEL, ch.getName())
                    .replaceAll(Replacment.PREFIX, ch.getPrefix())
                    .replaceAll(Replacment.TYPE, type.name()));
          } else {
            ChatHelper.sendMessage(
                sender,
                senderLang.local.LANG_CHANNEL_INVALID_TYPE.replaceAll(Replacment.TYPE, args[2]));
          }
        } else {
          ChatHelper.sendMessage(sender, senderLang.local.CHAT_INVALID_BOOLEAN);
        }
      } else {
        ChatHelper.sendMessage(
            sender,
            senderLang.local.LANG_CHANNEL_INVALID_NAME.replaceAll(Replacment.CHANNEL, name));
      }
    } else {
      ChatHelper.sendMessage(sender, getUsage(senderLang));
    }
  }

  @SubCommand(aliases = {"Del", "Rem", "Remove"})
  public void delete(
      MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    if (args.length == 1) {
      Channel ch = DataHelper.get(Storage.CHANNEL, args[0], new Channel());
      if (ch != null) {
        DataHelper.delete(Storage.CHANNEL, ch);
        ChatHelper.sendMessage(
            sender,
            senderLang.local.LANG_CHANNEL_DELETE.replaceAll(Replacment.CHANNEL, ch.getName()));
      } else {
        ChatHelper.sendMessage(
            sender,
            senderLang.local.LANG_CHANNEL_INVALID_NAME.replaceAll(Replacment.CHANNEL, args[0]));
      }
    } else {
      ChatHelper.sendMessage(sender, getUsage(senderLang));
    }
  }

  @SubCommand(aliases = "Show")
  public void list(MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    if (args.length == 1) {
      Channel ch = DataHelper.get(Storage.CHANNEL, args[0], new Channel());
      if (ch != null) {
        ChatHelper.sendMessage(sender, senderLang.local.CHAT_SPACER);
        ChatHelper.sendMessage(
            sender,
            senderLang.local.LANG_CHANNEL_NAME.replaceAll(Replacment.CHANNEL, ch.getName()));
        ChatHelper.sendMessage(
            sender,
            senderLang.local.LANG_CHANNEL_PREFIX.replaceAll(Replacment.PREFIX, ch.getPrefix()));
        ChatHelper.sendMessage(
            sender,
            senderLang.local.LANG_CHANNEL_TYPE.replaceAll(Replacment.TYPE, ch.getType().name()));
        ChatHelper.sendMessage(
            sender,
            senderLang.local.LANG_CHANNEL_FILTER.replaceAll(
                Replacment.FILTER, Strings.join(ch.getFilter(), ", ")));
        ChatHelper.sendMessage(sender, senderLang.local.CHAT_SPACER);
      } else {
        ChatHelper.sendMessage(
            sender,
            senderLang.local.LANG_CHANNEL_INVALID_NAME.replaceAll(Replacment.CHANNEL, args[0]));
      }
    } else if (args.length == 0) {
      ChatHelper.sendMessage(sender, senderLang.local.CHAT_SPACER);
      ChatHelper.sendMessage(sender, senderLang.local.LANG_CHANNEL_LIST);
      for (String ch : getChannelNames()) {
        ChatHelper.sendMessage(sender, ch);
      }
      ChatHelper.sendMessage(sender, senderLang.local.CHAT_SPACER);

    } else {
      ChatHelper.sendMessage(sender, getUsage(senderLang));
    }
  }

  private static String[] getChannelNames() {
    List<String> channels = new ArrayList<>();
    for (Channel ch : DataHelper.getData(Storage.CHANNEL, new Channel())) {
      if (!channels.contains(ch.getName())) {
        channels.add(ch.getName());
      }
    }
    return channels.toArray(new String[0]);
  }

  private static Type getType(String arg) {
    try {
      return Type.valueOf(arg.toUpperCase());
    } catch (Exception e) {
      return null;
    }
  }

  private static boolean validateName(String name) {
    for (String n : emptyAutoCompletion) {
      if (n.equalsIgnoreCase(name)) {
        return false;
      }
    }
    return true;
  }

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
