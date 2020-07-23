package com.wurmcraft.serveressentials.forge.modules.rank.command;

import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_COLOR;
import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_INFO_COLOR;
import static com.wurmcraft.serveressentials.forge.api.command.SECommand.ERROR_COLOR;

import com.wurmcraft.serveressentials.core.api.command.Command;
import com.wurmcraft.serveressentials.core.api.command.CommandArguments;
import com.wurmcraft.serveressentials.core.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.core.api.data.DataKey;
import com.wurmcraft.serveressentials.core.api.json.rank.Rank;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.core.utils.RestRequestGenerator;
import com.wurmcraft.serveressentials.forge.common.utils.PlayerUtils;
import com.wurmcraft.serveressentials.forge.modules.rank.RankUtils;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

@ModuleCommand(moduleName = "Rank", name = "Rank")
public class RankCommand {

  @Command(inputArguments = {CommandArguments.PLAYER,
      CommandArguments.STRING}, inputNames = {"Player", "Rank"})
  public void changeRank(ICommandSender sender, EntityPlayer player, String rank) {
    try {
      Rank selectedRank = (Rank) SERegistry.getStoredData(DataKey.RANK, rank);
      RankUtils.setRank(player, selectedRank);
      sender.sendMessage(new TextComponentString(COMMAND_COLOR +
          PlayerUtils.getUserLanguage(sender).RANK_RANK_SET
              .replaceAll("%PLAYER%",
                  COMMAND_INFO_COLOR + player.getDisplayNameString() + COMMAND_COLOR)
              .replaceAll("%RANK%",
                  COMMAND_COLOR + selectedRank.getName() + COMMAND_COLOR)));
    } catch (NoSuchElementException e) {
      sender.sendMessage(new TextComponentString(ERROR_COLOR +
          PlayerUtils.getUserLanguage(sender).ERROR_RANK_NOT_FOUND
              .replaceAll("%RANK%", rank)));
    }
  }

  @Command(inputArguments = {CommandArguments.STRING,
      CommandArguments.STRING}, inputNames = {"create, del", "rank"})
  public void create(ICommandSender sender, String arg, String name) {
    if (arg.equalsIgnoreCase("create") || arg.equalsIgnoreCase("add")) {
      Rank rank = new Rank(name, "[" + name + "]", "", new String[0], new String[0]);
      SERegistry.register(DataKey.RANK, rank);
      if (SERegistry.globalConfig.dataStorgeType.equalsIgnoreCase("Rest")) {
        RestRequestGenerator.Rank.addRank(rank);
      }
      sender.sendMessage(new TextComponentString(
          COMMAND_COLOR + PlayerUtils.getUserLanguage(sender).RANK_RANK_CREATE
              .replaceAll("%RANK%",
                  COMMAND_INFO_COLOR + rank.getName() + COMMAND_COLOR)));
    } else if (arg.equalsIgnoreCase("delete") || arg.equalsIgnoreCase("del") || arg
        .equalsIgnoreCase("remove") || arg.equalsIgnoreCase("rem")) {
      try {
        Rank rank = (Rank) SERegistry.getStoredData(DataKey.RANK, name);
        SERegistry.delStoredData(DataKey.RANK, rank.getID());
        if (SERegistry.globalConfig.dataStorgeType.equalsIgnoreCase("Rest")) {
          RestRequestGenerator.Rank.deleteRank(rank);
        }
        sender.sendMessage(new TextComponentString(
            COMMAND_COLOR + PlayerUtils.getUserLanguage(sender).RANK_RANK_DELETE
                .replaceAll("%RANK%",
                    COMMAND_INFO_COLOR + rank.getName() + COMMAND_COLOR)));
      } catch (NoSuchElementException e) {
        sender.sendMessage(new TextComponentString(ERROR_COLOR +
            PlayerUtils.getUserLanguage(sender).ERROR_RANK_NOT_FOUND
                .replaceAll("%RANK%", name)));
      }
    }
  }

  @Command(inputArguments = {CommandArguments.STRING, CommandArguments.STRING,
      CommandArguments.STRING,
      CommandArguments.STRING}, inputNames = {"meta", "rank", "prefix, suffix", "name"})
  public void metaSmall(ICommandSender sender, String arg, String rank, String type,
      String value) {
    if (arg.equalsIgnoreCase("meta")) {
      try {
        Rank selectedRank = (Rank) SERegistry.getStoredData(DataKey.RANK, rank);
        if (type.equalsIgnoreCase("prefix")) {
          selectedRank.setPrefix(value);
          SERegistry.register(DataKey.RANK, selectedRank);
          if (SERegistry.globalConfig.dataStorgeType.equalsIgnoreCase("Rest")) {
            RestRequestGenerator.Rank.overrideRank(selectedRank);
          }
          sender.sendMessage(new TextComponentString(COMMAND_COLOR +
              PlayerUtils.getUserLanguage(sender).RANK_RANK_PREFIX
                  .replaceAll("%RANK%",
                      COMMAND_INFO_COLOR + selectedRank.getName() + COMMAND_COLOR)
                  .replaceAll("%PREFIX%", value)));
        } else if (type.equalsIgnoreCase("suffix")) {
          selectedRank.setSuffix(value);
          SERegistry.register(DataKey.RANK, selectedRank);
          if (SERegistry.globalConfig.dataStorgeType.equalsIgnoreCase("Rest")) {
            RestRequestGenerator.Rank.overrideRank(selectedRank);
          }
          sender.sendMessage(new TextComponentString(COMMAND_COLOR +
              PlayerUtils.getUserLanguage(sender).RANK_RANK_SUFFIX
                  .replaceAll("%RANK%",
                      COMMAND_INFO_COLOR + selectedRank.getName() + COMMAND_COLOR)
                  .replaceAll("%SUFFIX%", value)));
        }
      } catch (NoSuchElementException e) {
        sender.sendMessage(new TextComponentString(ERROR_COLOR +
            PlayerUtils.getUserLanguage(sender).ERROR_RANK_NOT_FOUND
                .replaceAll("%RANK%", rank)));
      }
    }
  }

