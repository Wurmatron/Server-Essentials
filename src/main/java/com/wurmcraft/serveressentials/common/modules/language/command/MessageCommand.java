package com.wurmcraft.serveressentials.common.modules.language.command;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.api.lang.Lang;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.modules.language.ChatHelper;
import com.wurmcraft.serveressentials.common.reference.Replacment;
import com.wurmcraft.serveressentials.common.utils.command.CommandUtils;
import com.wurmcraft.serveressentials.common.utils.user.UserManager;
import java.util.List;
import java.util.UUID;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import org.cliffc.high_scale_lib.NonBlockingHashMap;

@ModuleCommand(moduleName = "Language")
public class MessageCommand extends Command {

  public static NonBlockingHashMap<UUID, UUID> lastMessage = new NonBlockingHashMap<>();

  @Override
  public String getName() {
    return "message";
  }

  @Override
  public String getUsage(Lang lang) {
    return "/msg <player> <msg>";
  }

  @Override
  public String getDescription(Lang lang) {
    return lang.local.DESCRIPTION_MESSAGE;
  }

  @Override
  public void execute(
      MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    if (args.length == 2) {
      EntityPlayer otherPlayer = CommandUtils.getPlayerForName(args[0]);
      if (otherPlayer != null) {
        String msg =
            ConfigHandler.msgFormat
                .replaceAll(Replacment.USERNAME, sender.getDisplayName().getFormattedText())
                .replaceAll(
                    Replacment.RANK,
                    UserManager.getUserRank((EntityPlayer) sender.getCommandSenderEntity())
                        .getPrefix());
        ChatHelper.sendMessage(otherPlayer, msg);
        ChatHelper.sendMessage(sender, msg);
        ChatHelper.notifySpy(msg);
        lastMessage.put(
            ((EntityPlayer) sender.getCommandSenderEntity()).getGameProfile().getId(),
            otherPlayer.getGameProfile().getId());
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
    if (args.length == 0 || args.length == 1) {
      return CommandUtils.predictUsernames(args, 0);
    }
    return super.getAutoCompletion(server, sender, args, pos);
  }

  @Override
  public List<String> getAliases(List<String> aliases) {
    aliases.add("Msg");
    aliases.add("P");
    aliases.add("Pm");
    return aliases;
  }
}
