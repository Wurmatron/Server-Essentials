package com.wurmcraft.serveressentials.common.general.commands.perk;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.SECommand;
import com.wurmcraft.serveressentials.common.language.LanguageModule;
import com.wurmcraft.serveressentials.common.utils.UsernameResolver;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

@Command(moduleName = "General")
public class FeedCommand extends SECommand {

  @Override
  public String getName() {
    return "feed";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    super.execute(server, sender, args);
    if (args.length == 0 && sender.getCommandSenderEntity() instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
      player.getFoodStats().setFoodLevel(20);
      player.sendMessage(new TextComponentString(getCurrentLanguage(sender).FEED));
    } else if (args.length == 1) {
      EntityPlayer player = UsernameResolver.getPlayer(args[0]);
      if (player != null) {
        player.getFoodStats().setFoodLevel(20);
        player.sendMessage(
            new TextComponentString(
                LanguageModule.getLangfromUUID(player.getGameProfile().getId()).FEED));
        sender.sendMessage(
            new TextComponentString(
                getCurrentLanguage(sender)
                    .FEED_OTHER
                    .replaceAll("%PLAYER%", player.getDisplayNameString())));
      } else {
        sender.sendMessage(
            new TextComponentString(
                getCurrentLanguage(sender).PLAYER_NOT_FOUND.replaceAll("%PLAYER%", args[0])));
      }
    } else {
      sender.sendMessage(new TextComponentString(getUsage(sender)));
    }
  }
}
