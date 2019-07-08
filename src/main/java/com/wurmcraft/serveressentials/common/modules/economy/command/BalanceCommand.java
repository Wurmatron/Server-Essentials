package com.wurmcraft.serveressentials.common.modules.economy.command;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.lang.Lang;
import com.wurmcraft.serveressentials.api.module.Module;
import com.wurmcraft.serveressentials.api.user.eco.Coin;
import com.wurmcraft.serveressentials.common.modules.language.ChatHelper;
import com.wurmcraft.serveressentials.common.modules.language.LanguageModule;
import com.wurmcraft.serveressentials.common.reference.Replacment;
import com.wurmcraft.serveressentials.common.utils.command.CommandUtils;
import com.wurmcraft.serveressentials.common.utils.user.UserManager;
import java.util.List;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextFormatting;

@Module(name = "")
public class BalanceCommand extends Command {

  @Override
  public String getName() {
    return "Balance";
  }

  @Override
  public String getUsage(Lang lang) {
    return "/balance <user>";
  }

  @Override
  public String getDescription(Lang lang) {
    return lang.local.DESCRIPTION_BALANCE;
  }

  @Override
  public void execute(
      MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    if (args.length == 0) {
      displayUserCurrency((EntityPlayer) sender.getCommandSenderEntity());
    } else if (args.length == 1) {
      EntityPlayer player = CommandUtils.getPlayerForName(args[0]);
      if (player != null) {
        displayUserCurrency(player);
      } else {
        ChatHelper.sendMessage(
            sender, senderLang.local.PLAYER_NOT_FOUND.replaceAll(Replacment.PLAYER, args[0]));
      }
    } else {
      ChatHelper.sendMessage(sender, getUsage(senderLang));
    }
  }

  @Override
  public List<String> getAliases(List<String> aliases) {
    aliases.add("Bal");
    aliases.add("Money");
    aliases.add("M");
    return aliases;
  }

  private static void displayUserCurrency(EntityPlayer player) {
    ChatHelper.sendMessage(player, LanguageModule.getUserLanguage(player).local.CHAT_SPACER);
    for (Coin coin : UserManager.getUserCoins(player)) {
      ChatHelper.sendMessage(
          player,
          TextFormatting.AQUA
              + coin.getName()
              + ": "
              + TextFormatting.GOLD
              + Math.round(coin.getAmount()));
    }
    ChatHelper.sendMessage(player, LanguageModule.getUserLanguage(player).local.CHAT_SPACER);
  }
}
