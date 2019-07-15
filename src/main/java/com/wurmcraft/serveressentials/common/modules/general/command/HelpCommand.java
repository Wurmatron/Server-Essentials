package com.wurmcraft.serveressentials.common.modules.general.command;

import com.wurmcraft.serveressentials.api.ServerEssentialsAPI;
import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.api.lang.Lang;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.modules.language.ChatHelper;
import com.wurmcraft.serveressentials.common.reference.Replacment;
import com.wurmcraft.serveressentials.common.utils.command.CommandUtils;
import com.wurmcraft.serveressentials.common.utils.user.UserManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextFormatting;
import org.cliffc.high_scale_lib.NonBlockingHashMap;

@ModuleCommand(moduleName = "General")
public class HelpCommand extends Command {

  public static final int COMMANDS_PER_PAGE = 8;
  private static NonBlockingHashMap<String, Command[][]> pageCommands = new NonBlockingHashMap<>();

  @Override
  public String getName() {
    return "help";
  }

  @Override
  public String getUsage(Lang lang) {
    return "/help <pageNo>";
  }

  @Override
  public String getDescription(Lang lang) {
    return lang.local.DESCRIPTION_HELP;
  }

  @Override
  public void execute(
      MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    if (pageCommands.getOrDefault(getRank(sender), new Command[0][0]).length == 0) {
      pageCommands.put(
          getRank(sender),
          CommandUtils.generateHelpCommandPages(
              COMMANDS_PER_PAGE, ServerEssentialsAPI.rankManager.getRank(getRank(sender))));
    }
    Command[][] commands = pageCommands.get(getRank(sender));
    if (args.length == 0) {
      displayCommandPage(sender, senderLang, commands[0]);
    } else if (args.length == 1) {
      try {
        int pageNO = Integer.parseInt(args[0]);
        if (pageNO > commands.length) {
          pageNO = commands.length - 1;
        }
        displayCommandPage(sender, senderLang, commands[pageNO]);
      } catch (NumberFormatException e) {
        ChatHelper.sendMessage(
            sender, senderLang.local.CHAT_INVALID_NUMBER.replaceAll(Replacment.NUMBER, args[0]));
      }
    } else {
      ChatHelper.sendMessage(sender, getUsage(senderLang));
    }
  }

  private static void displayCommandPage(ICommandSender sender, Lang lang, Command[] command) {
    ChatHelper.sendMessage(sender, lang.local.CHAT_SPACER);
    if (command != null) {
      for (Command c : command) {
        ChatHelper.sendMessage(
            sender, TextFormatting.LIGHT_PURPLE + "/" + c.getName() + " " + c.getDescription(lang));
      }
    }
    ChatHelper.sendMessage(sender, lang.local.CHAT_SPACER);
  }

  public static String getRank(ICommandSender sender) {
    if (sender.getCommandSenderEntity() instanceof EntityPlayer) {
      return UserManager.getUserRank(((EntityPlayer) sender.getCommandSenderEntity())).getID();
    } else if (sender.getCommandSenderEntity() == null) {
      return "*";
    }
    return ConfigHandler.defaultRank;
  }
}
