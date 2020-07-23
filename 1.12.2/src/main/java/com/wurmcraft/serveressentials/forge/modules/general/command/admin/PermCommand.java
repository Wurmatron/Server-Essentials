package com.wurmcraft.serveressentials.forge.modules.general.command.admin;

import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_COLOR;
import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_INFO_COLOR;

import com.wurmcraft.serveressentials.core.SECore;
import com.wurmcraft.serveressentials.core.api.command.Command;
import com.wurmcraft.serveressentials.core.api.command.CommandArguments;
import com.wurmcraft.serveressentials.core.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.core.api.data.DataKey;
import com.wurmcraft.serveressentials.core.api.json.rank.Rank;
import com.wurmcraft.serveressentials.core.api.player.GlobalPlayer;
import com.wurmcraft.serveressentials.core.api.player.StoredPlayer;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.core.utils.RestRequestGenerator;
import com.wurmcraft.serveressentials.forge.common.utils.PlayerUtils;
import com.wurmcraft.serveressentials.forge.modules.core.event.PlayerDataEvents;
import com.wurmcraft.serveressentials.forge.modules.rank.RankUtils;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

@ModuleCommand(moduleName = "General", name = "Perm")
public class PermCommand {

  @Command(inputArguments = {CommandArguments.STRING, CommandArguments.PLAYER,
      CommandArguments.STRING},
      inputNames = {"User, Group, Check", "Player", "Info, Sync, Fix, Permission_Node"})
  public void user(ICommandSender sender, String commandType, EntityPlayer player,
      String commandArg) {
    if (commandType.equalsIgnoreCase("user")) {
      if (commandArg.equalsIgnoreCase("info")) {
        StoredPlayer playerData = PlayerUtils.getPlayer(player);
        sender.sendMessage(new TextComponentString(
            COMMAND_COLOR + PlayerUtils.getUserLanguage(sender).GENERAL_PERM_USER_RANK
                .replaceAll("%RANK%",
                    COMMAND_INFO_COLOR + playerData.global.rank + COMMAND_COLOR)));
        sender.sendMessage(new TextComponentString(
            COMMAND_COLOR + PlayerUtils.getUserLanguage(sender).GENERAL_PERM_USER_LANG
                .replaceAll("%LANG%", COMMAND_INFO_COLOR
                    + playerData.global.language + COMMAND_COLOR)));
        sender.sendMessage(new TextComponentString(
            COMMAND_COLOR + PlayerUtils.getUserLanguage(sender).GENERAL_PERM_USER_MUTE
                .replaceAll("%MUTE%", COMMAND_INFO_COLOR
                    + playerData.global.muted + COMMAND_COLOR)));
        sender.sendMessage(new TextComponentString(
            COMMAND_COLOR + PlayerUtils.getUserLanguage(sender).GENERAL_PERM_USER_VERIFY
                .replaceAll("%VERIFIED%", COMMAND_INFO_COLOR
                    + ((!playerData.global.discordID.isEmpty()) ? "Yes" : "No")
                    + COMMAND_COLOR)));
      } else if (commandArg.equalsIgnoreCase("sync")) {
        SERegistry
            .delStoredData(DataKey.PLAYER, player.getGameProfile().getId().toString());
        PlayerDataEvents.handleLogin(player);
        sender.sendMessage(new TextComponentString(
            COMMAND_COLOR + PlayerUtils.getUserLanguage(sender).GENERAL_PERM_USER_SYNC
                .replaceAll("%PLAYER%",
                    COMMAND_INFO_COLOR + player.getDisplayNameString() + COMMAND_COLOR)));
      } else if (commandArg.equalsIgnoreCase("fix")) {
        PlayerDataEvents.handAndCheckForErrors(player);
        sender.sendMessage(new TextComponentString(
            COMMAND_COLOR + PlayerUtils.getUserLanguage(sender).GENERAL_PERM_USER_FIX
                .replaceAll("%PLAYER%",
                    COMMAND_INFO_COLOR + player.getDisplayNameString() + COMMAND_COLOR)));
      }
    } else if (commandType.equalsIgnoreCase("check")) {
      boolean hasPerk = RankUtils.hasPermission(RankUtils.getRank(player), commandArg);
      if (hasPerk) {
        sender.sendMessage(new TextComponentString(
            PlayerUtils.getUserLanguage(sender).GENERAL_PERM_CHECK_TRUE
                .replaceAll("%PLAYER%",
                    COMMAND_INFO_COLOR + player.getDisplayNameString() + COMMAND_COLOR)));
      } else {
        sender.sendMessage(new TextComponentString(
            PlayerUtils.getUserLanguage(sender).GENERAL_PERM_CHECK_FALSE
                .replaceAll("%PLAYER%",
                    COMMAND_INFO_COLOR + player.getDisplayNameString() + COMMAND_COLOR)));
      }
    }
  }

