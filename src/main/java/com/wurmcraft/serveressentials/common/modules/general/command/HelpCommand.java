package com.wurmcraft.serveressentials.common.modules.general.command;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.api.lang.Lang;
import com.wurmcraft.serveressentials.common.modules.language.ChatHelper;
import com.wurmcraft.serveressentials.common.reference.Replacment;
import com.wurmcraft.serveressentials.common.utils.command.CommandUtils;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextFormatting;

@ModuleCommand(moduleName = "General")
public class HelpCommand extends Command {

  public static final int COMMANDS_PER_PAGE = 8;
  private static Command[][] pageCommands;

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
    if (pageCommands == null)
      pageCommands = CommandUtils.generateHelpCommandPages(COMMANDS_PER_PAGE);
    if (args.length == 0) {
      displayCommandPage(sender, senderLang, pageCommands[0]);
    } else if (args.length == 1) {
      try {
        int pageNO = Integer.parseInt(args[0]);
        if (pageNO > pageCommands.length) {
          pageNO = pageCommands.length - 1;
        }
        displayCommandPage(sender, senderLang, pageCommands[pageNO]);
      } catch (NumberFormatException e) {
        ChatHelper.sendMessage(
            sender, senderLang.local.CHAT_INVALID_NUMBER.replaceAll(Replacment.NUMBER, args[0]));
      }
    } else {
      ChatHelper.sendMessage(sender, getUsage(senderLang));
    }
  }

  // TODO Check for permission to run command before displaying
  private static void displayCommandPage(ICommandSender sender, Lang lang, Command[] command) {
    ChatHelper.sendMessage(sender, lang.local.CHAT_SPACER);
    for (Command c : command) {
      ChatHelper.sendMessage(
          sender, TextFormatting.LIGHT_PURPLE + "/" + c.getName() + " " + c.getDescription(lang));
    }
    ChatHelper.sendMessage(sender, lang.local.CHAT_SPACER);
  }
}
