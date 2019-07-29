package com.wurmcraft.serveressentials.common.modules.economy.command;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.api.command.SubCommand;
import com.wurmcraft.serveressentials.api.lang.Lang;
import com.wurmcraft.serveressentials.api.storage.json.Currency;
import com.wurmcraft.serveressentials.common.modules.language.ChatHelper;
import com.wurmcraft.serveressentials.common.modules.language.LanguageModule;
import com.wurmcraft.serveressentials.common.reference.Replacment;
import com.wurmcraft.serveressentials.common.storage.rest.RequestGenerator.Economy;
import com.wurmcraft.serveressentials.common.utils.command.CommandUtils;
import com.wurmcraft.serveressentials.common.utils.user.UserManager;
import java.util.List;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.FMLCommonHandler;
import scala.actors.threadpool.Arrays;

@ModuleCommand
public class BankCommand extends Command {

  @Override
  public String getName() {
    return "Bank";
  }

  @Override
  public String getUsage(Lang lang) {
    return "/bank <info | exchange | admin> <set | add | consume>";
  }

  @Override
  public String getDescription(Lang lang) {
    return lang.local.DESCRIPTION_BANK;
  }

  private static boolean isValidInput(String arg) {
    return arg.equalsIgnoreCase("Info")
        || arg.equalsIgnoreCase("Exchange")
        || arg.equalsIgnoreCase("Admin");
  }

  @Override
  public void execute(
      MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    if (args.length == 0 || args.length == 1 && !isValidInput(args[0])) {
      ChatHelper.sendMessage(sender, getUsage(senderLang));
    }
  }

  @SubCommand
  public void info(MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    FMLCommonHandler.instance()
        .getMinecraftServerInstance()
        .commandManager
        .executeCommand(sender, "/balance");
  }

  @SubCommand
  public void exchange(
      MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    if (sender.getCommandSenderEntity() instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
      if (args.length == 3) {
        Currency currency = Economy.getEco(args[0]);
        if (currency != null) {
          Currency newCurrency = Economy.getEco(args[1]);
          if (newCurrency != null) {
            try {
              double amount = Double.parseDouble(args[2]);
              if (amount > 0) {
                double newAmount = currency.sell * newCurrency.buy;
                UserManager.spendCurrency(player, args[1], amount);
                UserManager.earnCurrency(player, args[2], newAmount);
                ChatHelper.sendMessage(
                    sender,
                    senderLang
                        .local
                        .ECO_EXCHANGED
                        .replaceAll(Replacment.COIN, args[1])
                        .replaceAll(Replacment.AMOUNT, "" + amount)
                        .replaceAll(Replacment.COIN2, args[2])
                        .replaceAll(Replacment.AMOUNT2, "" + newAmount));
              } else {
                ChatHelper.sendMessage(sender, senderLang.local.MUST_BE_POSITIVE);
              }
            } catch (NumberFormatException e) {
              ChatHelper.sendMessage(
                  sender,
                  senderLang.local.CHAT_INVALID_NUMBER.replaceAll(Replacment.NUMBER, args[2]));
            }
          } else {
            ChatHelper.sendMessage(
                sender, senderLang.local.ECO_INVALID_CURRENCY.replaceAll(Replacment.COIN, args[1]));
          }
        } else {
          ChatHelper.sendMessage(
              sender, senderLang.local.ECO_INVALID_CURRENCY.replaceAll(Replacment.COIN, args[0]));
        }
      } else {
        ChatHelper.sendMessage(
            sender, TextFormatting.LIGHT_PURPLE + "/exchange <currency> <newCurrency> <amount>");
      }
    }
  }

  @SubCommand
  public void admin(MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    if (args.length == 4) {
      EntityPlayer player = CommandUtils.getPlayerForName(args[0]);
      if (player != null) {
        Currency currency = Economy.getEco(args[2]);
        if (currency != null) {
          try {
            double amount = Double.parseDouble(args[3]);
            if (args[1].equalsIgnoreCase("add")) {
              UserManager.earnCurrency(player, currency.name, amount);
              ChatHelper.sendMessage(
                  sender,
                  senderLang
                      .local
                      .ECO_PAY_SENT
                      .replaceAll(Replacment.COIN, currency.name)
                      .replaceAll(Replacment.NUMBER, "" + amount)
                      .replaceAll(Replacment.PLAYER, player.getDisplayNameString()));
              ChatHelper.sendMessage(
                  player,
                  LanguageModule.getUserLanguage(player)
                      .local
                      .ECO_PAY_EARN
                      .replaceAll(Replacment.COIN, currency.name)
                      .replaceAll(Replacment.NUMBER, "" + amount)
                      .replaceAll(
                          Replacment.PLAYER,
                          ((EntityPlayer) sender.getCommandSenderEntity()).getDisplayNameString()));
            } else if (args[1].equalsIgnoreCase("consume")
                || args[1].equalsIgnoreCase("remove")
                || args[1].equalsIgnoreCase("rem")) {
              UserManager.spendCurrency(player, currency.name, amount);
              ChatHelper.sendMessage(
                  player,
                  LanguageModule.getUserLanguage(player)
                      .local
                      .ECO_PAY_SENT
                      .replaceAll(Replacment.COIN, currency.name)
                      .replaceAll(Replacment.NUMBER, "" + amount)
                      .replaceAll(Replacment.PLAYER, player.getDisplayNameString()));
              ChatHelper.sendMessage(
                  sender,
                  senderLang
                      .local
                      .ECO_PAY_EARN
                      .replaceAll(Replacment.COIN, currency.name)
                      .replaceAll(Replacment.NUMBER, "" + amount)
                      .replaceAll(
                          Replacment.PLAYER,
                          ((EntityPlayer) sender.getCommandSenderEntity()).getDisplayNameString()));
            }
          } catch (NumberFormatException e) {
            ChatHelper.sendMessage(
                sender,
                senderLang.local.CHAT_INVALID_NUMBER.replaceAll(Replacment.NUMBER, args[3]));
          }
        } else {
          ChatHelper.sendMessage(
              sender, senderLang.local.ECO_INVALID_CURRENCY.replaceAll(Replacment.COIN, args[2]));
        }
      } else {
        ChatHelper.sendMessage(
            sender, senderLang.local.PLAYER_NOT_FOUND.replaceAll(Replacment.PLAYER, args[0]));
      }
    } else {
      ChatHelper.sendMessage(sender, getUsage(senderLang));
    }
  }

  @Override
  public List<String> getAutoCompletion(
      MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
    if (args.length == 0 || args.length == 1 && args[0].isEmpty()) {
      return Arrays.asList(new String[] {"info", "exchange", "admin"});
    } else if (args.length == 1 || args.length == 2 && args[1].isEmpty()) {
      return CommandUtils.predictCurrency(args, 1);
    } else if (args.length == 2 || args.length == 3 && args[2].isEmpty()) {
      return CommandUtils.predictCurrency(args, 2);
    }
    return super.getAutoCompletion(server, sender, args, pos);
  }
}
