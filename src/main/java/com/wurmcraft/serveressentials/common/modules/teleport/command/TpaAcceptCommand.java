package com.wurmcraft.serveressentials.common.modules.teleport.command;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.api.lang.Lang;
import com.wurmcraft.serveressentials.api.storage.LocationWrapper;
import com.wurmcraft.serveressentials.common.modules.language.ChatHelper;
import com.wurmcraft.serveressentials.common.modules.language.LanguageModule;
import com.wurmcraft.serveressentials.common.modules.teleport.TeleportModule;
import com.wurmcraft.serveressentials.common.reference.Replacment;
import com.wurmcraft.serveressentials.common.utils.user.TeleportUtils;
import java.util.List;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

@ModuleCommand(moduleName = "Teleportation")
public class TpaAcceptCommand extends Command {

  @Override
  public String getName() {
    return "tpaAccept";
  }

  @Override
  public String getUsage(Lang lang) {
    return "/tpaAccept";
  }

  @Override
  public String getDescription(Lang lang) {
    return lang.local.DESCRIPTION_TPACCEPT;
  }

  @Override
  public void execute(
      MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
    if (TeleportModule.tpaRequests.containsKey(player.getGameProfile().getId())) {
      EntityPlayer otherPlayer = TeleportModule.tpaRequests.get(player.getGameProfile().getId());
      ChatHelper.sendMessage(sender, senderLang.local.TELEPORT_TPA_ACCEPT);
      ChatHelper.sendMessage(
          otherPlayer,
          LanguageModule.getUserLanguage(otherPlayer)
              .local
              .TELEPORT_TPA_ACCEPT_REQUEST
              .replaceAll(Replacment.PLAYER, player.getDisplayNameString()));
      boolean safe =
          TeleportUtils.teleportTo(
              (EntityPlayerMP) otherPlayer,
              new LocationWrapper(player.getPosition(), player.dimension),
              true,
              true);
      if (!safe)
        ChatHelper.sendMessage(
            otherPlayer, LanguageModule.getUserLanguage(otherPlayer).local.TELEPORT_FAILED);
      TeleportModule.tpaRequests.remove(player.getGameProfile().getId());
    } else {
      ChatHelper.sendMessage(sender, senderLang.local.TELEPORT_TPA_NONE);
    }
  }

  @Override
  public List<String> getAliases(List<String> aliases) {
    aliases.add("Accept");
    aliases.add("TpAccept");
    aliases.add("acc");
    return aliases;
  }
}
