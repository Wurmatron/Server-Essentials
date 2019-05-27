package com.wurmcraft.serveressentials.common.modules.language.command;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.api.lang.Lang;
import com.wurmcraft.serveressentials.common.modules.language.ChatHelper;
import com.wurmcraft.serveressentials.common.utils.command.CommandUtils;
import java.util.List;
import joptsimple.internal.Strings;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

@ModuleCommand(moduleName = "Language")
public class ReplyCommand extends Command {

  @Override
  public String getName() {
    return "Reply";
  }

  @Override
  public String getUsage(Lang lang) {
    return "/reply <msg>";
  }

  @Override
  public String getDescription(Lang lang) {
    return lang.local.DESCRIPTION_REPLY;
  }

  @Override
  public void execute(
      MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    if (args.length > 0) {
      EntityPlayer lastPlayer =
          CommandUtils.getPlayerFromUUID(
              (MessageCommand.lastMessage.getOrDefault(
                  ((EntityPlayer) sender.getCommandSenderEntity()).getGameProfile().getId(),
                  null)));
      if (lastPlayer != null) {
        FMLCommonHandler.instance()
            .getMinecraftServerInstance()
            .getCommandManager()
            .executeCommand(sender, "/msg " + lastPlayer.getName() + " " + Strings.join(args, " "));
      } else {
        ChatHelper.sendMessage(
            sender, senderLang.local.PLAYER_NOT_FOUND.replaceAll("%player%", "None"));
      }
    } else {
      ChatHelper.sendMessage(sender, getUsage(senderLang));
    }
  }

  @Override
  public List<String> getAliases(List<String> aliases) {
    aliases.add("R");
    return aliases;
  }
}
