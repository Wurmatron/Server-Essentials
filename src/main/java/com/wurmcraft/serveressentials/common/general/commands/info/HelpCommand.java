package com.wurmcraft.serveressentials.common.general.commands.info;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.SECommand;
import com.wurmcraft.serveressentials.common.chat.ChatHelper;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.FMLCommonHandler;

@Command(moduleName = "General")
public class HelpCommand extends SECommand {

  private static final Map<String, ICommand> sortedCommand =
      sortCommands(FMLCommonHandler.instance().getMinecraftServerInstance().getCommandManager());
  private static final int CHAT_WIDTH = 80;
  private static final int COMMANDS_PER_PAGE = 8;

  @Override
  public String getName() {
    return "help";
  }

  private static Map<String, ICommand> sortCommands(ICommandManager manager) {
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
    super.execute(server, sender, args);
    int page = 0;
    if (args.length == 1) {
      try {
        page = Integer.parseInt(args[0]);
      } catch (NumberFormatException e) {
        ChatHelper.sendMessage(
            sender, getCurrentLanguage(sender).INVALID_NUMBER.replaceAll("%NUMBER%", args[0]));
      }
    }
    displayPage(sender, page);
  }

  private void displayPage(ICommandSender sender, int page) {
    String pageName = "Page " + page;
    String SPACER = getCurrentLanguage(sender).CHAT_SPACER;
    String HALF_SPACER = SPACER.substring(0, (CHAT_WIDTH / 2));
    String TOP_SPACER = (HALF_SPACER + " " + pageName + " " + HALF_SPACER);
    ChatHelper.sendMessage(sender, TOP_SPACER);
    ChatHelper.sendMessage(sender, SPACER);
  }

  private HashMap<ICommand, String> getPageDisplay(ICommandSender sender, int page) {
    HashMap<ICommand, String> commands = new HashMap<>();
    int noPerms = 0;
    for (int start = (page * COMMANDS_PER_PAGE);
        start < (page * COMMANDS_PER_PAGE) + COMMANDS_PER_PAGE + noPerms;
        start++) {
      if (start + COMMANDS_PER_PAGE < sortedCommand.size()) {
        ICommand command = (ICommand) sortedCommand.values().toArray()[start];
        String formatted = formatCommand(sender, command);
        if (formatted.length() > 0) {
          commands.put(command, formatted);
        } else {
          noPerms++;
        }
      }
    }
    return commands;
  }

  private String formatCommand(ICommandSender sender, ICommand command) {
    if (command != null && command.checkPermission(sender.getServer(), sender)) {
      if (sender.getCommandSenderEntity() instanceof EntityPlayer) {
        if (command instanceof SECommand) {
          return TextFormatting.AQUA
              + "/"
              + command.getName()
              + TextFormatting.GOLD
              + " | "
              + TextFormatting.LIGHT_PURPLE
              + ((SECommand) command).getDescription(sender);
        } else {
          return command.getUsage(sender);
        }
      } else {
        if (command instanceof SECommand) {
          return TextFormatting.AQUA
              + "/"
              + command.getName()
              + TextFormatting.GOLD
              + " | "
              + TextFormatting.LIGHT_PURPLE
              + ((SECommand) command).getDescription(sender);
        } else {
          return command.getUsage(sender);
        }
      }
    }
    return "";
  }

  @Override
  public boolean canConsoleRun() {
    return true;
  }
}
