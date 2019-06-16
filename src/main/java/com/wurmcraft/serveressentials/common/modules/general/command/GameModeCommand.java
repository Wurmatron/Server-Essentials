package com.wurmcraft.serveressentials.common.modules.general.command;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.api.lang.Lang;
import com.wurmcraft.serveressentials.common.modules.language.ChatHelper;
import com.wurmcraft.serveressentials.common.modules.language.LanguageModule;
import com.wurmcraft.serveressentials.common.reference.Replacment;
import com.wurmcraft.serveressentials.common.utils.command.CommandUtils;
import java.util.Arrays;
import java.util.List;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameType;

@ModuleCommand(moduleName = "General")
public class GameModeCommand extends Command {

  private static final List<String> emptyAutoCompletion = Arrays.asList("0", "1", "2", "3");
  private static final String[] CREATIVE = new String[] {"Creative", "c", "1"};
  private static final String[] SURVIVAL = new String[] {"Survival", "s", "0"};
  private static final String[] ADVENTURE = new String[] {"Adventure", "a", "2"};
  private static final String[] SPECTATOR = new String[] {"Spectator", "sp", "3"};

  @Override
  public String getName() {
    return "GameMode";
  }

  @Override
  public String getUsage(Lang lang) {
    return "/gamemode <user> <type>";
  }

  @Override
  public String getDescription(Lang lang) {
    return lang.local.DESCRIPTION_GAMEMODE;
  }

  @Override
  public void execute(
      MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    if (args.length == 1 && sender.getCommandSenderEntity() instanceof EntityPlayer) {
      int mode = getMode(args[0]);
      if (mode != -1) {
        EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
        player.setGameType(GameType.getByID(mode));
        ChatHelper.sendMessage(
            sender,
            senderLang.local.GENERAL_GM_CHANGED.replaceAll(
                Replacment.MODE, GameType.getByID(mode).getName()));
      } else {
        ChatHelper.sendMessage(
            sender, senderLang.local.GENERAL_GM_INVALID.replaceAll(Replacment.NUMBER, args[0]));
      }
    } else if (args.length == 2) {
      EntityPlayer player = CommandUtils.getPlayerForName(args[0]);
      if (player != null) {
        int mode = getMode(args[0]);
        if (mode != -1) {
          player.setGameType(GameType.getByID(mode));
          ChatHelper.sendMessage(
              player,
              LanguageModule.getUserLanguage(player)
                  .local
                  .GENERAL_GM_CHANGED
                  .replaceAll(Replacment.MODE, GameType.getByID(mode).getName()));
          ChatHelper.sendMessage(
              sender,
              senderLang.local.GENERAL_GM_CHANGED_OTHER.replaceAll(
                  Replacment.MODE, GameType.getByID(mode).getName()));
        } else {
          ChatHelper.sendMessage(
              sender, senderLang.local.GENERAL_GM_INVALID.replaceAll(Replacment.NUMBER, args[0]));
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
  public List<String> getAliases(List<String> aliases) {
    aliases.add("GM");
    aliases.add("Mode");
    return aliases;
  }

  private int getMode(String arg) {
    for (String creative : CREATIVE) {
      if (arg.equalsIgnoreCase(creative)) {
        return 1;
      }
    }
    for (String survival : SURVIVAL) {
      if (arg.equalsIgnoreCase(survival)) {
        return 0;
      }
    }
    for (String adventure : ADVENTURE) {
      if (arg.equalsIgnoreCase(adventure)) {
        return 2;
      }
    }
    for (String spectator : SPECTATOR) {
      if (arg.equalsIgnoreCase(spectator)) {
        return 3;
      }
    }
    return -1;
  }

  @Override
  public boolean canConsoleRun() {
    return true;
  }

  @Override
  public List<String> getAutoCompletion(
      MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
    if (args.length == 0) {
      return emptyAutoCompletion;
    } else {
      return CommandUtils.predictUsernames(args, 0);
    }
  }
}
