package com.wurmcraft.serveressentials.common.teleport.commands.user.tpa;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.SECommand;
import com.wurmcraft.serveressentials.api.json.user.LocationWrapper;
import com.wurmcraft.serveressentials.api.json.user.restOnly.LocalUser;
import com.wurmcraft.serveressentials.common.chat.ChatHelper;
import com.wurmcraft.serveressentials.common.language.LanguageModule;
import com.wurmcraft.serveressentials.common.language.Local;
import com.wurmcraft.serveressentials.common.teleport.TeleportationModule;
import com.wurmcraft.serveressentials.common.teleport.utils.TeleportUtils;
import com.wurmcraft.serveressentials.common.utils.UserManager;
import com.wurmcraft.serveressentials.common.utils.UsernameResolver;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.ClickEvent.Action;
import net.minecraft.util.text.event.HoverEvent;

@Command(moduleName = "Teleportation")
public class TpaCommand extends SECommand {

  @Override
  public String getName() {
    return "tpa";
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "\u00A79/tpa \u00A7b<user>";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    super.execute(server, sender, args);
    EntityPlayer senderPlayer = (EntityPlayer) sender.getCommandSenderEntity();
    if (args.length == 1) {
      EntityPlayer player = UsernameResolver.getPlayer(args[0]);
      if (player != null) {
        Local lang = LanguageModule.getLangfromUUID(player.getGameProfile().getId());
        LocalUser user = (LocalUser) UserManager.getPlayerData(player.getGameProfile().getId())[1];
        if (user != null && !user.isTpLock()) {
          if (player.getGameProfile().getId().equals(senderPlayer.getGameProfile().getId())) {
            return;
          }
          if (TeleportUtils.safeToTeleport(player,
              new LocationWrapper(player.getPosition(), player.dimension))) {
            TeleportationModule.activeRequests.put(
                System.currentTimeMillis(), new EntityPlayer[]{senderPlayer, player});
            sender.sendMessage(
                new TextComponentString(
                    getCurrentLanguage(sender).TPA_SENT.replaceAll("&", "\u00A7")));
            TextComponentString msg =
                new TextComponentString(
                    lang.TPA_Recive.replaceAll("%PLAYER%", senderPlayer.getDisplayNameString())
                        .replaceAll("%ACCEPT%", lang.CHAT_ACCEPT.replaceAll("&", "\u00A7"))
                        .replaceAll("%DENY%", lang.CHAT_DENY.replaceAll("&", "\u00A7"))
                        .replaceAll("&", "\u00A7"));
            msg.getStyle().setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/tpaccept"));
            msg.getStyle()
                .setHoverEvent(
                    new HoverEvent(
                        HoverEvent.Action.SHOW_TEXT, new TextComponentString("\u00A7d/tpaccept")));
            player.sendMessage(msg);
          } else {
            ChatHelper.sendMessage(sender, getCurrentLanguage(sender).TPA_NOTSAFE
                .replaceAll("%PLAYER%", player.getDisplayNameString()));
          }
        }
      } else {
        sender.sendMessage(
            new TextComponentString(
                getCurrentLanguage(sender)
                    .PLAYER_NOT_FOUND
                    .replaceAll("%PLAYER%", args[0])
                    .replaceAll("&", "\u00A7")));
      }
    } else {
      sender.sendMessage(new TextComponentString(getUsage(sender)));
    }
  }

  @Override
  public boolean canConsoleRun() {
    return false;
  }

  @Override
  public String getDescription(ICommandSender sender) {
    return getCurrentLanguage(sender).COMMAND_TPA;
  }

  @Override
  public List<String> getTabCompletions(
      MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
    return autoCompleteUsername(args, 0);
  }
}
