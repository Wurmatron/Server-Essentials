package com.wurmcraft.serveressentials.common.chat.commands;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.SECommand;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.chat.ChatHelper;
import com.wurmcraft.serveressentials.common.general.utils.DataHelper;
import com.wurmcraft.serveressentials.common.reference.Keys;
import com.wurmcraft.serveressentials.common.utils.UsernameResolver;
import java.util.List;
import javax.annotation.Nullable;
import joptsimple.internal.Strings;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;

@Command(moduleName = "Chat")
public class MsgCommand extends SECommand {

  @Override
  public String getName() {
    return "msg";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    super.execute(server, sender, args);
    if (args.length > 1 && sender != null) {
      EntityPlayer reciv = UsernameResolver.getPlayer(args[0]);
      if (reciv != null) {
        String[] lines = new String[args.length - 1];
        for (int index = 1; index < args.length; index++) {
          lines[index - 1] = args[index];
        }
        String message = Strings.join(lines, " ");
        if (sender.getCommandSenderEntity() != null
            && sender.getCommandSenderEntity() instanceof EntityPlayer) {
          EntityPlayer entitySender = (EntityPlayer) sender.getCommandSenderEntity();
          ChatHelper.sendMessage(
              reciv,
              ConfigHandler.msgFormat
                  .replaceAll(
                      ChatHelper.USERNAME_KEY,
                      TextFormatting.AQUA
                          + UsernameResolver.getNick(reciv.getGameProfile().getId()))
                  .replaceAll(ChatHelper.MESSAGE_KEY, TextFormatting.GRAY + message)
                  .replaceAll(
                      "%sender%", UsernameResolver.getNick(entitySender.getGameProfile().getId())));
          ChatHelper.sendMessage(
              entitySender,
              ConfigHandler.msgFormat
                  .replaceAll(
                      ChatHelper.USERNAME_KEY,
                      TextFormatting.AQUA
                          + UsernameResolver.getNick(reciv.getGameProfile().getId()))
                  .replaceAll(ChatHelper.MESSAGE_KEY, TextFormatting.GRAY + message)
                  .replaceAll(
                      "%sender%", UsernameResolver.getNick(entitySender.getGameProfile().getId())));
          DataHelper.addTemp(
              Keys.LAST_MESSAGE,
              entitySender.getGameProfile().getId(),
              reciv.getGameProfile().getId(),
              false);
        } else {
          ChatHelper.sendMessage(
              reciv,
              ConfigHandler.msgFormat
                  .replaceAll(ChatHelper.USERNAME_KEY, TextFormatting.AQUA + "Server")
                  .replaceAll(ChatHelper.MESSAGE_KEY, TextFormatting.GRAY + message));
        }
      } else {
        ChatHelper.sendMessage(
            sender, getCurrentLanguage(sender).PLAYER_NOT_FOUND.replaceAll("%PLAYER%", args[0]));
      }
    } else {
      ChatHelper.sendMessage(sender, getUsage(sender));
    }
  }

  @Override
  public List<String> getTabCompletions(
      MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
    return autoCompleteUsername(args, 0);
  }
}
