package com.wurmcraft.serveressentials.common.economy.commands;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.SubCommand;
import com.wurmcraft.serveressentials.api.json.user.optional.Coin;
import com.wurmcraft.serveressentials.api.json.user.optional.Currency;
import com.wurmcraft.serveressentials.api.json.user.rest.GlobalUser;
import com.wurmcraft.serveressentials.common.chat.ChatHelper;
import com.wurmcraft.serveressentials.common.economy.EconomyModule;
import com.wurmcraft.serveressentials.common.rest.utils.RequestHelper;
import com.wurmcraft.serveressentials.common.utils.SECommand;
import com.wurmcraft.serveressentials.common.utils.UserManager;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

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

  @SubCommand
  public void exchange(ICommandSender sender, String[] args) {
    if (args.length == 3 && sender.getCommandSenderEntity() instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
      Currency currentCurrency = EconomyModule.getCurrency(args[0]);
      if (currentCurrency != null) {
        try {
          double amount = Double.parseDouble(args[1]);
          Currency newCurrency = EconomyModule.getCurrency(args[2]);
          GlobalUser user =
              (GlobalUser) UserManager.getPlayerData(player.getGameProfile().getId())[0];
          user.getBank().spend(currentCurrency.getName(), amount);
          double amt = (currentCurrency.getSell() * amount) / newCurrency.getBuy();
          user.getBank().earn(newCurrency.getName(), amt);
          ChatHelper.sendMessage(
              sender,
              getCurrentLanguage(sender)
                  .CURRENCY_CONVERT
                  .replaceAll("%CURRENCY%", currentCurrency.getName())
                  .replaceAll("%AMOUNT%", "" + amount)
                  .replaceAll("%AMOUNT2%", "" + amt)
                  .replaceAll("%CURRENCY2%", newCurrency.getName()));
          UserManager.PLAYER_DATA.put(
              player.getGameProfile().getId(),
              new Object[] {user, UserManager.PLAYER_DATA.get(player.getGameProfile().getId())[1]});
          RequestHelper.UserResponses.overridePlayerData(user);
        } catch (NumberFormatException e) {
          ChatHelper.sendMessage(
              sender, getCurrentLanguage(sender).INVALID_NUMBER.replaceAll("%NUMBER%", args[1]));
        }
      }
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
