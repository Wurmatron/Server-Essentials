package com.wurmcraft.serveressentials.common.teleport.commands.user.tpa;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.SECommand;
import com.wurmcraft.serveressentials.api.json.user.restOnly.LocalUser;
import com.wurmcraft.serveressentials.common.language.LanguageModule;
import com.wurmcraft.serveressentials.common.language.Local;
import com.wurmcraft.serveressentials.common.teleport.TeleportationModule;
import com.wurmcraft.serveressentials.common.utils.UserManager;
import com.wurmcraft.serveressentials.common.utils.UsernameResolver;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

@Command(moduleName = "Teleportation")
public class TpaCommand extends SECommand {

  @Override
  public String getName() {
    return "tpa";
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "/tpa <user>";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    super.execute(server, sender, args);
    EntityPlayer senderPlayer = (EntityPlayer) sender.getCommandSenderEntity();
    if (args.length == 1) {
      EntityPlayer player = UsernameResolver.getPlayer(args[0]);
      Local lang = LanguageModule.getLangfromUUID(player.getGameProfile().getId());
      if (player != null) {
        LocalUser user = (LocalUser) UserManager.getPlayerData(player.getGameProfile().getId())[1];
        if (user != null && !user.isTpLock()) {
          TeleportationModule.activeRequests.put(
              System.currentTimeMillis(), new EntityPlayer[]{senderPlayer, player});
          sender.sendMessage(new TextComponentString(getCurrentLanguage(sender).TPA_SENT));
          player.sendMessage(
              new TextComponentString(
                  lang.TPA_Recive.replaceAll("%PLAYER%", senderPlayer.getDisplayNameString())
                      .replaceAll("%ACCEPT%", lang.CHAT_ACCEPT)
                      .replaceAll("%DENY%", lang.CHAT_DENY)));
        }
      } else {
        sender.sendMessage(
            new TextComponentString(
                TextFormatting.RED
                    + getCurrentLanguage(sender).PLAYER_NOT_FOUND.replaceAll("%PLAYER%", args[0])));
      }
    } else {
      sender.sendMessage(new TextComponentString(getUsage(sender)));
    }
  }

  @Override
  public boolean canConsoleRun() {
    return false;
  }
}
