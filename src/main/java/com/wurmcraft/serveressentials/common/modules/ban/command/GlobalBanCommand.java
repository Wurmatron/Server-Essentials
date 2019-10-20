package com.wurmcraft.serveressentials.common.modules.ban.command;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.api.lang.Lang;
import com.wurmcraft.serveressentials.api.storage.json.GlobalBan;
import com.wurmcraft.serveressentials.common.modules.ban.BanModule;
import com.wurmcraft.serveressentials.common.modules.language.ChatHelper;
import com.wurmcraft.serveressentials.common.reference.Replacment;
import com.wurmcraft.serveressentials.common.storage.rest.RequestGenerator;
import com.wurmcraft.serveressentials.common.utils.command.CommandUtils;
import java.util.Arrays;
import joptsimple.internal.Strings;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

@ModuleCommand(moduleName = "Ban")
public class GlobalBanCommand extends Command {

  @Override
  public String getName() {
    return "Ban";
  }

  @Override
  public String getUsage(Lang lang) {
    return "/ban <username> <reason>";
  }

  @Override
  public String getDescription(Lang lang) {
    return lang.local.DESCRIPTION_BAN_BAN;
  }

  @Override
  public void execute(
      MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    if (args.length >= 2) {
      EntityPlayerMP player = (EntityPlayerMP) CommandUtils.getPlayerForName(args[0]);
      if (player != null) {
        String reason = Strings.join(Arrays.copyOfRange(args, 1, args.length), " ");
        RequestGenerator.Ban.addBan(
            new GlobalBan(
                player.getDisplayNameString(),
                player.getGameProfile().getId().toString(),
                player.connection.getNetworkManager().getRemoteAddress().toString(),
                reason));
        BanModule.checkPlayer(player);
        ChatHelper.sendMessage(
            sender,
            senderLang
                .local
                .BAN_PLAYER
                .replaceAll(Replacment.PLAYER, player.getDisplayNameString())
                .replaceAll(Replacment.REASON, reason));
      } else {
        ChatHelper.sendMessage(
            sender, senderLang.local.PLAYER_NOT_FOUND.replaceAll(Replacment.PLAYER, args[0]));
      }
    }
  }
}
