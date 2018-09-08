package com.wurmcraft.serveressentials.common.general.commands.info;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.SECommand;
import com.wurmcraft.serveressentials.api.json.user.Rank;
import com.wurmcraft.serveressentials.common.chat.ChatHelper;
import com.wurmcraft.serveressentials.common.utils.UserManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.annotation.Nullable;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.ClickEvent.Action;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.cliffc.high_scale_lib.NonBlockingHashMap;

@Command(moduleName = "General")
public class HelpCommand extends SECommand {

  private static final int COMMANDS_PER_PAGE = 8;
  private static ICommand[] sortedCommands;
  private static HashMap<Rank, ICommand[]> sortedRankCommands;
  private static final int CHAT_WIDTH = 150;

  public void init() {
    HashMap<String, ICommand> tempSort = FMLCommonHandler.instance()
        .getMinecraftServerInstance()
        .commandManager
        .getCommands()
        .values().stream().collect(Collectors
            .toMap(command -> command.getName().toLowerCase(), command -> command, (a, b) -> a,
                HashMap::new));
    sortedCommands = tempSort.values().toArray(new ICommand[0]);
    sortedRankCommands = new HashMap<>();
    Arrays.stream(UserManager.getRanks())
        .forEach(rank -> sortedRankCommands.put(rank, getRankExclusiveCommands(rank)));
  }

  private ICommand[] getRankExclusiveCommands(Rank rank) {
    HashMap<String, ICommand> tempSort = new HashMap<>();
    for (String perm : rank.getPermission()) {
      if(perm.equalsIgnoreCase("*"))
        return sortedCommands;
      for (ICommand command : sortedCommands) {
        if (command instanceof SECommand && ((SECommand) command).getCommandPerm().equalsIgnoreCase(perm)) {
          tempSort.putIfAbsent(command.getName().toLowerCase(), command);
        }
      }
    }
    return tempSort.values().toArray(new ICommand[0]);
  }

  @Override
  public String getName() {
    return "help";
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "/help <pageNo>";
  }

  @Override
  public List<String> getAltNames() {
    List<String> alts = new ArrayList<>();
    alts.add("h");
    alts.add("?");
    return alts;
  }

  @Override
  public List<String> getTabCompletions(
      MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
    if(sender instanceof EntityPlayer) {
      Rank rank = UserManager.getPlayerRank(((EntityPlayer) sender.getCommandSenderEntity()).getGameProfile().getId());
      return IntStream.range(0, (sortedRankCommands.get(rank).length / COMMANDS_PER_PAGE))
          .mapToObj(index -> "" + index)
          .collect(Collectors.toList());
    }
    return IntStream.range(0, (sortedCommands.length / COMMANDS_PER_PAGE))
        .mapToObj(index -> "" + index)
        .collect(Collectors.toList());
  }

  @Override
  public boolean canConsoleRun() {
    return true;
  }

  @Override
  public String getDescription(ICommandSender sender) {
    return getCurrentLanguage(sender).COMMAND_HELP;
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    super.execute(server, sender, args);
    if (sortedCommands == null || sortedRankCommands == null) {
      init();
    }
    if (sender.getCommandSenderEntity() instanceof EntityPlayer) {
      Rank rank = UserManager
          .getPlayerRank(((EntityPlayer) sender.getCommandSenderEntity()).getGameProfile().getId());
      if (args.length > 0) {
        try {
          displayPage(sender, rank, Integer.parseInt(args[0]));
        } catch (NumberFormatException e) {
          if (UserManager.isValidRank(args[0])) {
            Rank specifiedRank = UserManager.getRank(args[0]);
            displayPage(sender, specifiedRank, 0);
          } else if (args.length == 2 && UserManager.isValidRank(args[0])) {
            Rank specifiedRank = UserManager.getRank(args[0]);
            try {
              displayPage(sender, specifiedRank, Integer.parseInt(args[1]));
            } catch (NumberFormatException f) {
              ChatHelper.sendMessage(
                  sender,
                  getCurrentLanguage(sender).INVALID_NUMBER.replaceAll("%NUMBER%", args[0]));
            }
          } else {
            ChatHelper.sendMessage(
                sender, getCurrentLanguage(sender).INVALID_NUMBER.replaceAll("%NUMBER%", args[0]));
          }
        }
      } else {
        displayPage(sender, rank, 0);
      }
    } else {
      if (args.length > 0) {
        try {
          displayAllPage(sender, Integer.parseInt(args[0]));
        } catch (NumberFormatException e) {
          ChatHelper.sendMessage(
              sender, getCurrentLanguage(sender).INVALID_NUMBER.replaceAll("%NUMBER%", args[0]));
        }
      } else {
        displayAllPage(sender, 0);
      }
    }
  }

