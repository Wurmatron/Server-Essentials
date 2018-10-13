package com.wurmcraft.serveressentials.common.economy.commands;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.json.user.rest.GlobalUser;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.utils.SECommand;
import com.wurmcraft.serveressentials.common.utils.UserManager;
import com.wurmcraft.serveressentials.common.utils.UsernameResolver;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

// TODO Rework Command
@Command(moduleName = "Economy")
public class BalanceCommand extends SECommand {

  @Override
  public String getName() {
    return "balance";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    super.execute(server, sender, args);
    if (args.length == 0 && sender.getCommandSenderEntity() instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
      GlobalUser gloal = (GlobalUser) UserManager.getPlayerData(player.getGameProfile().getId())[0];
      sender.sendMessage(
          new TextComponentString(
              getCurrentLanguage(sender).CHAT_SPACER.replaceAll("&", FORMATTING_CODE)));
      for (String active : ConfigHandler.activeCurrency) {
        sender.sendMessage(
            new TextComponentString(
                TextFormatting.LIGHT_PURPLE
                    + active
                    + ": "
                    + TextFormatting.GOLD
                    + gloal.getBank().getCurrency(active.replaceAll(" ", "_"))));
      }
      sender.sendMessage(
          new TextComponentString(
              getCurrentLanguage(sender).CHAT_SPACER.replaceAll("&", FORMATTING_CODE)));
    } else if (args.length == 1) {
      GlobalUser user = forceUserFromUUID(UsernameResolver.getUUIDFromName(args[0]));
      sender.sendMessage(
          new TextComponentString(
              getCurrentLanguage(sender).CHAT_SPACER.replaceAll("&", FORMATTING_CODE)));
      for (String active : ConfigHandler.activeCurrency) {
        sender.sendMessage(
            new TextComponentString(
                TextFormatting.LIGHT_PURPLE
                    + active
                    + ": "
                    + TextFormatting.GOLD
                    + user.getBank().getCurrency(active.replaceAll(" ", "_"))));
      }
      sender.sendMessage(
          new TextComponentString(
              getCurrentLanguage(sender).CHAT_SPACER.replaceAll("&", FORMATTING_CODE)));
    } else {
      sender.sendMessage(new TextComponentString(getUsage(sender)));
    }
  }
}
