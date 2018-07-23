package com.wurmcraft.serveressentials.common.economy.commands;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.SECommand;
import com.wurmcraft.serveressentials.api.command.SubCommand;
import com.wurmcraft.serveressentials.api.json.user.optional.Coin;
import com.wurmcraft.serveressentials.api.json.user.restOnly.GlobalUser;
import com.wurmcraft.serveressentials.common.chat.ChatHelper;
import com.wurmcraft.serveressentials.common.utils.UserManager;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

@Command(moduleName = "Economy")
public class BankCommand extends SECommand {

  @Override
  public String getName() {
    return "bank";
  }

  @Override
  public List<String> getAltNames() {
    List<String> alts = new ArrayList<>();
    alts.add("eco");
    alts.add("b");
    return alts;
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "/bank";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    super.execute(server, sender, args);
  }

  @SubCommand
  public void info(ICommandSender sender, String[] args) {
    if (sender.getCommandSenderEntity() instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
      GlobalUser data = (GlobalUser) UserManager.getPlayerData(player.getGameProfile().getId())[0];
      ChatHelper.sendMessage(sender, getCurrentLanguage(sender).CHAT_SPACER);
      for (Coin coin : data.getBank().getCurrency()) {
        ChatHelper.sendMessage(
            sender, coin.getName().replaceAll("_", "") + ": " + coin.getAmount());
      }
      ChatHelper.sendMessage(sender, getCurrentLanguage(sender).CHAT_SPACER);
    } else {
      ChatHelper.sendMessage(sender, getCurrentLanguage(sender).PLAYER_ONLY);
    }
  }

  @Override
  public boolean canConsoleRun() {
    return true;
  }

  @Override
  public boolean hasSubCommand() {
    return true;
  }
}
