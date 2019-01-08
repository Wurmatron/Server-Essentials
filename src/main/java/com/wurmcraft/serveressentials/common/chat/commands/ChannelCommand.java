package com.wurmcraft.serveressentials.common.chat.commands;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.SubCommand;
import com.wurmcraft.serveressentials.api.json.global.Channel;
import com.wurmcraft.serveressentials.api.json.global.Channel.Type;
import com.wurmcraft.serveressentials.api.json.global.Keys;
import com.wurmcraft.serveressentials.api.json.user.file.PlayerData;
import com.wurmcraft.serveressentials.api.json.user.rest.LocalUser;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.chat.ChatHelper;
import com.wurmcraft.serveressentials.common.general.utils.DataHelper;
import com.wurmcraft.serveressentials.common.utils.SECommand;
import com.wurmcraft.serveressentials.common.utils.UserManager;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nullable;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.ClickEvent;

@Command(moduleName = "Chat")
public class ChannelCommand extends SECommand {

  private static void setUserChannel(EntityPlayer player, Channel channel) {
    if (ConfigHandler.storageType.equalsIgnoreCase("Rest")) {
      LocalUser user = (LocalUser) UserManager.getPlayerData(player.getGameProfile().getId())[1];
      user.setCurrentChannel(channel.getName());
      DataHelper.forceSave(Keys.LOCAL_USER, user);
    } else if (ConfigHandler.storageType.equalsIgnoreCase("File")) {
      PlayerData data = (PlayerData) UserManager.getPlayerData(player.getGameProfile().getId())[0];
      data.setCurrentChannel(channel);
      DataHelper.forceSave(Keys.PLAYER_DATA, data);
    }
  }

  @Override
  public String getName() {
    return "Channel";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    super.execute(server, sender, args);
    if (args.length == 0) {
      sender.sendMessage(new TextComponentString(getUsage(sender)));
    } else if (args.length == 1 && !args[0].equalsIgnoreCase("list")) {
      Channel ch = (Channel) DataHelper.get(Keys.CHANNEL, args[0]);
      // TODO Support for different channel types
      if (ch != null) {
        setUserChannel((EntityPlayer) sender.getCommandSenderEntity(), ch);
        ChatHelper.sendMessage(
            sender,
            getCurrentLanguage(sender).CHANNEL_CHANGED.replaceAll("%CHANNEL%", ch.getName()));
      }
    }
  }

  @SubCommand
  public void list(ICommandSender sender, String[] args) {
    List<Channel> channels = DataHelper.getData(Keys.CHANNEL, new Channel());
    sender.sendMessage(
        new TextComponentString(
            getCurrentLanguage(sender).CHAT_SPACER.replaceAll("&", FORMATTING_CODE)));
    for (Channel ch : channels) {
      TextComponentString msg =
          new TextComponentString(
              getCurrentLanguage(sender)
                  .CHANNEL_LIST
                  .replaceAll("%TYPE%", ch.getType().name())
                  .replaceAll("%PREFIX%", ch.getPrefix())
                  .replaceAll("%NAME%", ch.getName())
                  .replaceAll("&", FORMATTING_CODE));
      msg.getStyle()
          .setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/channel " + ch.getName()));
      sender.sendMessage(msg);
    }
    sender.sendMessage(
        new TextComponentString(
            getCurrentLanguage(sender).CHAT_SPACER.replaceAll("&", FORMATTING_CODE)));
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "\u00A79/channel \u00A7blist\u00A77|\u00A7bname | create | delete | addFilter | reload";
  }

  @Override
  public List<String> getAltNames() {
    List<String> alts = new ArrayList<>();
    alts.add("ch");
    alts.add("c");
    return alts;
  }

  @Override
  public String getDescription(ICommandSender sender) {
    return getCurrentLanguage(sender).COMMAND_CHANNEL;
  }

  @Override
  public boolean hasSubCommand() {
    return true;
  }

  @Override
  public boolean canConsoleRun() {
    return false;
  }

  @Override
  public List<String> getTabCompletions(
      MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
    List<String> predictions = new ArrayList<>();
    if (args.length > 0 && !args[0].isEmpty()) {
      for (Channel ch : DataHelper.getData(Keys.CHANNEL, new Channel())) {
        if (ch.getName().length() > args[0].length()
            && ch.getName().substring(0, args[0].length()).equalsIgnoreCase(args[0])) {
          predictions.add(ch.getName());
        }
      }
    }
    if (predictions.isEmpty()) {
      DataHelper.getData(Keys.CHANNEL, new Channel())
          .stream()
          .map(Channel::getName)
          .forEach(predictions::add);
    }
    return predictions;
  }

  @SubCommand
  public void create(ICommandSender sender, String[] args) {
    for (Channel ch : DataHelper.getData(Keys.CHANNEL, new Channel())) {
      if (ch.getName().equalsIgnoreCase(args[0])) {
        ChatHelper.sendMessage(
            sender, getCurrentLanguage(sender).CHANNEL_EXISTS.replaceAll("%CHANNEL%", args[0]));
        return;
      }
    }
    if (args.length >= 3) {
      try {
        Channel ch = new Channel(args[0], args[1], Type.valueOf(args[2]), "");
        DataHelper.createIfNonExist(Keys.CHANNEL, ch);
      } catch (Exception e) {
        ChatHelper.sendMessage(
            sender,
            getCurrentLanguage(sender)
                .CHANNEL_TYPE
                .replaceAll("%TYPES%", Arrays.toString(Type.values())));
      }
    } else {
      ChatHelper.sendMessage(sender, getUsage(sender));
    }
  }

  @SubCommand
  public void delete(ICommandSender sender, String[] args) {
    boolean found = false;
    for (Channel ch : DataHelper.getData(Keys.CHANNEL, new Channel())) {
      if (ch.getName().equalsIgnoreCase(args[0])) {
        found = true;
        DataHelper.delete(Keys.CHANNEL, ch);
      }
    }
    if (!found) {
      ChatHelper.sendMessage(sender, getUsage(sender));
    }
  }

  @SubCommand
  public void addFilter(ICommandSender sender, String[] args) {
    boolean found = false;
    for (Channel ch : DataHelper.getData(Keys.CHANNEL, new Channel())) {
      if (ch.getName().equalsIgnoreCase(args[0])) {
        found = true;
        ch.addFilter(args[1], args[2]);
      }
    }
    if (!found) {
      ChatHelper.sendMessage(sender, getUsage(sender));
    }
  }

  @SubCommand
  public void reload(ICommandSender sender, String[] args) {
    for (Iterator<Channel> iterator = DataHelper.getData(Keys.CHANNEL, new Channel()).iterator();
        iterator.hasNext();
        ) {
      DataHelper.remove(Keys.CHANNEL, iterator.next());
    }
    for (File file :
        Objects.requireNonNull(
            new File(
                    ConfigHandler.saveLocation
                        + File.separator
                        + Keys.CHANNEL.name()
                        + File.separator)
                .listFiles())) {
      DataHelper.load(file, Keys.CHANNEL, new Channel());
    }
  }
}