  @Command(inputArguments = {CommandArguments.STRING, CommandArguments.PLAYER,
      CommandArguments.STRING, CommandArguments.STRING, CommandArguments.STRING},
      inputNames = {"User", "Player", "Rank, Permission, Perk", "Add,Rem, Set", "Node"})
  public void userMod(ICommandSender sender, String commandType, EntityPlayer player,
      String commandArg, String subArg, String node) {
    if (commandType.equalsIgnoreCase("user")) {
      StoredPlayer playerData = PlayerUtils.getPlayer(player);
      if (commandArg.equalsIgnoreCase("rank")) {
        if (subArg.equalsIgnoreCase("set")) {
          try {
            Rank rank = (Rank) SERegistry.getStoredData(DataKey.RANK, node);
            playerData.global.rank = rank.getID();
            if (SERegistry.globalConfig.dataStorgeType.equalsIgnoreCase("Rest")) {
              SECore.executors.schedule(() -> {
                GlobalPlayer globalData = RestRequestGenerator.User
                    .getPlayer(player.getGameProfile().getId().toString());
                globalData.rank = rank.getID();
                RestRequestGenerator.User
                    .overridePlayer(player.getGameProfile().getId().toString(),
                        globalData);
                StoredPlayer data = PlayerUtils.getPlayer(player);
                data.global = globalData;
                SERegistry.register(DataKey.PLAYER, data);
                PlayerDataEvents.savePlayer(player);
              }, 0, TimeUnit.SECONDS);
            } else {
              PlayerDataEvents.savePlayer(player);
            }
            PlayerDataEvents.savePlayer(player);
          } catch (NoSuchElementException e) {
            sender.sendMessage(new TextComponentString(
                PlayerUtils.getUserLanguage(sender).ERROR_RANK_NOT_FOUND
                    .replaceAll("%RANK%", node)));
          }
        }
      }
    }
  }
//    if (commandType.equalsIgnoreCase("user")) {
//      StoredPlayer playerData = PlayerUtils.getPlayer(player);
//      if (commandArg.equalsIgnoreCase("rank")) {
//        if (subArg.equalsIgnoreCase("set")) {
//          try {
//            Rank rank = (Rank) SERegistry.getStoredData(DataKey.RANK, node);
//            playerData.global.rank = rank.getID();
//            if (SERegistry.globalConfig.dataStorgeType.equalsIgnoreCase("Rest")) {
//              SECore.executors.schedule(() -> {
//                GlobalPlayer globalData = RestRequestGenerator.User
//                    .getPlayer(player.getGameProfile().getId().toString());
//                globalData.rank = rank.getID();
//                RestRequestGenerator.User
//                    .overridePlayer(player.getGameProfile().getId().toString(),
//                        globalData);
//                StoredPlayer data = PlayerUtils.getPlayer(player);
//                data.global = globalData;
//                SERegistry.register(DataKey.PLAYER, data);
//                PlayerDataEvents.savePlayer(player);
//              }, 0, TimeUnit.SECONDS);
//            } else {
//              PlayerDataEvents.savePlayer(player);
//            }
//            PlayerDataEvents.savePlayer(player);
//          } catch (NoSuchElementException e) {
//            sender.sendMessage(new TextComponentString(
//                PlayerUtils.getUserLanguage(sender).ERROR_RANK_NOT_FOUND
//                    .replaceAll("%RANK%", node)));
//          }
//        }
//      } else if (commandArg.equalsIgnoreCase("permission") || commandArg
//          .equalsIgnoreCase("perm")) {
//        if (subArg.equalsIgnoreCase("Add")) {
//          playerData = PlayerUtils.addPerm(playerData, node);
//          sender.sendMessage(new TextComponentString(COMMAND_COLOR + PlayerUtils
//              .getUserLanguage(sender).GENERAL_PERM_USER_PERMISSION_ADD
//              .replaceAll("%NODE%", COMMAND_INFO_COLOR + node + COMMAND_COLOR
//                  .replaceAll("%PLAYER%",
//                      COMMAND_INFO_COLOR + player.getDisplayNameString()
//                          + COMMAND_COLOR))));
//          SERegistry.register(DataKey.PLAYER, playerData);
//        } else if (subArg.equalsIgnoreCase("del") || subArg.equalsIgnoreCase("rem")
//            || subArg.equalsIgnoreCase("remove")) {
//          playerData = PlayerUtils.delPerm(playerData, node);
//          sender.sendMessage(new TextComponentString(COMMAND_COLOR + PlayerUtils
//              .getUserLanguage(sender).GENERAL_PERM_USER_PERMISSION_DEL
//              .replaceAll("%NODE%", COMMAND_INFO_COLOR + node + COMMAND_COLOR
//                  .replaceAll("%PLAYER%",
//                      COMMAND_INFO_COLOR + player.getDisplayNameString()
//                          + COMMAND_COLOR))));
//          SERegistry.register(DataKey.PLAYER, playerData);
//        }
//      } else if (commandArg.equalsIgnoreCase("perk")) {
//        if (subArg.equalsIgnoreCase("Add")) {
//          playerData = PlayerUtils.addPerk(playerData, node);
//          sender.sendMessage(new TextComponentString(COMMAND_COLOR + PlayerUtils
//              .getUserLanguage(sender).GENERAL_PERM_USER_PERK_ADD
//              .replaceAll("%NODE%", COMMAND_INFO_COLOR + node + COMMAND_COLOR
//                  .replaceAll("%PLAYER%",
//                      COMMAND_INFO_COLOR + player.getDisplayNameString()
//                          + COMMAND_COLOR))));
//          SERegistry.register(DataKey.PLAYER, playerData);
//        } else if (subArg.equalsIgnoreCase("del") || subArg.equalsIgnoreCase("rem")
//            || subArg.equalsIgnoreCase("remove")) {
//          playerData = PlayerUtils.delPerk(playerData, node);
//          sender.sendMessage(new TextComponentString(COMMAND_COLOR + PlayerUtils
//              .getUserLanguage(sender).GENERAL_PERM_USER_PERK_DEL
//              .replaceAll("%NODE%", COMMAND_INFO_COLOR + node + COMMAND_COLOR
//                  .replaceAll("%PLAYER%",
//                      COMMAND_INFO_COLOR + player.getDisplayNameString()
//                          + COMMAND_COLOR))));
//          SERegistry.register(DataKey.PLAYER, playerData);
//        }
//      }
//    }
//  }

}