  private void displayAllPage(ICommandSender sender, int pageNo) {
    if (pageNo < (sortedCommands.length / COMMANDS_PER_PAGE)) {
      ChatHelper.sendMessage(sender, generateSpacerWithPageNo(sender, pageNo));
      for (ICommand command : getCommandForPage(pageNo)) {
        String description =
            command instanceof SECommand
                ? ((SECommand) command).getDescription(sender)
                : command.getUsage(sender);
        TextComponentTranslation msg = null;
        if (!(command instanceof SECommand)) {
          msg = new TextComponentTranslation(description);
          msg.getStyle().setColor(TextFormatting.LIGHT_PURPLE);
        } else {
          msg = new TextComponentTranslation(
              TextFormatting.LIGHT_PURPLE + "/" + command.getName() + " " + description);
        }
        msg.getStyle()
            .setClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, "/" + command.getName() + " "));
        sender.sendMessage(msg);
      }
      ChatHelper.sendMessage(sender, getCurrentLanguage(sender).CHAT_SPACER);
    } else {
      ChatHelper.sendMessage(sender,
          getCurrentLanguage(sender).INVALID_NUMBER.replaceAll("%NUMBER%", "" + pageNo));
    }
  }

  private void displayPage(ICommandSender sender, Rank rank, int pageNo) {
    if (pageNo-1 < (sortedRankCommands.get(rank).length / COMMANDS_PER_PAGE)) {
      ChatHelper.sendMessage(sender, generateSpacerWithPageNo(sender, pageNo));
      for (ICommand command : getCommandForRankPage(rank, pageNo)) {
        if(command == null)
          continue;
        String description =
            command instanceof SECommand
                ? ((SECommand) command).getDescription(sender)
                : command.getUsage(sender);
        TextComponentTranslation msg;
        if (!(command instanceof SECommand)) {
          msg = new TextComponentTranslation(description);
          msg.getStyle().setColor(TextFormatting.LIGHT_PURPLE);
        } else {
          msg = new TextComponentTranslation(
              TextFormatting.LIGHT_PURPLE + "/" + command.getName() + " " + description);
        }
        msg.getStyle()
            .setClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, "/" + command.getName() + " "));
        sender.sendMessage(msg);
      }
      ChatHelper.sendMessage(sender, getCurrentLanguage(sender).CHAT_SPACER);
    } else {
      ChatHelper.sendMessage(sender,
          getCurrentLanguage(sender).INVALID_NUMBER.replaceAll("%NUMBER%", "" + pageNo));
    }
  }

  private String generateSpacerWithPageNo(ICommandSender sender, int pageNo) {
    String pageName = " Page " + pageNo + " ";
    // TODO Readd color
    final String spacer =
        TextFormatting.getTextWithoutFormattingCodes(getCurrentLanguage(sender).CHAT_SPACER);
    int spacerLength = (CHAT_WIDTH / 2) - pageName.length();
    String FRONT_HALF = spacer.substring(0, spacerLength);
    String BACK_HALF = spacer.substring(spacerLength + 1, (spacerLength * 2) + 1);
    return FRONT_HALF + pageName + BACK_HALF;
  }

  public ICommand[] getCommandForRankPage(Rank rank, int pageNo) {
    return Arrays.copyOfRange(sortedRankCommands.get(rank), (pageNo * COMMANDS_PER_PAGE),
        (pageNo * COMMANDS_PER_PAGE) + COMMANDS_PER_PAGE);
  }

  public ICommand[] getCommandForPage(int pageNo) {
    return Arrays.copyOfRange(sortedCommands, (pageNo * COMMANDS_PER_PAGE),
        (pageNo * COMMANDS_PER_PAGE) + COMMANDS_PER_PAGE);
  }
}
