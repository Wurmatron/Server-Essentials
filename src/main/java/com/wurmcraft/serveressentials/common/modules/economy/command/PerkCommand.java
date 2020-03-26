package com.wurmcraft.serveressentials.common.modules.economy.command;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.api.command.SubCommand;
import com.wurmcraft.serveressentials.api.lang.Lang;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.modules.language.ChatHelper;
import com.wurmcraft.serveressentials.common.reference.Replacment;
import com.wurmcraft.serveressentials.common.utils.command.CommandUtils;
import com.wurmcraft.serveressentials.common.utils.user.UserManager;
import java.util.List;
import joptsimple.internal.Strings;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import org.cliffc.high_scale_lib.NonBlockingHashMap;
import scala.actors.threadpool.Arrays;

@ModuleCommand(moduleName = "Economy")
public class PerkCommand extends Command {

  public static NonBlockingHashMap<String, Double> perks = new NonBlockingHashMap();

  public PerkCommand() {
    perks.put("home", 10000.00);
  }

  @Override
  public String getName() {
    return "Perk";
  }

  @Override
  public String getUsage(Lang lang) {
    return "/perk <buy | list | add> <perkName>";
  }

  @Override
  public String getDescription(Lang lang) {
    return lang.local.DESCRIPTION_PERK;
  }

  private static boolean isValidInput(String arg) {
    return arg.equalsIgnoreCase("Buy")
        || arg.equalsIgnoreCase("List")
        || arg.equalsIgnoreCase("Admin")
        || arg.equalsIgnoreCase("Add")
        || arg.equalsIgnoreCase("Remove")
        || arg.equalsIgnoreCase("delAll")
        || arg.equalsIgnoreCase("remAll");
  }

  @Override
  public void execute(
      MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    if (args.length == 0 || args.length == 1 && !isValidInput(args[0])) {
      ChatHelper.sendMessage(sender, getUsage(senderLang));
    }
  }

  @SubCommand(aliases = "Purchase")
  public void buy(MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    if (args.length == 1 && sender.getCommandSenderEntity() instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
      if (perks.containsKey(args[0])) {
        if (args[0].equalsIgnoreCase("Home")) {
          int maxHomes = UserManager.getMaxHomes(player);
          double cost = (maxHomes + 1) * perks.get("home");
          if (UserManager.getServerCurrency(player.getGameProfile().getId())
              >= ((maxHomes + 1) * perks.get("home"))) {
            UserManager.addPerk(player, "home.amount." + (maxHomes + 1));
            UserManager.spendCurrency(player, ConfigHandler.serverCurrency, cost);
          } else {
            ChatHelper.sendMessage(
                sender, senderLang.local.ECO_NEED.replaceAll(Replacment.COIN, cost + ""));
          }
        }
      } else {
        ChatHelper.sendMessage(
            sender, senderLang.local.ECO_PERK_INVALID.replaceAll(Replacment.PERK, args[0]));
      }
    } else if (args.length == 0) {
      ChatHelper.sendMessage(
          sender,
          TextFormatting.LIGHT_PURPLE + Strings.join(perks.keySet().toArray(new String[0]), ", "));
    } else {
      ChatHelper.sendMessage(sender, getUsage(senderLang));
    }
  }

  @SubCommand
  public void list(MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    ChatHelper.sendMessage(
        sender,
        TextFormatting.LIGHT_PURPLE + Strings.join(perks.keySet().toArray(new String[0]), ", "));
  }

  @SubCommand
  public void add(MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    if (args.length == 2 && sender.getCommandSenderEntity() instanceof EntityPlayer) {
      EntityPlayer player = CommandUtils.getPlayerForName(args[0]);
      if (player != null) {
        if (perks.containsKey(args[1].toLowerCase())) {
          if (args[1].equalsIgnoreCase("Home")) {
            int maxHomes = UserManager.getMaxHomes(player);
            UserManager.addPerk(player, "home.amount." + (maxHomes + 1));
          }
        } else {
          ChatHelper.sendMessage(
              sender, senderLang.local.ECO_PERK_INVALID.replaceAll(Replacment.PERK, args[0]));
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
      return Arrays.asList(new String[] {"buy", "list", "add"});
    } else if (args.length == 1 && args[0].equalsIgnoreCase("buy")) {
      return Arrays.asList(perks.keySet().toArray(new String[0]));
    }
    return super.getAutoCompletion(server, sender, args, pos);
  }
}
