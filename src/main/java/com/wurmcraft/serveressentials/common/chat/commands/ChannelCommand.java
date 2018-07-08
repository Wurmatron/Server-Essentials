package com.wurmcraft.serveressentials.common.chat.commands;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.SECommand;
import com.wurmcraft.serveressentials.api.command.SubCommand;
import com.wurmcraft.serveressentials.api.json.global.Channel;
import com.wurmcraft.serveressentials.api.json.user.restOnly.LocalUser;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.general.utils.DataHelper;
import com.wurmcraft.serveressentials.common.reference.Keys;
import com.wurmcraft.serveressentials.common.utils.UserManager;
import java.util.List;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

@Command(moduleName = "Chat")
public class ChannelCommand extends SECommand {

  private static void setUserChannel(EntityPlayer player, Channel channel) {
    if (ConfigHandler.storageType.equalsIgnoreCase("Rest")) {
      LocalUser user = (LocalUser) UserManager.getPlayerData(player.getGameProfile().getId())[1];
      user.setCurrentChannel(channel.getName());
      DataHelper.forceSave(Keys.LOCAL_USER, user);
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
      if (ch != null) {
        setUserChannel((EntityPlayer) sender.getCommandSenderEntity(), ch);
        sender.sendMessage(
            new TextComponentString(
                getCurrentLanguage(sender).CHANNEL_CHANGED.replaceAll("%CHANNEL%", ch.getName())));
      }
    }
  }

  @SubCommand
  public void list(ICommandSender sender, String[] args) {
    List<Channel> channels = DataHelper.getData(Keys.CHANNEL, new Channel());
    sender.sendMessage(new TextComponentString(getCurrentLanguage(sender).CHAT_SPACER));
    for (Channel ch : channels) {
      sender.sendMessage(new TextComponentString(ch.getName()));
    }
    sender.sendMessage(new TextComponentString(getCurrentLanguage(sender).CHAT_SPACER));
  }

  @Override
  public boolean hasSubCommand() {
    return true;
  }

  @Override
  public boolean canConsoleRun() {
    return false;
  }
}
