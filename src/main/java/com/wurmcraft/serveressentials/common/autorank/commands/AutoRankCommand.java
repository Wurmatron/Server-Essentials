package com.wurmcraft.serveressentials.common.autorank.commands;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.SECommand;
import com.wurmcraft.serveressentials.api.command.SubCommand;
import com.wurmcraft.serveressentials.api.json.user.fileOnly.AutoRank;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.autorank.AutoRankModule;
import com.wurmcraft.serveressentials.common.chat.ChatHelper;
import com.wurmcraft.serveressentials.common.utils.UserManager;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

@Command(moduleName = "AutoRank")
public class AutoRankCommand extends SECommand {

  @Override
  public String getName() {
    return "autorank";
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "\u00A7b/ar check";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    super.execute(server, sender, args);
  }

  @Override
  public boolean hasSubCommand() {
    return true;
  }

  @SubCommand
  public void check(ICommandSender sender, String[] args) {
    if (args.length == 0) {
      if (sender.getCommandSenderEntity() instanceof EntityPlayer) {
        EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
        AutoRank auto = AutoRankModule
            .getAutorankFromRank(UserManager.getPlayerRank(player.getGameProfile().getId()));
        if (auto != null) {
          ChatHelper.sendMessage(sender, getCurrentLanguage(sender).CHAT_SPACER);
          ChatHelper.sendMessage(sender,
              getCurrentLanguage(sender).CHAT_RANK + ": " + auto.getNextRank());
          ChatHelper.sendMessage(sender,
              getCurrentLanguage(sender).CHAT_TIME + ": " + auto.getPlayTime());
          ChatHelper.sendMessage(sender, auto.getBalance() + " " + ConfigHandler.globalCurrency);
          ChatHelper.sendMessage(sender, auto.getExp() + " EXP");
          ChatHelper.sendMessage(sender, getCurrentLanguage(sender).CHAT_SPACER);
        }
      }
    }
  }
}
