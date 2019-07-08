package com.wurmcraft.serveressentials.common.modules.economy.command;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.api.lang.Lang;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.modules.language.ChatHelper;
import com.wurmcraft.serveressentials.common.modules.language.LanguageModule;
import com.wurmcraft.serveressentials.common.reference.Replacment;
import com.wurmcraft.serveressentials.common.utils.command.CommandUtils;
import com.wurmcraft.serveressentials.common.utils.user.UserManager;
import java.util.List;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

@ModuleCommand(moduleName = "Economy")
public class PayCommand extends Command {

  @Override
  public String getName() {
    return "Pay";
  }

  @Override
  public String getUsage(Lang lang) {
    return "/pay <user> <currency> <amount>";
  }

  @Override
  public String getDescription(Lang lang) {
    return lang.local.DESCRIPTION_PAY;
  }

  @Override
  public void execute(
      MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    if (args.length == 2) {
      handleCurrencyExchange(
          (EntityPlayer) sender.getCommandSenderEntity(),
          args[0],
          ConfigHandler.serverCurrency,
          args[1]);
    } else if (args.length == 3) { // non-standard currency
      handleCurrencyExchange(
          (EntityPlayer) sender.getCommandSenderEntity(), args[0], args[2], args[1]);
    } else {
      ChatHelper.sendMessage(sender, getUsage(senderLang));
    }
  }

  public static void handleCurrencyExchange(
      EntityPlayer a, String b, String currency, String userAmount) {
    EntityPlayer player = CommandUtils.getPlayerForName(b);
    if (player != null) {
      try {
        double amount = Double.parseDouble(userAmount);
        double spent = UserManager.spendCurrency(a, currency, amount);
        ChatHelper.sendMessage(
            a,
            LanguageModule.getUserLanguage(a)
                .local
                .ECO_PAY_SENT
                .replaceAll(Replacment.NUMBER, "" + spent)
                .replaceAll(Replacment.PLAYER, player.getDisplayNameString()));
        double amt = UserManager.earnCurrency(player, ConfigHandler.serverCurrency, amount);
        ChatHelper.sendMessage(
            player,
            LanguageModule.getUserLanguage(player)
                .local
                .ECO_PAY_EARN
                .replaceAll(Replacment.NUMBER, "" + amt)
                .replaceAll(Replacment.PLAYER, a.getDisplayName().getFormattedText()));
      } catch (NumberFormatException e) {
        ChatHelper.sendMessage(
            a,
            LanguageModule.getUserLanguage(a)
                .local
                .CHAT_INVALID_NUMBER
                .replaceAll(Replacment.NUMBER, userAmount));
      }
    } else {
      ChatHelper.sendMessage(
          a,
          LanguageModule.getUserLanguage(a)
              .local
              .PLAYER_NOT_FOUND
              .replaceAll(Replacment.PLAYER, b));
    }
  }

  @Override
  public List<String> getAliases(List<String> aliases) {
    aliases.add("P");
    return aliases;
  }
}
