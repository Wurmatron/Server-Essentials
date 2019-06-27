package com.wurmcraft.serveressentials.common.modules.teleport.command;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.api.lang.Lang;
import com.wurmcraft.serveressentials.common.ServerEssentialsServer;
import com.wurmcraft.serveressentials.common.modules.language.ChatHelper;
import com.wurmcraft.serveressentials.common.modules.language.LanguageModule;
import com.wurmcraft.serveressentials.common.modules.teleport.TeleportModule;
import com.wurmcraft.serveressentials.common.reference.Replacment;
import com.wurmcraft.serveressentials.common.utils.command.CommandUtils;
import com.wurmcraft.serveressentials.common.utils.user.UserManager;
import java.util.List;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

@ModuleCommand(moduleName = "Teleportation")
public class TpaCommand extends Command {

  @Override
  public String getName() {
    return "Tpa";
  }

  @Override
  public String getUsage(Lang lang) {
    return "/tpa <user>";
  }

  @Override
  public String getDescription(Lang lang) {
    return lang.local.DESCRIPTION_TPA;
  }

  @Override
  public void execute(
      MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    if (args.length == 1) {
      EntityPlayer player = CommandUtils.getPlayerForName(args[0]);
      if (player != null) {
        if (UserManager.isAcceptingTPARequests(player)) {
          TeleportModule.addRequest(
              player.getGameProfile().getId(), (EntityPlayer) sender.getCommandSenderEntity());
          ChatHelper.sendMessage(
              player,
              LanguageModule.getUserLanguage(player)
                  .local
                  .TELEPORT_REQUEST
                  .replaceAll(Replacment.PLAYER, sender.getName()));
        } else {
          ServerEssentialsServer.LOGGER.info(
              sender.getDisplayName()
                  + " tried to teleport to "
                  + player.getDisplayNameString()
                  + " but it was blocked due to tp lock");
        }
        ChatHelper.sendMessage(sender, senderLang.local.TELEPORT_TPA_SENT);
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
