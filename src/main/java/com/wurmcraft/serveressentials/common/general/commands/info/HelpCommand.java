package com.wurmcraft.serveressentials.common.general.commands.info;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.SECommand;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;

// TODO Hide Commands the user has not perms too use
@Command(moduleName = "General")
public class HelpCommand extends SECommand {

  private static final int chatWidth = 54;
  private static final Map<String, ICommand> prunedAliases =
      pruneAliases(FMLCommonHandler.instance().getMinecraftServerInstance().getCommandManager());

  @Override
  public String getName() {
    return "help";
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "/help <#>";
  }

  private static Map<String, ICommand> pruneAliases(final ICommandManager manager) {
    return new HashMap<String, ICommand>() {
      {
        final Map<String, ICommand> commands = manager.getCommands();
        Set<String> keySet = commands.keySet();
        keySet.forEach(
            (final String k1) ->
                keySet.forEach(
                    (final String k2) -> {
                      if (!k1.equalsIgnoreCase(k2) && !values().contains(commands.get(k1))) {
                        put(k1, commands.get(k1));
                      }
                    }));
      }
    };
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    int start = 0;
    try {
      if (args.length == 1 && Integer.parseInt(args[0]) != -1) {
        start = 8 * Integer.parseInt(args[0]);
      }
      if (start <= prunedAliases.size()) {
        String nPage = " Page # ".replaceAll("#", "" + start / 8);
        StringBuilder b = new StringBuilder();
        int startPos = (int) Math.floor((chatWidth - nPage.length()) / 2);
        b.append(getCurrentLanguage(sender).CHAT_SPACER.substring(0, startPos - 1) + nPage)
            .append(getCurrentLanguage(sender).CHAT_SPACER.substring(0, chatWidth - b.length()));
        if (start / 8 == 0) {
          sender.sendMessage(new TextComponentString(b.toString().replaceAll("&", "\u00A7")));
        } else {
          TextComponentString msg = new TextComponentString(b.toString().replaceAll("&", "\u00A7"));
          msg.getStyle().setClickEvent(clickEvent((start / 8) + 1));
          sender.sendMessage(msg);
        }
        for (int index = start; index < (start + 8); index++) {
          if (index < prunedAliases.size()) {
            TextComponentTranslation temp =
                new TextComponentTranslation(
                    formatCommand(sender, (ICommand) prunedAliases.values().toArray()[index]));
            temp.setStyle(
                new Style()
                    .setColor(TextFormatting.DARK_AQUA)
                    .setClickEvent(
                        commandInteract((ICommand) prunedAliases.values().toArray()[index])));
            sender.sendMessage(temp);
          }
        }
        TextComponentString msg =
            new TextComponentString(
                getCurrentLanguage(sender).CHAT_SPACER.replaceAll("&", "\u00A7"));
        msg.getStyle().setClickEvent(clickEvent((start / 8) + 1));
        sender.sendMessage(
            new TextComponentString(
                getCurrentLanguage(sender).CHAT_SPACER.replaceAll("&", "\u00A7")));
      }
    } catch (NumberFormatException e) {
      sender.sendMessage(
          new TextComponentString(
              getCurrentLanguage(sender).INVALID_NUMBER.replaceAll("%NUMBER%", args[0])));
    }
  }

  private String formatCommand(ICommandSender sender, ICommand command) {
    if (command instanceof SECommand) {
      return TextFormatting.AQUA
          + "/"
          + command.getName()
          + " | "
          + TextFormatting.DARK_AQUA
          + ((SECommand) command).getDescription(sender);
    } else {
      return command.getUsage(sender);
    }
  }

  private ClickEvent clickEvent(int index) {
    return new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/help #".replaceAll("#", "" + index));
  }

  private ClickEvent commandInteract(ICommand command) {
    return new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/" + command.getName() + " ");
  }

  @Override
  public String getDescription(ICommandSender sender) {
    return getCurrentLanguage(sender).COMMAND_HELP;
  }
}