  @Command(inputArguments = {CommandArguments.STRING, CommandArguments.STRING,
      CommandArguments.STRING, CommandArguments.STRING,
      CommandArguments.STRING}, inputNames = {"meta", "rank", "add, rem",
      "inheritance, permission", "node"})
  public void meta(ICommandSender sender, String meta, String rank, String type,
      String type2, String value) {
    if (meta.equalsIgnoreCase("meta")) {
      try {
        Rank selectedRank = (Rank) SERegistry.getStoredData(DataKey.RANK, rank);
        if (type.equalsIgnoreCase("add")) {
          if (type2.equalsIgnoreCase("inheritance") || type2.equalsIgnoreCase("i")) {
            List<String> current = Arrays.asList(selectedRank.getInheritance());
            current.add(value);
            selectedRank.setInheritance(current.toArray(new String[0]));
            SERegistry.register(DataKey.RANK, selectedRank);
            if (SERegistry.globalConfig.dataStorgeType.equalsIgnoreCase("Rest")) {
              RestRequestGenerator.Rank.overrideRank(selectedRank);
            }
            sender.sendMessage(new TextComponentString(
                COMMAND_COLOR + PlayerUtils.getUserLanguage(sender).RANK_RANK_INHERIT_ADD
                    .replaceAll("%NODE%", COMMAND_INFO_COLOR + value + COMMAND_COLOR)
                    .replaceAll("%RANK%",
                        COMMAND_INFO_COLOR + selectedRank.getName() + COMMAND_COLOR)));
          } else if (type2.equalsIgnoreCase("permission") || type2
              .equalsIgnoreCase("node") || type2.equalsIgnoreCase("p")) {
            List<String> current = Arrays.asList(selectedRank.getPermission());
            current.add(value);
            selectedRank.setPermission(current.toArray(new String[0]));
            SERegistry.register(DataKey.RANK, selectedRank);
            if (SERegistry.globalConfig.dataStorgeType.equalsIgnoreCase("Rest")) {
              RestRequestGenerator.Rank.overrideRank(selectedRank);
            }
          }
          sender.sendMessage(new TextComponentString(
              COMMAND_COLOR + PlayerUtils.getUserLanguage(sender).RANK_RANK_PERMISSION_ADD
                  .replaceAll("%NODE%", COMMAND_INFO_COLOR + value + COMMAND_COLOR)
                  .replaceAll("%RANK%",
                      COMMAND_INFO_COLOR + selectedRank.getName() + COMMAND_COLOR)));
        } else if (type.equalsIgnoreCase("remove") || type.equalsIgnoreCase("rem")) {
          if (type2.equalsIgnoreCase("inheritance") || type2.equalsIgnoreCase("i")) {
            List<String> current = Arrays.asList(selectedRank.getInheritance());
            current.remove(value);
            selectedRank.setInheritance(current.toArray(new String[0]));
            SERegistry.register(DataKey.RANK, selectedRank);
            if (SERegistry.globalConfig.dataStorgeType.equalsIgnoreCase("Rest")) {
              RestRequestGenerator.Rank.overrideRank(selectedRank);
            }
            sender.sendMessage(new TextComponentString(
                COMMAND_COLOR + PlayerUtils.getUserLanguage(sender).RANK_RANK_INHERIT_DEL
                    .replaceAll("%NODE%", COMMAND_INFO_COLOR + value + COMMAND_COLOR)
                    .replaceAll("%RANK%",
                        COMMAND_INFO_COLOR + selectedRank.getName() + COMMAND_COLOR)));
          } else if (type2.equalsIgnoreCase("permission") || type2
              .equalsIgnoreCase("node") || type2.equalsIgnoreCase("p")) {
            List<String> current = Arrays.asList(selectedRank.getPermission());
            current.remove(value);
            selectedRank.setPermission(current.toArray(new String[0]));
            SERegistry.register(DataKey.RANK, selectedRank);
            if (SERegistry.globalConfig.dataStorgeType.equalsIgnoreCase("Rest")) {
              RestRequestGenerator.Rank.overrideRank(selectedRank);
            }
            sender.sendMessage(new TextComponentString(COMMAND_COLOR + PlayerUtils
                .getUserLanguage(sender).RANK_RANK_PERMISSION_ADD
                .replaceAll("%NODE%", COMMAND_INFO_COLOR + value + COMMAND_COLOR)
                .replaceAll("%RANK%",
                    COMMAND_INFO_COLOR + selectedRank.getName() + COMMAND_COLOR)));
          }
        }
      } catch (NoSuchElementException e) {
        sender.sendMessage(new TextComponentString(ERROR_COLOR +
            PlayerUtils.getUserLanguage(sender).ERROR_RANK_NOT_FOUND
                .replaceAll("%RANK%", rank)));
      }
    }
  }

}
